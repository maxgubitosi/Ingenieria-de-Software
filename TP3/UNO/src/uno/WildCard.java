package uno;

// TODO: Nose si esta bien esto ni en qué momento decidiría el color el jugador?
public abstract class WildCard extends Card {
    @Override
    public boolean isCompatible(Card topCard) { return true; }
}

class UndeployedWildCard extends WildCard {
    @Override
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