package com.cardgame.controller;

import com.cardgame.entity.Player;
import com.cardgame.service.PlayerService;
import java.io.Serializable;
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
        String result = null;
        HttpStatus status;
        try {
            result = this.service.add(player).toString();
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
}
