package unobackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import unobackend.model.Card;
import unobackend.model.JsonCard;
import unobackend.service.Dealer;
import unobackend.service.UnoService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnoServiceTest {

    @MockBean
    Dealer dealer;

    // nose si esta bien estse
    @InjectMocks
    UnoService service;

    @BeforeEach
    void setUp() {
        // Provide a deck large enough to deal hands without running out
        List<Card> dummyDeck = new java.util.ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dummyDeck.add(Mockito.mock(Card.class));
        }
        when(dealer.fullDeck()).thenReturn(dummyDeck);
    }
}
