package uno;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for UNO game logic (basic, special cards and boundary cases).
 */
public class UnoTest {

    private Uno game;

    @BeforeEach
    void setUp() {
        game = Uno.withPlayers("Maxi", "Gabor");
    }

    @Test
    void DealSevenCards() {
        game.start();
        assertEquals(7, game.getPlayer("Maxi").getHand().size());
        assertEquals(7, game.getPlayer("Gabor").getHand().size());
    }

    @Test
    void AllowWhenColorMatches() {
        NumberCard initial = new NumberCard(Color.RED, 5);
        NumberCard toPlay  = new NumberCard(Color.RED, 7);
        game.setTopCard(initial).start();
        game.playCard("Maxi", toPlay);
        assertEquals(toPlay, game.getTopCard());
    }

    @Test
    void ThrowWhenPlayingInvalidCard() {
        NumberCard initial = new NumberCard(Color.RED, 5);
        NumberCard toPlay  = new NumberCard(Color.BLUE, 7);
        game.setTopCard(initial)
                .start();
        assertThrows(InvalidMoveException.class,
                () -> game.playCard("Maxi", toPlay)
        );
    }

    @Test
    void ApplyDrawTwoEffect() {
        NumberCard initial = new NumberCard(Color.RED, 5);
        DrawTwoCard draw2 = new DrawTwoCard(Color.RED);
        game.setTopCard(initial).start();
        int gaborBefore = game.getPlayer("Gabor").getHand().size();
        game.playCard("Maxi", draw2);
        assertEquals(gaborBefore + 2, game.getPlayer("Gabor").getHand().size());
        assertEquals(draw2, game.getTopCard());
    }

    @Test
    void ApplySkipEffect() {
        NumberCard initial = new NumberCard(Color.GREEN, 3);
        SkipCard skip    = new SkipCard(Color.GREEN);
        game.setTopCard(initial)
                .start();
        int gaborBefore = game.getPlayer("Gabor").getHand().size();
        game.playCard("Maxi", skip);
        assertEquals(gaborBefore, game.getPlayer("Gabor").getHand().size());
        assertEquals(skip, game.getTopCard());
    }

    @Test
    void ApplyReverseEffect() {
        NumberCard initial = new NumberCard(Color.YELLOW, 2);
        ReverseCard rev   = new ReverseCard(Color.YELLOW);
        game.setTopCard(initial)
                .start();
        int gaborBefore = game.getPlayer("Gabor").getHand().size();
        game.playCard("Maxi", rev);
        assertEquals(gaborBefore, game.getPlayer("Gabor").getHand().size());
        assertEquals(rev, game.getTopCard());
    }

    @Test
    void ApplyWildEffect() {
        NumberCard initial = new NumberCard(Color.BLUE, 1);
        WildCard wild     = new WildCard(Color.GREEN);
        game.setTopCard(initial)
                .start();
        game.playCard("Maxi", wild);
        assertEquals(wild, game.getTopCard());
    }
}
