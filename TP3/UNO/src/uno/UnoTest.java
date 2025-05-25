package uno;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnoTest {

    private List<String> playerNameList;

    private NumericCard blueThree;
    private NumericCard blueSeven;
    private NumericCard yellowTwo;
    private NumericCard greenNine;
    private NumericCard redThree;
    private NumericCard redFive;
    private NumericCard redSeven;
    private NumericCard redEight;
    private NumericCard redNine;
    private Draw2Card       redDraw2;
    private SkipCard        blueSkip;
    private ReverseCard     greenReverse;
    private UndeployedWildCard undeployedWild;

    @BeforeEach
    void setUp() {
        playerNameList = Arrays.asList("Maxi", "Gabor");
        redThree    = new NumericCard(Color.RED,    3);
        redFive     = new NumericCard(Color.RED,    5);
        redSeven    = new NumericCard(Color.RED,    7);
        redEight    = new NumericCard(Color.RED,    8);
        redNine     = new NumericCard(Color.RED,    9);
        blueThree   = new NumericCard(Color.BLUE,   3);
        blueSeven   = new NumericCard(Color.BLUE,   7);
        yellowTwo   = new NumericCard(Color.YELLOW, 2);
        greenNine   = new NumericCard(Color.GREEN,  9);
        redDraw2       = new Draw2Card(Color.RED);
        blueSkip       = new SkipCard(Color.BLUE);
        greenReverse   = new ReverseCard(Color.GREEN);
        undeployedWild = new UndeployedWildCard();
    }

    @Test
    void topCardIsFirstCardFromDeck() {
        Uno game = Uno.withPlayersAndDeck(playerNameList, List.of(redFive));
        assertEquals(redFive, game.getTopCard());
    }

    @Test
    void numericCardSameColorIsCompatible() {
        Uno game = Uno.withPlayersAndDeck(playerNameList, List.of(blueThree, blueSeven));
        assertDoesNotThrow(() -> game.playCard("Maxi", blueSeven));
    }

    @Test
    void numericCardDifferentColorAndNumberIsNotCompatible() {
        Uno game = Uno.withPlayersAndDeck(playerNameList, List.of(yellowTwo, greenNine));
        assertThrows(InvalidMoveException.class,
                () -> game.playCard("Maxi", greenNine));
    }

    @Test
    void bothPlayersCantPlaySequentially() {
        NumericCard greenOne  = new NumericCard(Color.GREEN, 1);
        NumericCard greenFive = new NumericCard(Color.GREEN, 5);
        NumericCard greenEight= new NumericCard(Color.GREEN, 8);

        Uno game = Uno.withPlayersAndDeck(
                playerNameList,
                List.of(greenOne, greenFive, greenEight)
        );

        assertDoesNotThrow(() -> game.playCard("Maxi",  greenFive));
        // juego terminado, maxi se quedo sin cartas
        assertThrows(IllegalStateException.class, () -> game.playCard("Gabor", greenEight));
    }

    @Test
    void bothPlayersPlaySequentially() {
        NumericCard greenOne  = new NumericCard(Color.GREEN, 1);
        NumericCard greenFive = new NumericCard(Color.GREEN, 5);
        NumericCard greenEight= new NumericCard(Color.GREEN, 8);
        NumericCard yellowTwo = new NumericCard(Color.YELLOW, 2);

        Uno game = Uno.withPlayersAndDeck(
                playerNameList,
                List.of(greenOne, greenFive, greenEight, yellowTwo)
        );

        assertDoesNotThrow(() -> game.playCard("Maxi",  greenFive));
        assertDoesNotThrow(() -> game.playCard("Gabor", greenEight));
    }

    @Test
    void drawTwoAppliesEffectAndIsCompatibleByColorOrType() {
        Draw2Card draw2 = new Draw2Card(Color.RED);
        Uno game = Uno.withPlayersAndDeck(playerNameList, List.of(redFive, draw2));
        assertDoesNotThrow(() -> game.playCard("Maxi", draw2));
    }

    @Test
    void skipCardSkipsNextPlayer() {
        SkipCard skip = new SkipCard(Color.BLUE);
        Uno game = Uno.withPlayersAndDeck(playerNameList, List.of(blueThree, skip));
        assertDoesNotThrow(() -> game.playCard("Maxi", skip));
    }

    @Test
    void reverseCardChangesDirection() {
        ReverseCard rev = new ReverseCard(Color.YELLOW);
        Uno game = Uno.withPlayersAndDeck(playerNameList, List.of(yellowTwo, rev));
        assertDoesNotThrow(() -> game.playCard("Maxi", rev));
    }

    @Test
    void wildCardAlwaysCompatible() {
        DeployedWildCard wild = new DeployedWildCard(Color.GREEN);
        Uno game = Uno.withPlayersAndDeck(playerNameList, List.of(redFive, wild));
        assertDoesNotThrow(() -> game.playCard("Maxi", wild));
    }

    @Test
    void gameFailsWithSinglePlayer() {
        assertThrows(IllegalArgumentException.class,
                () -> Uno.withPlayersAndDeck(List.of("Solo"), List.of(redFive)));
    }

    @Test
    void reverseReversesDirectionWithThreePlayers() {
        List<String> names = Arrays.asList("Maxi", "Gabor", "Ana");

        NumericCard greenOne  = new NumericCard(Color.GREEN, 1);
        NumericCard greenFive = new NumericCard(Color.GREEN, 5);
        NumericCard greenEight= new NumericCard(Color.GREEN, 8);

        Uno game = Uno.withPlayersAndDeck(
                names,
                List.of(greenOne, greenReverse, greenFive, greenEight, yellowTwo)
        );

        game.playCard("Maxi", greenReverse);
        assertEquals("Ana", game.getCurrentPlayer().getName());
    }

    @Test
    void skipCardReallySkipsNextPlayer() {
        List<String> names = Arrays.asList("Maxi", "Gabor", "Ana");

        Uno game = Uno.withPlayersAndDeck(
                names,
                List.of(blueThree, blueSkip, blueSeven, blueThree, blueSeven)
        );

        game.playCard("Maxi", blueSkip);
        assertEquals("Ana", game.getCurrentPlayer().getName());
    }

    @Test
    void undeployedWildCardMustChooseColor() {
        Uno game = Uno.withPlayersAndDeck(playerNameList, List.of(redFive, undeployedWild));
        assertThrows(InvalidMoveException.class,
                () -> game.playCard("Maxi", undeployedWild));
    }

    @Test
    void deployedWildCardIsPlayable() {
        // top card is RED; Maxi starts with a GREEN deployed wild in hand
        DeployedWildCard greenWild = new DeployedWildCard(Color.GREEN);

        Uno game = Uno.withPlayersAndDeck(
                playerNameList,
                List.of(redFive, greenWild)
        );

        // Maxi plays the pre-coloured wild; should be legal and become top card
        assertDoesNotThrow(() -> game.playCard("Maxi", greenWild));
        assertEquals(Color.GREEN, game.getTopCard().getColor());
    }

    @Test
    void colorizedWildCardLetsNextPlayerFollowColour() {
    /* Deck layout (after top-card removal rows are dealt 7-by-7):
       round-0 Maxi  → undeployedWild
               Gabor → greenNine
    */
        List<Card> deck = List.of(
                redFive,          // top card
                undeployedWild,   // a Maxi
                greenNine,         // a Gabor
                redFive           // a maxi
        );

        Uno game = Uno.withPlayersAndDeck(playerNameList, deck);

        // Maxi colours the wild GREEN and plays it
        assertDoesNotThrow(() ->
                game.playCard("Maxi", undeployedWild.colorize(Color.GREEN)));
        assertEquals(Color.GREEN, game.getTopCard().getColor());

        // Turn passes to Gabor, who can now play GREEN numeric
        assertDoesNotThrow(() -> game.playCard("Gabor", greenNine));
    }

    @Test
    void drawTwoSkipsNextAndReturnsTurn() {
        Uno game = Uno.withPlayersAndDeck(
                playerNameList,
                List.of(redFive, redDraw2, redFive, blueThree)
        );
        game.playCard("Maxi", redDraw2);
        assertEquals("Maxi", game.getCurrentPlayer().getName());
    }

    @Test
    void unoDeclaredTooEarlyThrows() {
        List<Card> deck = List.of(
                redFive, redSeven, redThree, blueThree, yellowTwo, greenNine,
                blueSeven, greenNine, yellowTwo, blueSeven, greenNine, yellowTwo
        );
        Uno game = Uno.withPlayersAndDeck(playerNameList, deck);
        assertThrows(InvalidMoveException.class,
                () -> game.playCard("Maxi", redSeven.uno()));
    }

    @Test
    void forgettingUnoPreventsWin() {
        List<Card> deck = List.of(
                redFive, redSeven, redThree, redNine, blueThree, redEight
        );
        Uno game = Uno.withPlayersAndDeck(playerNameList, deck);

        game.playCard("Maxi", redSeven);
        game.playCard("Gabor", redThree);
        game.playCard("Maxi", redNine);

        assertFalse(game.getWinner().isPresent());
    }

    @Test
    void unoDeclaredAllowsWin() {
        Uno game = Uno.withPlayersAndDeck(playerNameList, List.of(redFive, redSeven));
        game.playCard("Maxi", redSeven.uno());

        assertTrue(game.getWinner().isPresent());
        assertEquals("Maxi", game.getWinner().get().getName());
    }
}