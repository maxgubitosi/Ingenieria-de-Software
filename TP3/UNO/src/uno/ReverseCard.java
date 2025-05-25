package uno;

public class ReverseCard extends Card {
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
    public Card applyEffect(Uno game) {
        game.reverseDirection();
        return this;
    }
}
