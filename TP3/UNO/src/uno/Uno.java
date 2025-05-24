package uno;

import java.util.*;

public class Uno {

    private final PlayerRing players = new PlayerRing();
    private final Deque<Card> drawPile = new ArrayDeque<>();

    private Card       topCard;
    private Controller turnController;

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

        // TODO: cambiar por version funcional???
        //  me quedo medio feucho el tema del queue de cartas y repartija
        //  quizas el player tiene que controlar lo de las 7 cartas. no el for este
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


    // TODO: quizas poner los Throws en el method signature? pq dijo que deverdad los que queres que el usuario conozca los haces exception asi le avisa al usuario que existe esa exception, sino no compila. osea hacer que InvalidMoveException extienda a Exception en vez de RuntimeException
    // TODO: quedo raro que todo ocurra dentro del playCard del Player, nose si esta bien
    public void playCard(String playerName, Card card) {
        getPlayer(playerName).playCard(this, card);
        turnController.advance(this);
    }

    public void drawCardsForNextPlayer(int count) {
        Player next = turnController.peekNext(this);
        for (int i = 0; i < count && !drawPile.isEmpty(); i++)
            next.addCard(drawPile.pop());
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
    // TODO: nose si hace falta esto o si esto es mas de la interfaz del juego que del modelo
    public Player getCurrentPlayer() { return this.players.curr(); }
    public int  getPlayersCount() { return players.size(); }

    public void setTopCard(Card card) { topCard = card; }

    // TODO: la verdad que esta re nasty esta implementacion de anillo, nose que me paso. pero el doubble linked list de java es medio feo comparado al de C++ pq no te deja tener un puntero al current.
    // ver mejor forma de hacer el anillo de jugadores
    static final class PlayerRing extends AbstractCollection<Player> {

        private static final class Node {
            final Player player;
            Node next, prev;
            Node(Player p) { this.player = p; }
        }

        private Node current;
        private int  size = 0;

        public boolean add(Player p) {
            Node n = new Node(p);
            if (current == null) {
                n.next = n.prev = n;
                current = n;
            } else {
                Node tail = current.prev;
                tail.next = n;
                n.prev  = tail;
                n.next  = current;
                current.prev = n;
            }
            size++;
            return true;
        }

        Player curr() { return current.player; }
        Player next() { return current.next.player; }
        Player prev() { return current.prev.player; }
        void   forward()  { current = current.next; }
        void   backward() { current = current.prev; }
        public int    size()     { return size; }

        @Override public Iterator<Player> iterator() {
            Node start = current;
            return new Iterator<>() {
                private Node node = start;
                private int  visited = 0;
                @Override public boolean hasNext() { return visited < size; }
                @Override public Player next() {
                    Player out = node.player;
                    node = node.next;
                    visited++;
                    return out;
                }
            };
        }
    }
}