package com.cardgame.repository;

import com.cardgame.entity.Card;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author William Leite
 */
public interface CardRepository extends JpaRepository<Card, UUID> {
}