package com.cardgame.controller;

import com.cardgame.entity.Card;
import com.cardgame.exception.InvalidGameIDException;
import com.cardgame.service.DeckService;
import java.io.Serializable;
import java.util.List;
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
@RequestMapping(path = "api/v1/deck")
public class DeckController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private DeckService service;

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<Card>> createDeck() {
        List<Card> result = null;
        HttpStatus status;
        try {
            result = this.service.createDeck();
            status = HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }

    @RequestMapping(path = "{game_id}", method = RequestMethod.POST)
    public ResponseEntity<String> addDeck(final @PathVariable("game_id") UUID gameId) {
        HttpStatus status;
        try {
            this.service.addDeck(gameId);
            status = HttpStatus.OK;
        } catch (InvalidGameIDException ex) {
            ex.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(null, status);
    }
}
