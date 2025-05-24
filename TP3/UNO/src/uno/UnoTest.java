package uno;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnoTest {

    private List<String> players;
    private NumericCard redFive;
    private NumericCard blueThree;
    private NumericCard blueSeven;
    private NumericCard yellowTwo;
    private NumericCard greenNine;

    @BeforeEach
    void setUp() {
        players = Arrays.asList("Maxi", "Gabor");
        redFive     = new NumericCard(Color.RED,    5);
        blueThree   = new NumericCard(Color.BLUE,   3);
        blueSeven   = new NumericCard(Color.BLUE,   7);
        yellowTwo   = new NumericCard(Color.YELLOW, 2);
        greenNine   = new NumericCard(Color.GREEN,  9);
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
        NumericCard greenOne  = new NumericCard(Color.GREEN, 1);
        NumericCard greenFive = new NumericCard(Color.GREEN, 5);
        NumericCard greenEight= new NumericCard(Color.GREEN, 8);

        Uno game = Uno.withPlayersAndDeck(
                players,
                List.of(greenOne, greenFive, greenEight)
        );

        assertDoesNotThrow(() -> game.playCard("Maxi",  greenFive));
        assertDoesNotThrow(() -> game.playCard("Gabor", greenEight));
    }

    @Test
    void drawTwoAppliesEffectAndIsCompatibleByColorOrType() {
        Draw2Card draw2 = new Draw2Card(Color.RED);
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