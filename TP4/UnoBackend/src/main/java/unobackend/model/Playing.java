package unobackend.model;

import java.util.List;

public class Playing extends GameStatus {
    private Playing left;
    private Playing right;

    public Playing( String aName, List<Card> aHand ) {
        super( new Player( aName, aHand ) );
    }

    public Playing( Playing aPlayer, String aName, List<Card> aHand ) {
        this( aName, aHand );
        linkTo( aPlayer );
    }
    public void linkTo( Playing aPlayer ) {
        left = aPlayer;
        aPlayer.right = this;
    }

    public GameStatus right() {     return right; }
    public GameStatus left() {      return left;  }

    public void assertTurnOf( String playerName ) {
        player.assertTurnOf( playerName );
    }

    public boolean isOver() {
        return false;
    }
}
