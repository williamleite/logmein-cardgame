package com.cardgame.entity;

import com.cardgame.enums.FACE;
import com.cardgame.enums.SUIT;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "card")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter @Setter
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Enumerated
    @Column(name = "suit")
    private SUIT suit;

    @Enumerated
    @Column(name = "face")
    private FACE face;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;
    
    @Column(name = "sequence")
    private UUID sequence;
}
