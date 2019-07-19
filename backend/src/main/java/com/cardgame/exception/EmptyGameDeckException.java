package com.cardgame.exception;

import java.util.UUID;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public class EmptyGameDeckException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    @Getter private final UUID gameId;
    
    public EmptyGameDeckException(final UUID gameId) {
        super(String.format("The supplied Game [%s] has an empty game deck", gameId.toString()));
        this.gameId = gameId;
    }
}
