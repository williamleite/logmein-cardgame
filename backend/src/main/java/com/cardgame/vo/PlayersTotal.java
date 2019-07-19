package com.cardgame.vo;

import com.cardgame.entity.Player;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Getter @Setter
public class PlayersTotal implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Player player;
    private Integer total;
}
