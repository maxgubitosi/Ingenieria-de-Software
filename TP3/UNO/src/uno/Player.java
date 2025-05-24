package uno;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

//    public List<Card> getHand() {
//        return hand;
//    }

    public void addCard(Card card) {
        hand.add(card);
    }

    // TODO: no me queda claro si se pueden sacar estos ifs.
    public void playCard(Uno game, Card card) {            // old entry point
        playCard(game, card, null);                        // delegate
    }

    public void playCard(Uno game, Card card, Color color) {
        if (this != game.getCurrentPlayer())
            throw new InvalidMoveException("No es el turno de " + name);
        if (!card.isCompatible(game.getTopCard()))
            throw new InvalidMoveException("Carta no es compatible");
        if (!hand.remove(card)) {
            throw new InvalidMoveException("Jugador " + name + " no tiene la carta especificada");
        }
        game.setTopCard(card.deploy(color).applyEffect(game));
    }
}