package com.cardgame.controller;

import com.cardgame.entity.Player;
import com.cardgame.service.PlayerService;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author William Leite
 */
@Controller
@RequestMapping(path = "api/v1/player")
public class PlayerController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private PlayerService service;
    
    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity<String> add(final @RequestBody Player player) {
        final UUID result = this.service.add(player);
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
}
