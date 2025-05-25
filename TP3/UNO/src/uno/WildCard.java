package uno;

public abstract class WildCard extends Card {
    @Override
    public boolean isCompatible(Card topCard) { return true; }

    @Override // Wildcards 
    public boolean equals(Object o) { return o instanceof WildCard; }
}

class UndeployedWildCard extends WildCard {
    @Override
    public DeployedWildCard colorize(Color chosenColor) {
        return new DeployedWildCard(chosenColor);
    }
    @Override
    public boolean isCompatible(Card topCard) {
        throw new InvalidMoveException("Se debe elegir el color de la wildcard antes de jugarla");
    }
}

class DeployedWildCard extends WildCard {
    private final Color chosenColor;
    public DeployedWildCard(Color chosenColor) { this.chosenColor = chosenColor; }
    @Override
    public Color getColor() { return chosenColor; }
}