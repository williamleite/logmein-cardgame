package com.cardgame.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public enum SUIT {
    HEARTS(1),
    SPADES(2),
    CLUBS(3),
    DIAMONDS(4);
    
    @Getter private final Integer sequence;
    
    SUIT(final Integer sequence) {
        this.sequence = sequence;
    }
    
    public static List<SUIT> ordered() {
        return Arrays.stream(SUIT.values()).sorted(Comparator.comparing(SUIT::getSequence)).collect(Collectors.toList());
    }
}