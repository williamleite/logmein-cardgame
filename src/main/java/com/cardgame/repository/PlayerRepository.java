package com.cardgame.repository;

import com.cardgame.entity.Player;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author William Leite
 */
public interface PlayerRepository extends JpaRepository<Player, UUID> {
}