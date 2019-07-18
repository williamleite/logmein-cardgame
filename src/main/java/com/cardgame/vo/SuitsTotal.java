package com.cardgame.vo;

import com.cardgame.enums.SUIT;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Getter @Setter
public class SuitsTotal implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private SUIT suit;
    private Long total;
}
