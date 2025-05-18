package uno;

/**
 * Clase abstracta para todas las cartas de UNO.
 * Contiene la definición de los métodos comunes y las subclases concretas.
 */
public abstract class Card {
    /**
     * Determina si esta carta puede jugarse sobre la carta en cima.
     */
    public abstract boolean canPlayOn(Card topCard);

    /**
     * Aplica el efecto de la carta sobre la partida (sin efecto por defecto).
     */
    public void applyEffect(Uno game) {
        // Sin efecto por defecto
    }
}

/**
 * Carta numérica (0–9) de un color específico.
 */
class NumberCard extends Card {
    private final Color color;
    private final int number;

    public NumberCard(Color color, int number) {
        this.color = color;
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean canPlayOn(Card topCard) {
        if (topCard instanceof NumberCard) {
            NumberCard other = (NumberCard) topCard;
            return color == other.color || number == other.number;
        } else if (topCard instanceof SpecialColorCard) {
            return color == ((SpecialColorCard) topCard).getColor();
        } else if (topCard instanceof WildCard) {
            return color == ((WildCard) topCard).getChosenColor();
        }
        return false;
    }
}

/**
 * Clase base para cartas de acción con color (DrawTwoCard, SkipCard, ReverseCard).
 */
abstract class SpecialColorCard extends Card {
    private final Color color;

    public SpecialColorCard(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean canPlayOn(Card topCard) {
        if (topCard instanceof NumberCard) {
            // Permitir jugar sobre número del mismo color
            return this.color == ((NumberCard) topCard).getColor();
        }
        else if (topCard instanceof SpecialColorCard) {
            return this.color == ((SpecialColorCard) topCard).getColor();
        }
        else if (topCard instanceof WildCard) {
            return this.color == ((WildCard) topCard).getChosenColor();
        }
        return false;
    }
}

/**
 * Carta "+2": el siguiente jugador roba 2 y pierde turno.
 */
class DrawTwoCard extends SpecialColorCard {
    public DrawTwoCard(Color color) {
        super(color);
    }

    @Override
    public void applyEffect(Uno game) {
        game.drawCardsForNextPlayer(2);
    }
}

/**
 * Carta "Skip": el siguiente jugador pierde turno.
 */
class SkipCard extends SpecialColorCard {
    public SkipCard(Color color) {
        super(color);
    }

    @Override
    public void applyEffect(Uno game) {
        game.skipNextPlayer();
    }
}

/**
 * Carta "Reverse": invierte el sentido de juego (en 2 jugadores actúa como Skip).
 */
class ReverseCard extends SpecialColorCard {
    public ReverseCard(Color color) {
        super(color);
    }

    @Override
    public void applyEffect(Uno game) {
        game.reverseDirection();
        if (game.getPlayersCount() == 2) {
            game.skipNextPlayer();
        }
    }
}

/**
 * Carta "Wild": cambia al color elegido al jugar.
 */
class WildCard extends Card {
    private final Color chosenColor;

    public WildCard(Color chosenColor) {
        this.chosenColor = chosenColor;
    }

    public Color getChosenColor() {
        return chosenColor;
    }

    @Override
    public boolean canPlayOn(Card topCard) {
        return true;
    }

    @Override
    public void applyEffect(Uno game) {
        // El color ya fue definido en el constructor
    }
}