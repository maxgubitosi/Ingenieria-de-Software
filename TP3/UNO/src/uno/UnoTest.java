package uno;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnoTest {
    private List<String> oneName;
    private List<String> twoNames;
    private List<String> threeNames;
    private NumericCard blueThree;
    private NumericCard blueSeven;
    private NumericCard yellowTwo;
    private NumericCard greenOne;
    private NumericCard greenFive;
    private NumericCard greenEight;
    private NumericCard greenNine;
    private NumericCard redThree;
    private NumericCard redFive;
    private NumericCard redSeven;
    private NumericCard redNine;
    private Draw2Card redDraw2;
    private Draw2Card blueDraw2;
    private SkipCard blueSkip;
    private ReverseCard yellowRev;
    private UndeployedWildCard undeployedWild;
    DeployedWildCard deployedGreenWild;


    @BeforeEach
    void setUp() {
        oneName = List.of("Solo");
        twoNames = Arrays.asList("Maxi", "Gabor");
        threeNames = Arrays.asList("Maxi", "Gabor", "Ana");

        redThree = new NumericCard(Color.RED, 3);
        redFive = new NumericCard(Color.RED, 5);
        redSeven = new NumericCard(Color.RED, 7);
        redNine = new NumericCard(Color.RED, 9);
        blueThree = new NumericCard(Color.BLUE, 3);
        blueSeven = new NumericCard(Color.BLUE, 7);
        yellowTwo = new NumericCard(Color.YELLOW, 2);
        greenOne = new NumericCard(Color.GREEN, 1);
        greenFive = new NumericCard(Color.GREEN, 5);
        greenEight = new NumericCard(Color.GREEN, 8);
        greenNine = new NumericCard(Color.GREEN, 9);
        redDraw2 = new Draw2Card(Color.RED);
        blueDraw2 = new Draw2Card(Color.BLUE);
        blueSkip = new SkipCard(Color.BLUE);
        yellowRev = new ReverseCard(Color.YELLOW);
        undeployedWild = new UndeployedWildCard();
        deployedGreenWild = new DeployedWildCard(Color.GREEN);

    }

    @Test
    void topCardIsFirstCardFromDeck() {
        Uno game = Uno.withPlayersAndDeck(twoNames, List.of(redFive));
        assertEquals(redFive, game.getTopCard());
    }

    @Test
    void numericCardSameColorIsCompatible() {
        Uno game = Uno.withPlayersAndDeck(twoNames, List.of(blueThree, blueSeven));
        assertDoesNotThrow(() -> game.playCard("Maxi", blueSeven));
    }

    @Test
    void numericCardDifferentColorAndNumberIsNotCompatible() {
        Uno game = Uno.withPlayersAndDeck(twoNames, List.of(yellowTwo, greenNine));
        assertThrows(InvalidMoveException.class,
                () -> game.playCard("Maxi", greenNine));
    }

    @Test
    void bothPlayersCantPlaySequentially() {
        Uno game = Uno.withPlayersAndDeck(
                twoNames,
                List.of(greenOne, greenFive, greenEight)
        );

        assertDoesNotThrow(() -> game.playCard("Maxi", greenFive));
        // juego terminado, maxi se quedo sin cartas
        assertThrows(IllegalStateException.class, () -> game.playCard("Gabor", greenEight));
    }

    @Test
    void bothPlayersPlaySequentially() {
        Uno game = Uno.withPlayersAndDeck(
                twoNames,
                List.of(greenOne, yellowTwo, greenEight, greenFive)
        );

        assertDoesNotThrow(() -> game.playCard("Maxi", greenFive));
        assertDoesNotThrow(() -> game.playCard("Gabor", greenEight));
    }

    @Test
    void playerCantPlayRandomCard() {
        Uno game = Uno.withPlayersAndDeck(
                twoNames,
                List.of(greenOne, greenFive, greenEight, yellowTwo)
        );

        assertThrows(InvalidMoveException.class,() -> game.playCard("Maxi", greenEight));
    }

    @Test
    void playerCantPlayNotHisTurn() {
        Uno game = Uno.withPlayersAndDeck(
                twoNames,
                List.of(greenOne, greenFive, greenEight, yellowTwo)
        );

        assertThrows(InvalidMoveException.class,() -> game.playCard("Gabor", greenEight));
    }

    @Test
    void invalidMoveDoesNotAdvanceGameState() {
        Uno game = Uno.withPlayersAndDeck(
                twoNames,
                List.of(greenOne, greenFive, greenEight, yellowTwo)
        );

        assertThrows(InvalidMoveException.class,() -> game.playCard("Maxi", yellowTwo));
        assertDoesNotThrow(() -> game.playCard("Maxi", greenFive));
        assertDoesNotThrow(() -> game.playCard("Gabor", greenEight));
    }

    @Test
    void drawTwoIsCompatibleByColor() {
        Uno game = Uno.withPlayersAndDeck(twoNames, List.of(redFive, redDraw2));
        assertDoesNotThrow(() -> game.playCard("Maxi", redDraw2));
    }

    @Test
    void drawTwoIsCompatibleByType() {
        Uno game = Uno.withPlayersAndDeck(twoNames, List.of(blueDraw2, redDraw2));
        assertDoesNotThrow(() -> game.playCard("Maxi", redDraw2));
    }

    @Test
    void drawTwoAppliesEffect() {
        List<Card> deck = List.of(
                //top,   maxi,     gabor,     maxi,     gabor,     maxi
                redFive, redDraw2, blueThree, redThree, blueThree, blueThree
        );
        Uno game = Uno.withPlayersAndDeck(twoNames, deck);
        // agregamos cartas a la pila
        // pero sin tener que repartir 7 a cada jugador antes
        game.addCardsToDrawPile(List.of(redSeven, blueThree));
        assertDoesNotThrow(() -> game.playCard("Maxi", redDraw2));
        assertDoesNotThrow(() -> game.playCard("Maxi", redThree));
        assertDoesNotThrow(() -> game.playCard("Gabor", redSeven));
    }

    @Test
    void skipCardSkipsNextPlayer() {
        Uno game = Uno.withPlayersAndDeck(threeNames, List.of(blueThree, blueSkip, yellowTwo, blueSeven, yellowTwo));
        assertDoesNotThrow(() -> game.playCard("Maxi", blueSkip));
        assertEquals("Ana", game.getCurrentPlayer().getName());
        assertDoesNotThrow(() -> game.playCard("Ana", blueSeven));
    }

    @Test
    void reverseCardChangesDirection() {
        Uno game = Uno.withPlayersAndDeck(threeNames, List.of(yellowTwo, yellowRev, blueSeven, yellowTwo, blueSeven));
        assertDoesNotThrow(() -> game.playCard("Maxi", yellowRev));
        assertDoesNotThrow(() -> game.playCard("Ana", yellowTwo));
    }

    @Test
    void gameFailsWithSinglePlayer() {
        assertThrows(IllegalArgumentException.class,
                () -> Uno.withPlayersAndDeck(oneName, List.of(redFive)));
    }

    @Test
    void gameFailsWithNoCardsInDeck() {
        assertThrows(IllegalArgumentException.class,
                () -> Uno.withPlayersAndDeck(twoNames, List.of()));
    }

    @Test
    void undeployedWildCardMustChooseColor() {
        Uno game = Uno.withPlayersAndDeck(twoNames, List.of(redFive, undeployedWild));
        assertThrows(InvalidMoveException.class,
                () -> game.playCard("Maxi", undeployedWild));
    }

    @Test
    void colorizedWildCardLetsNextPlayerFollowColor() {
        Uno game = Uno.withPlayersAndDeck(twoNames, List.of(redFive, undeployedWild, greenNine, redFive));

        assertDoesNotThrow(() ->
                game.playCard("Maxi", undeployedWild.colorize(Color.GREEN)));
        assertEquals(Color.GREEN, game.getTopCard().getColor());
        assertDoesNotThrow(() -> game.playCard("Gabor", greenNine));
    }

    @Test
    void unoDeclaredTooEarlyThrows() {
        List<Card> deck = List.of(
                redFive, redSeven, redThree, blueThree, yellowTwo, greenNine,
                blueSeven, greenNine, yellowTwo, blueSeven, greenNine, yellowTwo
        );
        Uno game = Uno.withPlayersAndDeck(twoNames, deck);
        assertThrows(InvalidMoveException.class,
                () -> game.playCard("Maxi", redSeven.uno()));
    }

    @Test
    void forgettingUnoPreventsWin() {
        List<Card> deck = List.of(
                redFive, redSeven, redThree, redNine, blueThree
        );
        Uno game = Uno.withPlayersAndDeck(twoNames, deck);
        // agregamos cartas a la pila
        // pero sin tener que repartir 7 a cada jugador antes
        game.addCardsToDrawPile(List.of(greenOne, greenFive, greenEight));

        game.playCard("Maxi", redSeven);
        game.playCard("Gabor", redThree);
        game.playCard("Maxi", redNine);

        assertFalse(game.getWinner().isPresent());
    }

    @Test
    void declaringUnoAllowsWin() {
        List<Card> deck = List.of(
                redFive, redSeven, redThree, redNine, blueThree
        );
        Uno game = Uno.withPlayersAndDeck(twoNames, deck);
        // agregamos cartas a la pila
        // pero sin tener que repartir 7 a cada jugador antes
        game.addCardsToDrawPile(List.of(greenOne, greenFive, greenEight));

        game.playCard("Maxi", redSeven.uno());
        game.playCard("Gabor", redThree);
        game.playCard("Maxi", redNine);

        assertTrue(game.getWinner().isPresent());
    }
}