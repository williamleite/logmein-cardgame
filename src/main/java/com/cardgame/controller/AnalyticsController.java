package com.cardgame.controller;

import com.cardgame.entity.Card;
import com.cardgame.exception.EmptyGameDeckException;
import com.cardgame.exception.EmptyGamePlayersException;
import com.cardgame.exception.InvalidGameIDException;
import com.cardgame.service.AnalyticsService;
import com.cardgame.vo.PlayersTotal;
import com.cardgame.vo.RemainingCard;
import com.cardgame.vo.SuitsTotal;
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
@RequestMapping(path = "api/v1/analytics")
public class AnalyticsController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private AnalyticsService service;
    
    @RequestMapping(path = "cards/{game_id}/{player_id}", method = RequestMethod.GET)
    public ResponseEntity<List<Card>> cards(final @PathVariable("game_id") UUID gameId, final @PathVariable("player_id") UUID playerId) {
        List<Card> result = null;
        HttpStatus status;
        try {
            result = this.service.cards(gameId, playerId);
            status = HttpStatus.OK;
        } catch (InvalidGameIDException ex) {
            ex.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        } catch (EmptyGameDeckException ex) {
            ex.printStackTrace();
            status = HttpStatus.PRECONDITION_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }
    
    @RequestMapping(path = "players/{game_id}", method = RequestMethod.GET)
    public ResponseEntity<List<PlayersTotal>> playersTotal(final @PathVariable("game_id") UUID gameId) {
        List<PlayersTotal> result = null;
        HttpStatus status;
        try {
            result = this.service.playersTotal(gameId);
            status = HttpStatus.OK;
        } catch (InvalidGameIDException ex) {
            ex.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        } catch (EmptyGamePlayersException ex) {
            ex.printStackTrace();
            status = HttpStatus.PRECONDITION_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }
    
    @RequestMapping(path = "suits/{game_id}", method=RequestMethod.GET)
    public ResponseEntity<List<SuitsTotal>> suitsTotal(final @PathVariable("game_id") UUID gameId) {
        List<SuitsTotal> result = null;
        HttpStatus status;
        try {
            result = this.service.suitsTotal(gameId);
            status = HttpStatus.OK;
        } catch (InvalidGameIDException ex) {
            ex.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        } catch (EmptyGameDeckException ex) {
            ex.printStackTrace();
            status = HttpStatus.PRECONDITION_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }
    
    @RequestMapping(path = "remaining/{game_id}", method = RequestMethod.GET)
    public ResponseEntity<List<RemainingCard>> remainingCards(final @PathVariable("game_id") UUID gameId) {
        List<RemainingCard> result = null;
        HttpStatus status;
        try {
            result = this.service.remainingCards(gameId);
            status = HttpStatus.OK;
        } catch (InvalidGameIDException ex) {
            ex.printStackTrace();
            status = HttpStatus.BAD_REQUEST;
        } catch (EmptyGameDeckException ex) {
            ex.printStackTrace();
            status = HttpStatus.PRECONDITION_FAILED;
        } catch (Exception e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(result, status);
    }
}
