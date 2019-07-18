package com.cardgame.entity;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "player")
@Getter @Setter
public class Player implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private UUID id;
    
    @Column(name = "name")
    private String name;
}