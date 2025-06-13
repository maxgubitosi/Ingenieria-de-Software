package unobackend.service;

import org.apache.logging.log4j.simple.internal.SimpleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import unobackend.model.Card;
import unobackend.model.JsonCard;
import unobackend.model.Match;

@Service
public class UnoService {

    @Autowired Dealer dealer;
    private final Map<UUID, Match> sessions = new HashMap<UUID, Match>();

    public UUID newMatch(List<String> players) {
        UUID newKey = UUID.randomUUID();
        sessions.put( newKey, Match.fullMatch( dealer.fullDeck(), players));
        return newKey;
    }

    public List<Card> playerHand(UUID matchId) {
        return sessions.get( matchId ).playerHand();
    }

    public void playerPlaysCard(UUID matchId, String player, JsonCard card) {
        sessions.get(matchId).play(player,card.asCard());
    }

    public Card activeCard(UUID matchId) {
        return sessions.get( matchId ).activeCard();
    }

    public void playerDrawsCard(UUID matchId, String player) {
        sessions.get(matchId).drawCard(player);
    }
}
