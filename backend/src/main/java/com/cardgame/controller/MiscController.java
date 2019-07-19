package com.cardgame.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Bill
 */
@Controller
public class MiscController {

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<String> alive() {
        return new ResponseEntity<>("Abandon all hope ye who enters here!", HttpStatus.OK);
    }
}
