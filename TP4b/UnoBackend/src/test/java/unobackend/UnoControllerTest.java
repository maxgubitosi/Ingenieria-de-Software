package unobackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import unobackend.controller.UnoController;
import unobackend.model.JsonCard;
import unobackend.service.UnoService;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnoController.class)
class UnoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;
    @MockBean  UnoService unoService;

    @Test
    void newMatch_ReturnsUuid() throws Exception {
        UUID fakeId = UUID.randomUUID();
        when(unoService.newMatch(Arrays.asList("Emilio","Julio"))).thenReturn(fakeId);

        mockMvc.perform(post("/newmatch")
                        .param("players", "Emilio", "Julio"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"" + fakeId.toString() + "\""));
            // TODO: ver si posta deberia devolver con "" el controller o no.
            // osea esta devolviendo "bffaaa9c-47d7-4b11-95c9-12a10d527662" ahora
            //
    }

    @Test
    void play_ValidCard_ReturnsOk() throws Exception {
        JsonCard card = new JsonCard("Red", 5, "NumberCard", false);
        String json = mapper.writeValueAsString(card);

        mockMvc.perform(post("/play/{matchId}/{player}", UUID.randomUUID(), "Alice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void play_InvalidTurn_ReturnsBadRequest() throws Exception {
        JsonCard badCard = new JsonCard("Blue", null, "SkipCard", false);
        String json = mapper.writeValueAsString(badCard);
        UUID matchId = UUID.randomUUID();

        doThrow(new IllegalStateException("Not your turn"))
                .when(unoService).playerPlaysCard(matchId, "Alice", badCard);

        mockMvc.perform(post("/play/{matchId}/{player}", matchId, "Alice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Not your turn"));
    }
}