package com.cardgame.controller;

import com.cardgame.entity.Card;
import com.cardgame.exception.EmptyGameDeckException;
import com.cardgame.exception.EmptyGamePlayersException;
import com.cardgame.exception.InvalidGameIDException;
import com.cardgame.exception.InvalidPlayerIDException;
import com.cardgame.exception.PlayerNotInGameException;
import com.cardgame.service.GameService;
import java.io.Serializable;
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
        String result = null;
        HttpStatus status;
        try {
            result = this.service.create().toString();
            status = HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(final @PathVariable("id") UUID id) {
        HttpStatus status;
        try {
            this.service.delete(id);
            status = HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(null, status);
    }

    @RequestMapping(path = "{game_id}/{player_id_name}", method = RequestMethod.POST)
    public ResponseEntity<String> addPlayer(final @PathVariable("game_id") UUID gameId, final @PathVariable("player_id_name") String playerIdName) {
        String result = null;
        HttpStatus status;
        try {
            result = this.service.addPlayer(gameId, playerIdName).toString();
            status = HttpStatus.OK;
        } catch (InvalidGameIDException ex) {
            ex.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }

    @RequestMapping(path = "{game_id}/{player_id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> removePlayer(final @PathVariable("game_id") UUID gameId, final @PathVariable("player_id") UUID playerId) {
        HttpStatus status;
        try {
            this.service.removePlayer(gameId, playerId);
            status = HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(null, status);
    }

    @RequestMapping(path = "deal/{game_id}/{player_id}", method = RequestMethod.POST)
    public ResponseEntity<Card> deal(final @PathVariable("game_id") UUID gameId, final @PathVariable("player_id") UUID playerId) {
        Card result = null;
        HttpStatus status;
        try {
            result = this.service.deal(gameId, playerId);
            status = HttpStatus.OK;
        } catch (InvalidGameIDException | InvalidPlayerIDException ex) {
            ex.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        } catch (EmptyGameDeckException | EmptyGamePlayersException | PlayerNotInGameException ex) {
            ex.printStackTrace();
            status = HttpStatus.PRECONDITION_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }

    @RequestMapping(path = "shuffle/{game_id}", method = RequestMethod.POST)
    public ResponseEntity<String> shuffle(final @PathVariable("game_id") UUID gameId) {
        HttpStatus status;
        try {
            this.service.shuffle(gameId);
            status = HttpStatus.OK;
        } catch (EmptyGameDeckException ex) {
            ex.printStackTrace();
            status = HttpStatus.PRECONDITION_FAILED;
        } catch (InvalidGameIDException ex) {
            ex.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(null, status);
    }

    @RequestMapping(path = "peek/{game_id}", method = RequestMethod.GET)
    public ResponseEntity<String> peek(final @PathVariable("game_id") UUID gameId) {
        String result = null;
        HttpStatus status;
        try {
            result = this.service.peek(gameId);
            status = HttpStatus.OK;
        } catch (EmptyGameDeckException ex) {
            ex.printStackTrace();
            status = HttpStatus.PRECONDITION_FAILED;
        } catch (InvalidGameIDException ex) {
            ex.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }
}
