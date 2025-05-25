package uno;

public final class UnoDeclaredCard extends Card {

    private final Card inner;

    UnoDeclaredCard(Card inner) { this.inner = inner; }

    @Override public boolean isCompatible(Card top)      { return inner.isCompatible(top); }
    @Override protected boolean numberCompatible(Card t) { return inner.numberCompatible(t);}
    @Override protected boolean colorCompatible(Card t)  { return inner.colorCompatible(t); }
    @Override protected boolean typeCompatible(Card t)   { return inner.typeCompatible(t); }
    @Override public Color getColor()                    { return inner.getColor(); }
    @Override public Card colorize(Color c)              { return inner.colorize(c); }
    @Override public Card applyEffect(Uno g)             { return inner.applyEffect(g); }

    @Override public boolean declaredUno() { return true; }
    @Override public Card     unwrap()     { return inner; }
}
