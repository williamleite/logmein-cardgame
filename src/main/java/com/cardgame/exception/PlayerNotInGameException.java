package com.cardgame.exception;

import java.util.UUID;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public class PlayerNotInGameException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    @Getter private final UUID playerId;
    @Getter private final UUID gameId;
    
    public PlayerNotInGameException(final UUID playerId, final UUID gameId) {
        super(String.format("The supplied Player [%s] is not in the supplied Game [%s]", playerId, gameId));
        this.playerId = playerId;
        this.gameId = gameId;
    }
}
