package com.cardgame.controller;

import com.cardgame.entity.Card;
import com.cardgame.service.GameService;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author William Leite
 */
@Controller
@RequestMapping(path = "api/v1/game")
public class GameController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private GameService service;

    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<String> create() {
        UUID result = this.service.create();
        if (Objects.nonNull(result)) {
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(final @PathVariable("id") UUID id) {
        final Boolean result = this.service.delete(id);
        return new ResponseEntity<>(null, result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @RequestMapping(path = "{game_id}/{player_id_name}", method = RequestMethod.POST)
    public ResponseEntity<String> addPlayer(final @PathVariable("game_id") UUID gameId, final @PathVariable("player_id_name") String playerIdName) {
        UUID result = this.service.addPlayer(gameId, playerIdName);
        if (Objects.nonNull(result)) {
            return new ResponseEntity<>(result.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(path = "{game_id}/{player_id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> removePlayer(final @PathVariable("game_id") UUID gameId, final @PathVariable("player_id") UUID playerId) {
        final Boolean result = this.service.removePlayer(gameId, playerId);
        return new ResponseEntity<>(null, result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @RequestMapping(path = "deal/{game_id}/{player_id}", method = RequestMethod.POST)
    public ResponseEntity<Card> deal(final @PathVariable("game_id") UUID gameId, final @PathVariable("player_id") UUID playerId) {
        final Card result = this.service.deal(gameId, playerId);
        return new ResponseEntity<>(result, Objects.nonNull(result) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @RequestMapping(path = "shuffle/{game_id}", method = RequestMethod.POST)
    public ResponseEntity<String> shuffle(final @PathVariable("game_id") UUID gameId) {
        final Boolean result = this.service.shuffle(gameId);
        return new ResponseEntity<>(null, result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @RequestMapping(path = "peek/{game_id}", method=RequestMethod.GET)
    public ResponseEntity<String> peek(final @PathVariable("game_id") UUID gameId) {
        final String result = this.service.peek(gameId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}