package uno;

public abstract class WildCard extends Card {
    @Override
    public boolean isCompatible(Card topCard) { return true; }

    @Override
    // para que funcione el hand.remove(card) en Player.playCard() consideramos equivalente un deployed y undeployed wildcard
    // el jugador nunca tendrá en su mano un deployed, pero un jugador solo puede jugar un deployed
    // entonces al jugar un deployed buscamos remover de su mano un undeployed
    public boolean equals(Object o) { return o instanceof WildCard; }
}

class UndeployedWildCard extends WildCard {
    @Override
    public DeployedWildCard colorize(Color chosenColor) {
        return new DeployedWildCard(chosenColor);
    }
    @Override
    public boolean isCompatible(Card topCard) { // undeployed no se puede jugar. Debe ser colorizada.
        throw new InvalidMoveException("Se debe elegir el color de la wildcard antes de jugarla");
    }
}

class DeployedWildCard extends WildCard {
    private final Color chosenColor;
    public DeployedWildCard(Color chosenColor) { this.chosenColor = chosenColor; }
    @Override
    public Color getColor() { return chosenColor; }
}