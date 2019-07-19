package com.cardgame.exception;

import java.util.UUID;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public class EmptyGamePlayersException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    @Getter public final UUID gameId;
    
    public EmptyGamePlayersException(final UUID gameId) {
        super(String.format("The supplied Game [%s] has an empty collection of Players", gameId.toString()));
        this.gameId = gameId;
    }
}