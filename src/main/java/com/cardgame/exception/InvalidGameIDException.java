package com.cardgame.exception;

import java.util.UUID;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public class InvalidGameIDException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    @Getter private final UUID gameId;
    
    public InvalidGameIDException(final UUID gameId) {
        super(String.format("The supplied Game ID [%s] is invalid!", gameId.toString()));
        this.gameId = gameId;
    }
}
