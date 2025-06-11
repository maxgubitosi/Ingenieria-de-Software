package unobackend.service;

import org.springframework.stereotype.Component;
import unobackend.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Dealer {

    // genera el mazo para un nuevo match
    public List<Card> fullDeck() {
        ArrayList<Card> fullDeck = new ArrayList<>();

        fullDeck.addAll(cardsOn("Red"));
        fullDeck.addAll(cardsOn("Yellow"));
        fullDeck.addAll(cardsOn("Blue"));
        fullDeck.addAll(cardsOn("Green"));

        Collections.shuffle(fullDeck);
        return fullDeck;
    }

    private List<Card> cardsOn(String color) {
        return List.of( new WildCard(),
                        new SkipCard(color),
                        new Draw2Card(color),
                        new ReverseCard(color),
                        new NumberCard(color, 0),
                        new NumberCard(color, 1),
                        new NumberCard(color, 2),
                        new NumberCard(color, 3),
                        new NumberCard(color, 4),
                        new NumberCard(color, 5),
                        new NumberCard(color, 6),
                        new NumberCard(color, 7),
                        new NumberCard(color, 8),
                        new NumberCard(color, 9));
    }

}
