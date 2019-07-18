package com.cardgame.repository;

import com.cardgame.entity.Game;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author William Leite
 */
public interface GameRepository extends JpaRepository<Game, UUID> {
}