package unobackend.service;

import org.springframework.stereotype.Component;
import unobackend.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Dealer {

    public List<Card> fullDeck() {
        ArrayList<Card> fullDeck = new ArrayList<>();

        for (String color : List.of("Red", "Blue", "Green", "Yellow")) {
            fullDeck.addAll(allCardsColored(color));
        }
        fullDeck.addAll(List.of(new WildCard(),
                                new WildCard(),
                                new WildCard(),
                                new WildCard())
                        );

        Collections.shuffle(fullDeck);
        return fullDeck;
    }

    private List<Card> allCardsColored(String color) {

        List<Card> cards = new ArrayList<>();
        cards.add(new NumberCard(color, 0));

        for (int i = 1; i <= 9; i++) {
            cards.add(new NumberCard(color, i));
            cards.add(new NumberCard(color, i));
        }
        for (int i = 0; i < 2; i++) {
            cards.add(new Draw2Card(color));
            cards.add(new ReverseCard(color));
            cards.add(new SkipCard(color));
        }
        return cards;
    }
}
