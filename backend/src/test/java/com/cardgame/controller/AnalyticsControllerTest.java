package com.cardgame.controller;

import com.cardgame.entity.Card;
import com.cardgame.entity.Player;
import com.cardgame.vo.CardVO;
import com.cardgame.vo.PlayersTotal;
import com.cardgame.vo.RemainingCard;
import com.cardgame.vo.SuitsTotal;
import java.util.List;
import java.util.Objects;
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
public class AnalyticsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String root() {
        return "http://localhost:" + port + "/api/v1";
    }

    public AnalyticsControllerTest() {
    }
    
    @Test
    public void testPlayersCards() {
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
        String dealUrl = String.format("%s/game/deal/%s/%s", this.root(), gameId, playerId);
        ResponseEntity<CardVO> dealResponse = restTemplate.postForEntity(dealUrl, null, CardVO.class);
        CardVO dealtCard = dealResponse.getBody();
        
        // Get list
        String url = String.format("%s/analytics/cards/%s/%s", this.root(), gameId, playerId);
        ResponseEntity<List<CardVO>> response = restTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<List<CardVO>>(){});
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        Assert.notNull(response.getBody(), "Null card list");
        Assert.isTrue(response.getBody().size() == 1, "Cards in list differs from cards dealt");
        Assert.isTrue(response.getBody().stream().anyMatch(c -> Objects.equals(c.getId(), dealtCard.getId())), "Card dealt not in list");
    }
    
    @Test
    public void testPlayersTotal() {
        // First, create game
        String gameUrl = String.format("%s/game", this.root());
        ResponseEntity<String> gameResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameResponse.getBody();
        
        // Then, create players
        Player player = new Player();
        player.setName("Player 1");
        
        String playerUrl = String.format("%s/player", this.root());
        ResponseEntity<String> playerResponse = restTemplate.postForEntity(playerUrl, player, String.class);
        String player1Id = playerResponse.getBody();
        
        player = new Player();
        player.setName("Player 2");
        
        playerResponse = restTemplate.postForEntity(playerUrl, player, String.class);
        String player2Id = playerResponse.getBody();
        
        // Add players to game
        String addGameUrl = String.format("%s/game/%s/%s", this.root(), gameId, player1Id);
        restTemplate.postForEntity(addGameUrl, null, String.class);
        
        addGameUrl = String.format("%s/game/%s/%s", this.root(), gameId, player2Id);
        restTemplate.postForEntity(addGameUrl, null, String.class);
        
        // Add a deck to the game
        String deckUrl = String.format("%s/deck/%s", this.root(), gameId);
        restTemplate.postForEntity(deckUrl, null, String.class);
        
        // Deal
        String dealUrl = String.format("%s/game/deal/%s/%s", this.root(), gameId, player1Id);
        ResponseEntity<CardVO> dealResponse = restTemplate.postForEntity(dealUrl, null, CardVO.class);
        CardVO dealtCardPlayer1 = dealResponse.getBody();
        
        dealUrl = String.format("%s/game/deal/%s/%s", this.root(), gameId, player2Id);
        dealResponse = restTemplate.postForEntity(dealUrl, null, CardVO.class);
        CardVO dealtCardPlayer2 = dealResponse.getBody();
        
        // Get total
        String url = String.format("%s/analytics/players/%s", this.root(), gameId);
        ResponseEntity<List<PlayersTotal>> response = restTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<List<PlayersTotal>>(){});
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        
        if (dealtCardPlayer1.getFace().getValue() > dealtCardPlayer2.getFace().getValue()) {
            Assert.isTrue(Objects.equals(response.getBody().get(0).getTotal(), dealtCardPlayer1.getFace().getValue()), "Total not equal to dealt card");
            Assert.isTrue(Objects.equals(response.getBody().get(1).getTotal(), dealtCardPlayer2.getFace().getValue()), "Total not equal to dealt card");
        } else {
            Assert.isTrue(Objects.equals(response.getBody().get(0).getTotal(), dealtCardPlayer2.getFace().getValue()), "Total not equal to dealt card");
            Assert.isTrue(Objects.equals(response.getBody().get(1).getTotal(), dealtCardPlayer1.getFace().getValue()), "Total not equal to dealt card");
        }        
    }
    
    @Test
    public void testSuitsTotal() {
        // First, create game
        String gameUrl = String.format("%s/game", this.root());
        ResponseEntity<String> gameResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameResponse.getBody();
        
        // Then, create players
        Player player = new Player();
        player.setName("Player 1");
        
        String playerUrl = String.format("%s/player", this.root());
        ResponseEntity<String> playerResponse = restTemplate.postForEntity(playerUrl, player, String.class);
        String player1Id = playerResponse.getBody();
        
        // Add players to game
        String addGameUrl = String.format("%s/game/%s/%s", this.root(), gameId, player1Id);
        restTemplate.postForEntity(addGameUrl, null, String.class);
        
        // Add a deck to the game
        String deckUrl = String.format("%s/deck/%s", this.root(), gameId);
        restTemplate.postForEntity(deckUrl, null, String.class);
        
        // Deal
        String dealUrl = String.format("%s/game/deal/%s/%s", this.root(), gameId, player1Id);
        ResponseEntity<CardVO> dealResponse = restTemplate.postForEntity(dealUrl, null, CardVO.class);
        CardVO dealtCardPlayer1 = dealResponse.getBody();
        
        // Get total
        String url = String.format("%s/analytics/suits/%s", this.root(), gameId);
        ResponseEntity<List<SuitsTotal>> response = restTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<List<SuitsTotal>>(){});
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        
        SuitsTotal suitCount = response.getBody().stream().filter(s -> s.getSuit() == dealtCardPlayer1.getSuit()).findFirst().orElse(null);
        Assert.notNull(suitCount, "Suit doesn't exist on count");
        Assert.isTrue(suitCount.getTotal() == 12L, "Suit count is incorrect");
    }
    
    @Test
    public void testRemainingCards() {
        // First, create game
        String gameUrl = String.format("%s/game", this.root());
        ResponseEntity<String> gameResponse = restTemplate.postForEntity(gameUrl, null, String.class);
        String gameId = gameResponse.getBody();
        
        // Then, create players
        Player player = new Player();
        player.setName("Player 1");
        
        String playerUrl = String.format("%s/player", this.root());
        ResponseEntity<String> playerResponse = restTemplate.postForEntity(playerUrl, player, String.class);
        String player1Id = playerResponse.getBody();
        
        // Add players to game
        String addGameUrl = String.format("%s/game/%s/%s", this.root(), gameId, player1Id);
        restTemplate.postForEntity(addGameUrl, null, String.class);
        
        // Add a deck to the game
        String deckUrl = String.format("%s/deck/%s", this.root(), gameId);
        restTemplate.postForEntity(deckUrl, null, String.class);
        
        // Deal
        String dealUrl = String.format("%s/game/deal/%s/%s", this.root(), gameId, player1Id);
        restTemplate.postForEntity(dealUrl, null, CardVO.class);
        
        // Remaining Cards
        String url = String.format("%s/analytics/remaining/%s", this.root(), gameId);
        ResponseEntity<List<RemainingCard>> response = restTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<List<RemainingCard>>(){});
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        Assert.notNull(response.getBody(), "Remaining list is null");
        Assert.isTrue(response.getBody().size() == 51, "Wrong card count");
    }
}
