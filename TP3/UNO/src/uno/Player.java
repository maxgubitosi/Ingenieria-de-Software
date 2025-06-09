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

    public void addCard(Card card) {
        hand.add(card);
    }

    public void playCard(Uno game, Card card) {
        if (this != game.getCurrentPlayer())
            throw new InvalidMoveException("No es el turno de " + name);

        boolean saidUno = card.declaredUno();
        card = card.unwrap();

        if (!card.isCompatible(game.getTopCard()))
            throw new InvalidMoveException("Carta no es compatible");
        if (hand.size() == 2 && !saidUno) {
            game.drawCardsForCurrentPlayer(2);
        } else if (hand.size() > 2 && saidUno) {
            throw new InvalidMoveException("UNO se declara solo al quedar con una carta");
        }
        if (!hand.remove(card)) {
            throw new InvalidMoveException("Jugador " + name + " no tiene la carta especificada");
        }

        game.setTopCard(card.applyEffect(game));

        if (hand.isEmpty()) {game.finish(this);}
    }
}