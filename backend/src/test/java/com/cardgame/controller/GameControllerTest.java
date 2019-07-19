package com.cardgame.controller;

import com.cardgame.entity.Card;
import com.cardgame.entity.Player;
import java.util.Objects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
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
public class GameControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String root() {
        return "http://localhost:" + port + "/api/v1";
    }

    public GameControllerTest() {
    }
    
    @Test
    public void testAdd() {
        String url = String.format("%s/game", this.root());
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        Assert.notNull(response.getBody(), "Game ID was null");
    }
    
    @Test
    public void testDelete() {
        String url = String.format("%s/game", this.root());
        // First we create;
        ResponseEntity<String> creationResponse = restTemplate.postForEntity(url, null, String.class);
        String gameId = creationResponse.getBody();
        
        // Now we delete the newly created;
        url = String.format("%s/%s", url, gameId);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
    }
    
    @Test
    public void testAddPlayersWithNew() {
        // First, create game
        String gameUrl = String.format("%s/game", this.root());
        ResponseEntity<String> gameResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameResponse.getBody();
        
        String playerName = "Player New";
        
        // Add to game
        String url = String.format("%s/game/%s/%s", this.root(), gameId, playerName);
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        Assert.notNull(response.getBody(), "Player ID null");
    }
    
    @Test
    public void testAddPlayersWithExisting() {
        // First, create game
        String gameUrl = String.format("%s/game", this.root());
        ResponseEntity<String> gameResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameResponse.getBody();
        
        // Then, create player
        Player player = new Player();
        player.setName("Player 1");
        
        String playerUrl = String.format("%s/player", this.root());
        ResponseEntity<String> playerResponse = restTemplate.postForEntity(playerUrl, player, String.class);
        String playerId = playerResponse.getBody();
        
        // Add to game
        String url = String.format("%s/game/%s/%s", this.root(), gameId, playerId);
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        Assert.notNull(response.getBody(), "Player ID null");
        Assert.isTrue(Objects.equals(response.getBody(), playerId), "The player ID returned is not equal to the sent");
    }
    
    @Test
    public void testRemovePlayer() {
        // First, create game
        String gameUrl = String.format("%s/game", this.root());
        ResponseEntity<String> gameResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameResponse.getBody();
        
        // Then, create player
        Player player = new Player();
        player.setName("Player 1");
        
        String playerUrl = String.format("%s/player", this.root());
        ResponseEntity<String> playerResponse = restTemplate.postForEntity(playerUrl, player, String.class);
        String playerId = playerResponse.getBody();
        
        // Add to game
        String url = String.format("%s/game/%s/%s", this.root(), gameId, playerId);
        restTemplate.postForEntity(url, null, String.class);
        
        // Remove from game
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
    }
    
    @Test
    public void testDeal() {
        // First, create game
        String gameUrl = String.format("%s/game", this.root());
        ResponseEntity<String> gameResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameResponse.getBody();
        
        // Then, create player
        Player player = new Player();
        player.setName("Player 1");
        
        String playerUrl = String.format("%s/player", this.root());
        ResponseEntity<String> playerResponse = restTemplate.postForEntity(playerUrl, player, String.class);
        String playerId = playerResponse.getBody();
        
        // Add player to game
        String addGameUrl = String.format("%s/game/%s/%s", this.root(), gameId, playerId);
        restTemplate.postForEntity(addGameUrl, null, String.class);
        
        // Add a deck to the game
        String deckUrl = String.format("%s/deck/%s", this.root(), gameId);
        restTemplate.postForEntity(deckUrl, null, String.class);
        
        // Deal
        String url = String.format("%s/game/deal/%s/%s", this.root(), gameId, playerId);
        ResponseEntity<Card> response = restTemplate.postForEntity(url, null, Card.class);
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        Assert.notNull(response.getBody(), "Null card");
    }
    
    @Test
    public void dealEntireDeck() {
        // First, create game
        String gameUrl = String.format("%s/game", this.root());
        ResponseEntity<String> gameResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameResponse.getBody();
        
        // Then, create player
        Player player = new Player();
        player.setName("Player 1");
        
        String playerUrl = String.format("%s/player", this.root());
        ResponseEntity<String> playerResponse = restTemplate.postForEntity(playerUrl, player, String.class);
        String playerId = playerResponse.getBody();
        
        // Add player to game
        String addGameUrl = String.format("%s/game/%s/%s", this.root(), gameId, playerId);
        restTemplate.postForEntity(addGameUrl, null, String.class);
        
        // Add a deck to the game
        String deckUrl = String.format("%s/deck/%s", this.root(), gameId);
        restTemplate.postForEntity(deckUrl, null, String.class);
        
        // Deal
        String url = String.format("%s/game/deal/%s/%s", this.root(), gameId, playerId);
        ResponseEntity<Card> response = restTemplate.postForEntity(url, null, Card.class);
        
        int count = 0;
        
        while (Objects.nonNull(response.getBody())) {
            count++;
            response = restTemplate.postForEntity(url, null, Card.class);            
        }
        
        Assert.isTrue(count == 52, "Deck did not have 52 cards");
    }
    
    @Test
    public void testShuffle() {
        // First, create game
        String gameUrl = String.format("%s/game", this.root());
        ResponseEntity<String> gameResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameResponse.getBody();
        
        // Add a deck to the game
        String deckUrl = String.format("%s/deck/%s", this.root(), gameId);
        restTemplate.postForEntity(deckUrl, null, String.class);
        
        // Peek
        String peekUrl = String.format("%s/game/peek/%s", this.root(), gameId);
        ResponseEntity<String> peekResponse = restTemplate.getForEntity(peekUrl, String.class);
        
        String before = peekResponse.getBody();
        
        // Shuffle
        String shuffleUrl = String.format("%s/game/shuffle/%s", this.root(), gameId);
        ResponseEntity<String> response = restTemplate.postForEntity(shuffleUrl, null, String.class);
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        
        peekResponse = restTemplate.getForEntity(peekUrl, String.class);
        String after = peekResponse.getBody();
        
        Assert.isTrue(!Objects.equals(after, before), "Before peek is the same as the after peek");
    }
}