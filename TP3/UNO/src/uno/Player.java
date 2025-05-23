package uno;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();
    private Player next;
    private Player prev;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setNext(Player next) {
        this.next = next;
    }

    public Player getNext() {
        return next;
    }

    public void setPrev(Player prev) {
        this.prev = prev;
    }

    public Player getPrev() {
        return prev;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void playCard(Card card) {
        if (!hand.remove(card)) {
            throw new RuntimeException("Player " + name + " does not have the specified card");
        }
    }
}