package uno;

public abstract class Card {

    public boolean isCompatible(Card topCard) {
        return numberCompatible(topCard) ||
               colorCompatible(topCard) ||
               typeCompatible(topCard);
    }

    protected boolean numberCompatible(Card topCard) { return false; }
    protected boolean colorCompatible(Card topCard) { return false; }
    protected boolean typeCompatible(Card topCard) { return false; }

    public void applyEffect(Uno game) { }

    public Color getColor() { return null; }
}

class NumericCard extends Card {
    private final Color color;
    private final int number;

    public NumericCard(Color color, int number) {
        this.color = color;
        this.number = number;
    }

    @Override
    protected boolean numberCompatible(Card topCard) {
        if (topCard instanceof NumericCard) {
            return number == ((NumericCard) topCard).number;
        }
        return false;
    }

    @Override
    protected boolean colorCompatible(Card topCard) {
        return color == topCard.getColor();
    }

    @Override
    public Color getColor() { return color; }
}

class Draw2Card extends Card {
    private final Color color;

    public Draw2Card(Color color) { this.color = color; }

    @Override
    protected boolean colorCompatible(Card topCard) {
        return color == topCard.getColor();
    }

    @Override
    protected boolean typeCompatible(Card topCard) {
        return topCard instanceof Draw2Card;
    }

    @Override
    public Color getColor() { return color; }

    @Override
    public void applyEffect(Uno game) { game.drawCardsForNextPlayer(2); }
}

class SkipCard extends Card {
    private final Color color;

    public SkipCard(Color color) { this.color = color; }

    @Override
    protected boolean colorCompatible(Card topCard) {
        return color == topCard.getColor();
    }

    @Override
    protected boolean typeCompatible(Card topCard) {
        return topCard instanceof SkipCard;
    }

    @Override
    public Color getColor() { return color; }

    @Override
    public void applyEffect(Uno game) { game.skipNextPlayer(); }
}

class ReverseCard extends Card {
    private final Color color;

    public ReverseCard(Color color) { this.color = color; }

    @Override
    protected boolean colorCompatible(Card topCard) {
        return color == topCard.getColor();
    }

    @Override
    protected boolean typeCompatible(Card topCard) {
        return topCard instanceof ReverseCard;
    }

    @Override
    public Color getColor() { return color; }

    @Override
    public void applyEffect(Uno game) {
        game.reverseController();
        if (game.getPlayersCount() == 2) {
            game.skipNextPlayer();
        }
    }
}

abstract class WildCard extends Card {
    @Override
    public boolean isCompatible(Card topCard) { return true; }
}

class UndeployedWildCard extends WildCard {
    public DeployedWildCard deploy(Color chosenColor) {
        return new DeployedWildCard(chosenColor);
    }
}

class DeployedWildCard extends WildCard {
    private final Color chosenColor;
    public DeployedWildCard(Color chosenColor) { this.chosenColor = chosenColor; }
    @Override
    public Color getColor() { return chosenColor; }
}

class NumberCard extends NumericCard {
    public NumberCard(Color color, int number) { super(color, number); }
}

class DrawTwoCard extends Draw2Card {
    public DrawTwoCard(Color color) { super(color); }
}