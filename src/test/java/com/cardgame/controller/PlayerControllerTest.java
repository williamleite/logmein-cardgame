package com.cardgame.controller;

import com.cardgame.entity.Player;
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
public class PlayerControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String root() {
        return "http://localhost:" + port + "/api/v1";
    }

    public PlayerControllerTest() {
    }
    
    @Test
    public void testAdd() {
        String url = String.format("%s/player", this.root());
        
        Player player = new Player();
        player.setName("Player 1");
        
        ResponseEntity<String> response = restTemplate.postForEntity(url, player, String.class);
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
        Assert.notNull(response.getBody(), "Player ID was null");
    }
    
    @Test
    public void testDelete() {
        String url = String.format("%s/player", this.root());
        // First we create;
        Player player = new Player();
        player.setName("Player 1");
        
        ResponseEntity<String> creationResponse = restTemplate.postForEntity(url, player, String.class);
        String playerId = creationResponse.getBody();
        
        // Now we delete the newly created;
        url = String.format("%s/%s", url, playerId);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
        
        Assert.notNull(response, "Endpoint did not return");
        Assert.isTrue(response.getStatusCodeValue() == 200, String.format("Status code was not 200 [%s]", response.getStatusCodeValue()));
    }
}
