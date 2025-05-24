package uno;

public abstract class Card {

    public boolean isCompatible(Card topCard) {
        return   numberCompatible(topCard)
              || colorCompatible(topCard)
              || typeCompatible(topCard);
    }

    protected boolean numberCompatible(Card topCard) {
        return false;
    }
    protected boolean numberCompatibleWithNumeric(NumericCard other) {
        return false;
    }
    protected boolean colorCompatible(Card topCard) { return false; }
    protected boolean typeCompatible(Card topCard) { return false; }

    public Color getColor() {return null;}

    public Card deploy(Color chosenColor) {   // default: “nothing to deploy”
        return this;
    }

    public Card applyEffect(Uno game) {
        return this;
    }
}