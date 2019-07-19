package com.cardgame.vo;

import com.cardgame.enums.FACE;
import com.cardgame.enums.SUIT;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Getter @Setter
public class CardVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID id;
    private SUIT suit;
    private FACE face;
}