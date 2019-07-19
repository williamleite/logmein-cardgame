package com.cardgame.service;

import com.cardgame.entity.Card;
import com.cardgame.entity.Player;
import com.cardgame.repository.PlayerRepository;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PlayerService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private PlayerRepository repository;

    public Player getOne(UUID id) {
        return this.repository.getOne(id);
    }

    @Transactional
    public UUID add(final Player player) {
        player.setId(UUID.randomUUID());
        this.repository.saveAndFlush(player);
        return player.getId();
    }

    @Transactional
    public void delete(final UUID id) {
        this.repository.deleteById(id);
    }
}
