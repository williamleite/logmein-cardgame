package com.cardgame.controller;

import com.cardgame.entity.Card;
import com.cardgame.service.DeckService;
import java.io.Serializable;
import java.util.List;
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
@RequestMapping(path = "api/v1/deck")
public class DeckController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private DeckService service;
    
    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<List<Card>> createDeck() {
        final List<Card> result = this.service.createDeck();
        if (Objects.nonNull(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(path = "{game_id}", method = RequestMethod.POST)
    public ResponseEntity<String> addDeck(final @PathVariable("game_id") UUID gameId) {
        final Boolean result = this.service.addDeck(gameId);
        return new ResponseEntity<>(null, result ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}