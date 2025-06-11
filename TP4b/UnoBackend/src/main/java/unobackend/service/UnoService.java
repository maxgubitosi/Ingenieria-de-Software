package unobackend.service;

import org.apache.logging.log4j.simple.internal.SimpleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import unobackend.model.Card;
import unobackend.model.Match;

@Service
public class UnoService {

    @Autowired Dealer dealer;
    private Map<UUID, Match> sessions = new HashMap<UUID, Match>();

    public UUID newMatch(List<String> players) {
        UUID newKey = UUID.randomUUID();
        sessions.put( newKey, Match.fullMatch( dealer.fullDeck(), players));
        return newKey;
    }

    public List<Card> playerHand(UUID matchId) {
        return sessions.get( matchId ).playerHand();
    }
}
