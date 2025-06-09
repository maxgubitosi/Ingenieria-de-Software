package uno;

public class NumericCard extends Card {
    private final Color color;
    private final int number;

    public NumericCard(Color color, int number) {
        this.color = color;
        this.number = number;
    }

    @Override
    protected boolean numberCompatible(Card topCard) {
        return topCard.numberCompatibleWithNumeric(this);
    }
    @Override
    protected boolean numberCompatibleWithNumeric(NumericCard other) {
        return this.number == other.number;
    }

    @Override
    protected boolean colorCompatible(Card topCard) {
        return color == topCard.getColor();
    }

    @Override
    public Color getColor() { return color; }
}
