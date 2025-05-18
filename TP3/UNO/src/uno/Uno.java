package uno;

import java.util.*;

/**
 * Lógica principal de la partida UNO.
 */
public class Uno {
    private final List<Player> players = new ArrayList<>();
    private final Deque<Card> drawPile = new ArrayDeque<>();
    private final Deque<Card> discardPile = new ArrayDeque<>();
    private Card topCard;
    private int currentPlayerIndex;
    private int direction = 1;

    private Uno() {}

    /**
     * Crea una partida con los nombres de jugadores dados.
     */
    public static Uno withPlayers(String... names) {
        Uno game = new Uno();
        for (String name : names) {
            game.players.add(new Player(name));
        }
        game.initializeDrawPile();
        return game;
    }

    private void initializeDrawPile() {
        // Para simplicidad, llenamos el mazo con cards numéricas rojas de valor 0
        for (int i = 0; i < 100; i++) {
            drawPile.addLast(new NumberCard(Color.RED, 0));
        }
    }

    /**
     * Define la carta inicial en cima del mazo de descarte.
     */
    public Uno setTopCard(Card card) {
        this.topCard = card;
        discardPile.push(card);
        return this;
    }

    /**
     * Inicia la partida repartiendo 7 cartas a cada jugador.
     */
    public void start() {
        currentPlayerIndex = 0;
        for (Player p : players) {
            for (int i = 0; i < 7; i++) {
                p.getHand().add(drawPile.pop());
            }
        }
    }

    /**
     * Recupera un jugador por su nombre.
     */
    public Player getPlayer(String name) {
        return players.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No existe jugador: " + name));
    }

    /**
     * Juega una carta, aplica su efecto y avanza el turno.
     */
    public void playCard(String playerName, Card card) {
        // 1) Verifico primero si es jugada válida
        if (topCard != null && !card.canPlayOn(topCard)) {
            throw new InvalidMoveException("Movimiento inválido");
        }

        // 2) Sólo elimino la carta de la mano si realmente existía
        Player p = getPlayer(playerName);
        if (p.getHand().contains(card)) {
            p.getHand().remove(card);
        }

        // 3) Aplico la jugada
        topCard = card;
        discardPile.push(card);
        card.applyEffect(this);

        // 4) Avanzo el turno
        advanceTurn();
    }


    private void advanceTurn() {
        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
    }

    /**
     * El siguiente jugador pierde turno.
     */
    public void skipNextPlayer() {
        advanceTurn();
    }

    /**
     * Invierte el sentido de juego.
     */
    public void reverseDirection() {
        direction *= -1;
    }

    /**
     * El siguiente jugador roba `count` cartas y pierde turno.
     */
    public void drawCardsForNextPlayer(int count) {
        int nextIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        Player next = players.get(nextIndex);
        for (int i = 0; i < count; i++) {
            next.getHand().add(drawPile.pop());
        }
        skipNextPlayer();
    }

    /**
     * Devuelve la carta en cima del descarte.
     */
    public Card getTopCard() {
        return topCard;
    }

    /**
     * Número de jugadores en la partida.
     */
    public int getPlayersCount() {
        return players.size();
    }
}
