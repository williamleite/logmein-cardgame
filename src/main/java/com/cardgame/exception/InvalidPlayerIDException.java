package com.cardgame.exception;

import java.util.UUID;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public class InvalidPlayerIDException extends Exception {

    private static final long serialVersionUID = 1L;

    @Getter private final UUID playerId;

    public InvalidPlayerIDException(final UUID playerId) {
        super(String.format("The supplied Player ID [%s] is invalid!", playerId.toString()));
        this.playerId = playerId;
    }
}