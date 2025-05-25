package uno;

public class Draw2Card extends Card {
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
    public Card applyEffect(Uno game) {
        game.drawCardsForNextPlayer(2);
        game.skipNextPlayer();
        return this;
    }
}
