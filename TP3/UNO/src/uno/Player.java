package uno;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un jugador con su mano de cartas.
 */
public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }
}