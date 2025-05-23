package uno;

import java.util.*;

public class Uno {
    private final List<Player> players = new ArrayList<>();
    private final Deque<Card> drawPile = new ArrayDeque<>();
    private final Deque<Card> discardPile = new ArrayDeque<>();
    private Card topCard;
    private Player currentPlayer;
    private Controller controller;

    private static abstract class Controller {
        abstract Player next(Player current);
        abstract Controller twin();
    }

    private static class RightController extends Controller {
        private Controller twin;
        void setTwin(Controller twin) { this.twin = twin; }
        @Override public Player next(Player current) { return current.getNext(); }
        @Override public Controller twin() { return twin; }
    }

    private static class LeftController extends Controller {
        private Controller twin;
        void setTwin(Controller twin) { this.twin = twin; }
        @Override public Player next(Player current) { return current.getPrev(); }
        @Override public Controller twin() { return twin; }
    }

    private Uno() {}

    public static Uno withPlayersAndDeck(List<String> names, List<Card> deck) {
        Uno game = new Uno();
        for (String name : names) {
            game.players.add(new Player(name));
        }

        Iterator<Card> it = deck.iterator();
        if (it.hasNext()) {
            Card first = it.next();
            game.topCard = first;
            game.discardPile.push(first);
        }

        int idx = 0;
        while (it.hasNext()) {
            Player p = game.players.get(idx);
            Card c = it.next();
            if (p.getHand().size() < 7) {
                p.addCard(c);
            } else {
                game.drawPile.addLast(c);
            }
            idx = (idx + 1) % game.players.size();
        }

        for (int i = 0; i < game.players.size(); i++) {
            Player curr = game.players.get(i);
            Player next = game.players.get((i + 1) % game.players.size());
            Player prev = game.players.get((i - 1 + game.players.size()) % game.players.size());
            curr.setNext(next);
            curr.setPrev(prev);
        }

        RightController rc = new RightController();
        LeftController lc = new LeftController();
        rc.setTwin(lc);
        lc.setTwin(rc);
        game.controller = rc;
        game.currentPlayer = game.players.getFirst();

        return game;
    }




    public Player getPlayer(String name) {
        return players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No existe jugador: " + name));
    }

    public void playCard(String playerName, Card card) {
        if (topCard != null && !card.isCompatible(topCard)) {
            throw new InvalidMoveException("Movimiento inválido");
        }

        Player p = getPlayer(playerName);
        p.playCard(card);

        topCard = card;
        discardPile.push(card);
        card.applyEffect(this);

        advanceTurn();
    }


    private void advanceTurn() {
        currentPlayer = controller.next(currentPlayer);
    }

    public void skipNextPlayer() {
        currentPlayer = controller.next(currentPlayer);
    }

    public void reverseDirection() {
        controller = controller.twin();
    }

    public void drawCardsForNextPlayer(int count) {
        Player next = controller.next(currentPlayer);
        for (int i = 0; i < count; i++) {
            if (drawPile.isEmpty()) break;
            next.addCard(drawPile.pop());
        }
        skipNextPlayer();
    }

    public Card getTopCard() {
        return topCard;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public void reverseController() {
        controller = controller.twin();
    }
}
