package unobackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import unobackend.model.JsonCard;
import unobackend.model.Player;
import unobackend.service.UnoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;

import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;

@SpringBootTest
@AutoConfigureMockMvc
class UnoControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean  UnoService unoService; // TODO: julio tenia unoService en Autowired no en MockBean. pero para recibir un UUID especifico del service necesitamos Mockear.

    UUID mockedId;

    @BeforeEach
    void beforeEach() {
        mockedId = UUID.randomUUID();
    }
    /* ---------- newMatch ---------- */

    @Test
    void newMatch_ReturnsUuid200() throws Exception {
        when(unoService.newMatch(Arrays.asList("Emilio","Julio"))).thenReturn(mockedId);

        mockMvc.perform(post("/newmatch")
                        .param("players", "Emilio", "Julio"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + mockedId.toString() + "\""));
            // TODO: ver si posta deberia devolver con "" el UUID o no.
    }

    @Test
    void newMatch_BadPlayersReturns400() throws Exception {
        when(unoService.newMatch(Arrays.asList(null, null)))
                .thenThrow(new RuntimeException("bad players"));

        mockMvc.perform(post("/newmatch").param("players",null,null))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Runtime Error: bad players"));
    }

    /* ---------- play ---------- */

    @Test
    void play_SuccessReturns200() throws Exception {
        String cardJson = """
        {"color":"Blue","number":6,"type":"NumberCard","shout":false}
        """;
        doNothing().when(unoService)
                .play(eq(mockedId), eq("Emilio"), any());
        mockMvc.perform(post("/play/" + mockedId + "/Emilio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardJson))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    // TODO: esta era la implementacion de Julio, usa unoService en autowired entonces newGame entrega un UUID, pero como lo mockie, ya no puedo usar newMatch pq devuelve null en vez de UUID
    // me parece que esta bien mockear el service entero total lo estamos testiando en unoServiceTest en autowired, y ahi si mockeo solo Dealer
//    @Test
//    void play_WrongTurnReturns400() throws Throwable {
//        String uuid = newGame();
//        assertNotNull(UUID.fromString(uuid));
//
//        List<JsonCard> cards = activeHand(uuid);
//
//        String resp = mockMvc.perform(post("/play/" + uuid + "/Julio")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(cards.getFirst().toString()))
//                .andDo(print())
//                .andExpect(status().is(400)).andReturn().getResponse().getContentAsString(); // 400 Bad request (no es turno de Julio)
//
//        assertEquals("Runtime Error: " + Player.NotPlayersTurn + "Julio", resp);
//    }

    @Test
    void play_WrongTurnReturns400() throws Exception {
        when(unoService.newMatch(Arrays.asList("Emilio", "Julio")))
                .thenReturn(mockedId);

        JsonCard blueSix = new JsonCard("Blue", 6, "NumberCard", false);
        when(unoService.playerHand(mockedId)).thenReturn(List.of(blueSix.asCard()));

        doThrow(new RuntimeException(Player.NotPlayersTurn + "Julio"))
                .when(unoService).play(eq(mockedId), eq("Julio"), any());

        String uuid = newGame();
        assertEquals(mockedId.toString(), uuid);

        List<JsonCard> cards = activeHand(uuid);

        String resp = mockMvc.perform(post("/play/" + uuid + "/Julio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cards.getFirst().toString()))
                .andExpect(status().isBadRequest())   // 400
                .andReturn().getResponse().getContentAsString();

        assertEquals("Runtime Error: " + Player.NotPlayersTurn + "Julio", resp);
    }

    /* ---------- drawCard ---------- */

    @Test
    void drawCard_SuccessReturns200() throws Exception {
        doNothing().when(unoService).drawCard(mockedId,"Emilio");

        mockMvc.perform(post("/draw/" + mockedId + "/Emilio"))
                .andExpect(status().isOk());
    }

    @Test
    void drawCard_ServiceThrowsReturns500() throws Exception {
        doThrow(new IllegalArgumentException("no cards"))
                .when(unoService).drawCard(mockedId,"Emilio");

        mockMvc.perform(post("/draw/" + mockedId + "/Emilio"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error: no cards"));
    }

    /* ---------- activeCard ---------- */

    @Test
    void activeCard_SuccessReturnsJson200() throws Exception {
        JsonCard json = new JsonCard("Blue",6,"NumberCard",false);
        when(unoService.activeCard(mockedId)).thenReturn(json.asCard());

        mockMvc.perform(post("/activecard/" + mockedId))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(json)));
    }

    @Test
    void activeCard_MatchNotFoundReturns500() throws Exception {
        when(unoService.activeCard(mockedId))
                .thenThrow(new IllegalArgumentException("match not found"));

        mockMvc.perform(post("/activecard/" + mockedId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error: match not found"));
    }

    /* ---------- playerHand ---------- */

    @Test
    void playerHand_SuccessReturnsList() throws Exception {
        JsonCard blueSix  = new JsonCard("Blue", 6, "NumberCard", false);
        JsonCard redFive  = new JsonCard("Red", 5, "NumberCard", false);

        when(unoService.playerHand(mockedId))
                .thenReturn(List.of(blueSix.asCard(), redFive.asCard()));

        mockMvc.perform(post("/playerhand/" + mockedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("Blue"))
                .andExpect(jsonPath("$[0].number").value(6))
                .andExpect(jsonPath("$[1].color").value("Red"))
                .andExpect(jsonPath("$[1].number").value(5));
    }

    @Test
    void playerHand_WrongIdReturns400() throws Exception {
        when(unoService.playerHand(mockedId))
                .thenThrow(new RuntimeException("bad id"));

        mockMvc.perform(post("/playerhand/" + mockedId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Runtime Error: bad id"));
    }

    /* ---------- helpers ---------- */

    private String newGame() throws Exception {
        String resp = mockMvc.perform(post( "/newmatch?players=Emilio&players=Julio"))
            .andExpect(status().is(200))
            .andReturn().getResponse().getContentAsString();

        return new ObjectMapper().readTree(resp).asText();
    }

    private List<JsonCard> activeHand(String uuid) throws Exception {
        String resp = mockMvc.perform(post("/playerhand/" + uuid))
                    .andExpect(status().is(200)).andReturn()
                    .getResponse().getContentAsString();

        return new ObjectMapper().readValue(resp, new TypeReference<List<JsonCard>>() {});
    }
}