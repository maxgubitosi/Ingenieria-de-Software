package uno;

public class SkipCard extends Card {
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
    public Card applyEffect(Uno game) {
        game.skipNextPlayer();
        return this;}
}
