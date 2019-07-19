package com.cardgame.controller;

import com.cardgame.entity.Card;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 *
 * @author William Leite
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeckControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String root() {
        return "http://localhost:" + port + "/api/v1";
    }
    
    public DeckControllerTest() {
    }
    
    @Test
    public void testCreate() {
        String url = String.format("%s/deck", this.root());
        ResponseEntity<List<Card>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Card>>(){});
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        
        List<Card> cards = response.getBody();
        Assert.notNull(cards, "Null list of cards");
        Assert.isTrue(cards.size() == 52, "Does not have exactly 52 cards");
    }
    
    @Test
    public void testAdd() {
        String gameUrl = String.format("%s/game", this.root());
        
        // Creates the game
        ResponseEntity<String> gameCreateResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameCreateResponse.getBody();
        
        // Add a deck to the game
        String deckUrl = String.format("%s/deck/%s", this.root(), gameId);
        ResponseEntity<String> deckResponse = restTemplate.postForEntity(deckUrl, null, String.class);
        
        Assert.notNull(deckResponse, "Endpoint did not return");
        Assert.isTrue(deckResponse.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", deckResponse.getStatusCodeValue()));
    }
}
