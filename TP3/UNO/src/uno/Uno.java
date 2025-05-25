package uno;

import java.util.*;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.AbstractCollection;

public class Uno {

    private final PlayerRing players = new PlayerRing();
    private final Deque<Card> drawPile = new ArrayDeque<>();

    private Card       topCard;
    private Controller turnController;

    private boolean finished = false;
    private Player  winner   = null;

    public Optional<Player> getWinner()  { return Optional.ofNullable(winner); }
    public void finish(Player winner) {
        finished = true;
        this.winner = winner;
    }

    private static abstract class Controller {
        protected Controller twin;
        abstract void   advance(Uno g);
        abstract Player peekNext(Uno g);
        Controller twin() {return twin;};
    }
    private static final class RightController extends Controller {
        void setTwin(Controller t) { twin = t; }
        @Override void   advance(Uno g) { g.players.forward(); }
        @Override Player peekNext(Uno g) { return g.players.next(); }
    }
    private static final class LeftController extends Controller {
        void setTwin(Controller t) { twin = t; }
        @Override void   advance(Uno g) { g.players.backward(); }
        @Override Player peekNext(Uno g) { return g.players.prev(); }
    }

    private Uno() {}

    public static Uno withPlayersAndDeck(List<String> names, List<Card> deck) {
        if (names == null || names.size() < 2)
            throw new IllegalArgumentException("Uno requiere al menos dos jugadores");
        if (deck == null || deck.isEmpty())
            throw new IllegalArgumentException("La baraja no puede estar vacía");

        Uno uno = new Uno();

        names.forEach(n -> uno.players.add(new Player(n)));

        uno.drawPile.addAll(new ArrayDeque<>(deck));

        uno.topCard = uno.drawPile.poll();

        for (int round = 0; round < 7 && !uno.drawPile.isEmpty(); round++) {
            for (Player p : uno.players) {
                if (uno.drawPile.isEmpty()) break;
                p.addCard(uno.drawPile.poll());
            }
        }

        RightController rc = new RightController();
        LeftController  lc = new LeftController();
        rc.setTwin(lc);  lc.setTwin(rc);
        uno.turnController = rc;

        return uno;
    }

    public void playCard(String playerName, Card card) {
        if (finished) throw new IllegalStateException("La partida ya terminó. No se puede seguir jugando cartas.");
        getPlayer(playerName).playCard(this, card);
        turnController.advance(this);
    }

    public void drawCardsForCurrentPlayer(int count) {
        Player currentPlayer = getCurrentPlayer();
        for (int i = 0; i < count && !drawPile.isEmpty(); i++) {
            currentPlayer.addCard(drawPile.pop());
        }
    }

    public void drawCardsForNextPlayer(int count) {
        Player next = turnController.peekNext(this);
        for (int i = 0; i < count && !drawPile.isEmpty(); i++)
            next.addCard(drawPile.pop());
    }

    public void addCardsToDrawPile(Collection<Card> cards) {
        if (cards == null || cards.isEmpty()) return;
        drawPile.addAll(cards);
    }

    public void reverseDirection() { turnController = turnController.twin(); }
    public void skipNextPlayer()   { turnController.advance(this); }

    public Player getPlayer(String name) {
        return players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No existe jugador: " + name));
    }

    public Card getTopCard()      { return topCard;     }
    public Player getCurrentPlayer() { return this.players.curr(); }

    public void setTopCard(Card card) { topCard = card; }

    // Usamos AbstractCollection en vez de Iterable
    // para poder usar .stream() en getPlayer()
    public static final class PlayerRing extends AbstractCollection<Player> {

        private final LinkedList<Player> list = new LinkedList<>();

        @Override
        public boolean add(Player p) { return list.add(p); }

        Player curr() { return list.getFirst(); }
        Player next() { return list.get(1); }
        Player prev() { return list.getLast(); }

        void forward()  { list.addLast(list.removeFirst()); }
        void backward() { list.addFirst(list.removeLast()); }

        @Override public int     size()        { return list.size(); }
        @Override public Iterator<Player> iterator() { return list.iterator(); }
    }
}