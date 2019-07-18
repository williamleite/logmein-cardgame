package com.cardgame.service;

import com.cardgame.entity.Card;
import com.cardgame.entity.Game;
import com.cardgame.entity.Player;
import com.cardgame.repository.CardRepository;
import com.cardgame.repository.GameRepository;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class GameService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private GameRepository repository;

    @Autowired
    private PlayerService playerService;

    @Transactional
    public UUID create() {
        try {
            Game game = new Game();
            game.setId(UUID.randomUUID());
            this.repository.saveAndFlush(game);
            return game.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Boolean delete(final UUID id) {
        try {
            this.repository.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    @Transactional
    public UUID addPlayer(final UUID gameId, final String playerIdName) {
        try {
            Player player = new Player();
            try {
                player.setId(UUID.fromString(playerIdName));
            } catch (Exception e) {
                player.setName(playerIdName);
                player.setId(this.playerService.add(player));
            }

            Game game = this.repository.findById(gameId).orElse(null);
            if (Objects.nonNull(game)) {
                game.getPlayers().add(player);
                this.repository.saveAndFlush(game);
                return player.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Transactional
    public Boolean removePlayer(UUID gameId, UUID playerId) {
        try {
            Game game = this.repository.findById(gameId).orElse(null);
            if (Objects.nonNull(game)) {
                game.getPlayers().removeIf(p -> Objects.equals(p.getId(), playerId));
                this.repository.saveAndFlush(game);
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    @Transactional
    public Card deal(UUID gameId, UUID playerId) {
        try {
            Game game = this.repository.findById(gameId).orElse(null);
            if (Objects.nonNull(game)) {
                Card available = game.getDeck().stream()
                    .filter(c -> Objects.isNull(c.getPlayer()))
                    .sorted(Comparator.comparing(Card::getSequence)).findFirst().orElse(null);

                if (Objects.nonNull(available)) {
                    Player player = this.playerService.getOne(playerId);

                    if (Objects.nonNull(player) && game.getPlayers().stream().anyMatch(p -> Objects.equals(p.getId(), playerId))) {
                        available.setPlayer(player);
                        available.setGame(game);
                        return this.cardRepository.saveAndFlush(available);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public Boolean shuffle(UUID gameId) {
        try {
            Game game = this.repository.findById(gameId).orElse(null);
            if (Objects.nonNull(game)) {
                game.getDeck().stream()
                    .filter(c -> Objects.isNull(c.getPlayer()))
                    .forEach(c -> {
                        c.setSequence(UUID.randomUUID());
                    });
                this.repository.saveAndFlush(game);
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public String peek(UUID gameId) {
        Game game = this.repository.findById(gameId).orElse(null);
        if (Objects.nonNull(game)) {
            return game.getDeck().stream()
                .filter(c -> Objects.isNull(c.getPlayer()))
                .sorted(Comparator.comparing(Card::getSequence))
                .map(c -> String.format("%s - %s", c.getSuit(), c.getFace()))
                .collect(Collectors.joining(";"));
        }
        return null;
    }
}
