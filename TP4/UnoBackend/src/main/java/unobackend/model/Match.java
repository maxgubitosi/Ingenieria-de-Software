package unobackend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Match {
    public static String NotACardInHand = "Not a card in hand of ";
    public static String CardDoNotMatch = "Card does not match Color, Number or Kind";
    public static String NotEnoughPlayers = "Not enough players to create game";
    public static String EmptyOrNullPlayers = "Players must be valid strings";
    private Function<GameStatus, GameStatus> reverseShift;
    private Function<GameStatus, GameStatus> nextShift;
    private GameStatus status;
    private Card discardPileHead;
    private List<Card> drawPile;

    public static Match newReducedMatch( List<Card> deck, String ... players ) {
        return new Match( new ArrayList( deck ), 5, List.of( players ) );
    }
    public static Match fullMatch( List<Card> deck, List<String> players ) {
        return new Match( new ArrayList( deck ), 7, players );
    }

    public Match( List<Card> deck, int cardsInHand, List<String> players ) {

        // TODO: crear test de estos dos ifs a nivel del model. Simplemente con que no te deje crear un juego con menos de 2 o nombres no strings ta bien.
        if ( players == null || players.size() <= 1 ) {
            throw new IllegalArgumentException( NotEnoughPlayers ); }

        for ( String player : players ) {
            if (player == null || player.trim().isEmpty()) {
                throw new IllegalArgumentException( EmptyOrNullPlayers );}
        }

        discardPileHead = deck.remove( 0 );
        nextShift = (status) -> status.right();
        reverseShift = (status) -> status.left();

        Playing first = new Playing( players.getFirst(), new ArrayList( deck.subList( 0, cardsInHand ) ) );
        deck.subList( 0, cardsInHand ).clear();

        Playing st = first;
        for ( String player: players.subList( 1, players.size() ) ) {
            st = new Playing( st, player, new ArrayList( deck.subList( 0, cardsInHand ) ) );
            deck.subList( 0, cardsInHand ).clear();
        }
        first.linkTo( st );
        drawPile = deck;
        status = first;
    }

    public void drawCard( String player ) {
        status.assertTurnOf( player );
        draw();
    }

    public void play( String playerName, Card aCard ) {
        status.assertTurnOf( playerName );
        Player player = status.player();

        if ( !player.hasCard( aCard ) ) {
            throw new RuntimeException( NotACardInHand + playerName );
        }

        if ( !discardPileHead.acceptsOnTop( aCard ) ) {
            throw new RuntimeException( CardDoNotMatch );
        }

        player.removeCard( aCard );
        discardPileHead = aCard;

        if ( player.oneCardLeft() && !aCard.unoShouted() ) {
            draw();
            draw();
        }

        // TODO: No chequea que le asignes color a una WildCard al jugarla??!!
        // La verdad que no debe ser muy importante arreglar esto

        if ( player.noCardLeft() ) {
            status = new GameOver( player );
        } else {
            aCard.affectGame( this );
        }

    }

    public Card activeCard() {
        return discardPileHead;
    }

    public List<Card> playerHand() {
        return status.player().hand();
    }

    public boolean acceptsColor( String aColor ) {
        return discardPileHead.yourColorIs( aColor );
    }

    public void reverseShifturn() {
        Function<GameStatus, GameStatus> temp = nextShift;
        nextShift = reverseShift;
        reverseShift = temp;
    }

    public void shiftTurn() {
        status = nextShift.apply( status );
    }

    public void draw() {
        status.player().drawCard( drawPile.remove( 0 ) );
    }

    public boolean isOver() {
        return status.isOver();
    }
}
