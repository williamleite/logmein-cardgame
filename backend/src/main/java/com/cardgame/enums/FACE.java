package com.cardgame.enums;

import lombok.Getter;

/**
 *
 * @author William Leite
 */
public enum FACE {
    F_A("A", 1),
    F_2("2", 2),
    F_3("3", 3),
    F_4("4", 4),
    F_5("5", 5),
    F_6("6", 6),
    F_7("7", 7),
    F_8("8", 8),
    F_9("9", 9),
    F_10("10", 10),
    F_J("J", 11),
    F_Q("Q", 12),
    F_K("K", 13);
    
    @Getter final String description;
    @Getter final Integer value;
    
    FACE(final String description, final Integer value) {
        this.description = description;
        this.value = value;
    }
}
