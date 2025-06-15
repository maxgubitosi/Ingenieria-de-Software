package unobackend.model;

import java.util.List;

public abstract class GameStatus {
    protected Player player;

    public GameStatus( Player aPlayer ) {
        player = aPlayer;
    }

    public Player player() { return player; };

    public abstract GameStatus right();
    public abstract GameStatus left();
    public abstract void assertTurnOf( String player );
    public abstract boolean isOver() ;
}

