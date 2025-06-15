package unobackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.beans.factory.annotation.Autowired;

import unobackend.model.*;
import unobackend.service.Dealer;
import unobackend.service.UnoService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UnoServiceTest {

    @Autowired UnoService unoService;
    @MockBean Dealer dealer;

    @BeforeEach
    void beforeEach() {when(dealer.fullDeck()).thenReturn(fullDeck());}

    public static List<Card> fullDeck() {
        return List.of( new NumberCard("Red", 1),
                        // cartas emilio
                        new WildCard(),
                        new WildCard(),
                        new ReverseCard("Red"),
                        new Draw2Card("Red"),
                        new SkipCard("Red"),
                        new NumberCard("Red", 2),
                        new NumberCard("Red", 2),
                        // cartas julio
                        new NumberCard("Red", 3),
                        new NumberCard("Red", 3),
                        new NumberCard("Blue", 3),
                        new NumberCard("Blue", 3),
                        new NumberCard("Green", 4),
                        new NumberCard("Green", 4),
                        new NumberCard("Green", 4),
                        // deck
                        new NumberCard("Green", 4));
    }

    @Test
    void newMatch_ReturnsUniqueIds() {
        UUID id1 = unoService.newMatch(List.of("Emilio", "Julio"));
        UUID id2 = unoService.newMatch(List.of("Emilio", "Julio"));

        assertNotEquals(id1, id2, "Every new match must get a fresh UUID");
    }

    @Test
    void newMatch_TooFewPlayersThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> unoService.newMatch(List.of("Solo")),
                "Service should reject 1 player match");
    }

    @Test
    void newMatch_PlayersStartWithSevenCards() {
        UUID matchId = unoService.newMatch(List.of("Emilio", "Julio"));

        int initialSize = unoService.playerHand(matchId).size();

        assertEquals(7, initialSize,
                "Each player should start with 7 cards");
    }

    @Test
    void play_WrongTurnThrowsException() {
        UUID matchId = unoService.newMatch(List.of("Emilio", "Julio"));

        assertThrows(RuntimeException.class,
                () -> unoService.play(matchId, "Julio", new WildCard()),
                "Playing out of turn should raise an error");
    }

    @Test
    void play_UpdatesHandAndActive() {
        UUID matchId = unoService.newMatch(List.of("Emilio", "Julio"));

        int emilioSizeBefore = unoService.playerHand(matchId).size();   // 7

        Card red2 = new NumberCard("Red", 2);
        assertTrue(unoService.playerHand(matchId).contains(red2), "Emilio must have Red 2");
        unoService.play(matchId, "Emilio", red2);

        Card red3 = new NumberCard("Red", 3);
        assertTrue(unoService.playerHand(matchId).contains(red3), "Julio must have Red 3");
        unoService.play(matchId, "Julio", red3);

        assertEquals(red3, unoService.activeCard(matchId));

        int emilioSizeAfter = unoService.playerHand(matchId).size();
        assertEquals(emilioSizeBefore - 1, emilioSizeAfter,
                "After playing a card Emilio’s hand shrunk by 1");
    }

    @Test
    void drawCard_IncreasesHandByOne() {
        UUID matchId = unoService.newMatch(List.of("Emilio", "Julio"));

        int before = unoService.playerHand(matchId).size();
        unoService.drawCard(matchId, "Emilio");
        int after  = unoService.playerHand(matchId).size();

        assertEquals(before + 1, after,
                "Drawing a card should increase hand size by one");
    }

    @Test
    void drawCard_WrongPlayerThrowsException() {
        UUID matchId = unoService.newMatch(List.of("Emilio", "Julio"));

        assertThrows(RuntimeException.class,
                () -> unoService.drawCard(matchId, "NonExistingPlayer"),
                "Service must reject drawCard from an invalid player");
    }

    @Test
    void activeCard_NotNullAfterNewMatch() {
        UUID matchId = unoService.newMatch(List.of("Emilio", "Julio"));
        Card active = unoService.activeCard(matchId);

        assertNotNull(active, "Active card should be set after starting a match");
    }

    @Test
    void activeCard_InvalidMatchIdThrowsException() {
        UUID rand = UUID.randomUUID();
        assertThrows(RuntimeException.class,
                () -> unoService.activeCard(rand),
                "Active card lookup with unknown UUID should fail");
    }

    @Test
    void playerHand_InvalidIdThrowsException() {
        UUID matchId = unoService.newMatch(List.of("Emilio", "Julio"));
        UUID rand = UUID.randomUUID();

        assertThrows(RuntimeException.class,
                () -> unoService.playerHand(rand),
                "Lookup with an unknown UUID should fail");
    }

    @Test
    void multipleMatchesIsolatedSessions() {
        UUID id1 = unoService.newMatch(List.of("Emilio", "Julio"));
        UUID id2 = unoService.newMatch(List.of("Ana", "Bruno"));

        Card active2Before = unoService.activeCard(id2);
        int  anaHandBefore = unoService.playerHand(id2).size();

        Card card1 = unoService.playerHand(id1).get(0);
        unoService.play(id1, "Emilio", card1);

        assertEquals(active2Before, unoService.activeCard(id2),
                "Match 2’s active card should not change");
        assertEquals(anaHandBefore, unoService.playerHand(id2).size(),
                "Match 2’s hands should be unchanged");
    }





}

