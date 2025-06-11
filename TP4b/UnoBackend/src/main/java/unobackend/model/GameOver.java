package unobackend.model;

public class GameOver extends GameStatus {
    public static String GameOver = "GameOver";

    public GameOver( Player aPlayer ) {
        super( aPlayer );
    }

    public GameStatus right() { return null;    }
    public GameStatus left() {  return null;    }

    public void assertTurnOf( String player ) {
       throw new RuntimeException( GameOver );
    }

    public boolean isOver() {
        return true;
    }
}
