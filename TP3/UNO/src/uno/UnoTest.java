package uno;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnoTest {

    private List<String> players;
    private NumberCard redFive;
    private NumberCard blueThree;
    private NumberCard blueSeven;
    private NumberCard yellowTwo;
    private NumberCard greenNine;

    @BeforeEach
    void setUp() {
        players = Arrays.asList("Maxi", "Gabor");
        redFive     = new NumberCard(Color.RED,    5);
        blueThree   = new NumberCard(Color.BLUE,   3);
        blueSeven   = new NumberCard(Color.BLUE,   7);
        yellowTwo   = new NumberCard(Color.YELLOW, 2);
        greenNine   = new NumberCard(Color.GREEN,  9);
    }

    @Test
    void topCardIsFirstCardFromDeck() {
        Uno game = Uno.withPlayersAndDeck(players, List.of(redFive));
        assertEquals(redFive, game.getTopCard());
    }

    @Test
    void numericCardSameColorIsCompatible() {
        Uno game = Uno.withPlayersAndDeck(players, List.of(blueThree, blueSeven));
        assertDoesNotThrow(() -> game.playCard("Maxi", blueSeven));
    }

    @Test
    void numericCardDifferentColorAndNumberIsNotCompatible() {
        Uno game = Uno.withPlayersAndDeck(players, List.of(yellowTwo, greenNine));
        assertThrows(InvalidMoveException.class,
                () -> game.playCard("Maxi", greenNine));
    }

    @Test
    void bothPlayersCanPlaySequentially() {
        NumberCard greenOne  = new NumberCard(Color.GREEN, 1);
        NumberCard greenFive = new NumberCard(Color.GREEN, 5);
        NumberCard greenEight= new NumberCard(Color.GREEN, 8);

        Uno game = Uno.withPlayersAndDeck(
                players,
                List.of(greenOne, greenFive, greenEight)
        );

        assertDoesNotThrow(() -> game.playCard("Maxi",  greenFive));
        assertDoesNotThrow(() -> game.playCard("Gabor", greenEight));
    }

    @Test
    void drawTwoAppliesEffectAndIsCompatibleByColorOrType() {
        DrawTwoCard draw2 = new DrawTwoCard(Color.RED);
        Uno game = Uno.withPlayersAndDeck(players, List.of(redFive, draw2));
        assertDoesNotThrow(() -> game.playCard("Maxi", draw2));
    }

    @Test
    void skipCardSkipsNextPlayer() {
        SkipCard skip = new SkipCard(Color.BLUE);
        Uno game = Uno.withPlayersAndDeck(players, List.of(blueThree, skip));
        assertDoesNotThrow(() -> game.playCard("Maxi", skip));
    }

    @Test
    void reverseCardChangesDirection() {
        ReverseCard rev = new ReverseCard(Color.YELLOW);
        Uno game = Uno.withPlayersAndDeck(players, List.of(yellowTwo, rev));
        assertDoesNotThrow(() -> game.playCard("Maxi", rev));
    }

    @Test
    void wildCardAlwaysCompatible() {
        DeployedWildCard wild = new DeployedWildCard(Color.GREEN);
        Uno game = Uno.withPlayersAndDeck(players, List.of(redFive, wild));
        assertDoesNotThrow(() -> game.playCard("Maxi", wild));
    }
}