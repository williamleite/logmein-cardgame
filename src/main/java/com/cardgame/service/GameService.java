package com.cardgame.service;

import com.cardgame.entity.Card;
import com.cardgame.entity.Game;
import com.cardgame.entity.Player;
import com.cardgame.exception.EmptyGameDeckException;
import com.cardgame.exception.EmptyGamePlayersException;
import com.cardgame.exception.InvalidGameIDException;
import com.cardgame.exception.InvalidPlayerIDException;
import com.cardgame.exception.PlayerNotInGameException;
import com.cardgame.repository.CardRepository;
import com.cardgame.repository.GameRepository;
import java.io.Serializable;
import java.util.Comparator;
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
        Game game = new Game();
        game.setId(UUID.randomUUID());
        this.repository.saveAndFlush(game);
        return game.getId();
    }

    @Transactional
    public void delete(final UUID id) {
        this.repository.deleteById(id);
    }

    @Transactional
    public UUID addPlayer(final UUID gameId, final String playerIdName) throws InvalidGameIDException {
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
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }

    @Transactional
    public void removePlayer(UUID gameId, UUID playerId) throws InvalidGameIDException {
        Game game = this.repository.findById(gameId).orElse(null);
        if (Objects.nonNull(game)) {
            game.getPlayers().removeIf(p -> Objects.equals(p.getId(), playerId));
            this.repository.saveAndFlush(game);
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }

    @Transactional
    public Card deal(UUID gameId, UUID playerId) throws InvalidGameIDException, EmptyGameDeckException, InvalidPlayerIDException, PlayerNotInGameException, EmptyGamePlayersException {
        Game game = this.repository.findById(gameId).orElse(null);
        if (Objects.nonNull(game)) {
            if (Objects.nonNull(game.getDeck()) && !game.getDeck().isEmpty()) {
                Card available = game.getDeck().stream()
                    .filter(c -> Objects.isNull(c.getPlayer()))
                    .sorted(Comparator.comparing(Card::getSequence)).findFirst().orElse(null);

                if (Objects.nonNull(available)) {
                    Player player = this.playerService.getOne(playerId);

                    if (Objects.nonNull(player)) {
                        if (Objects.nonNull(game.getPlayers()) && !game.getPlayers().isEmpty()) {
                            if (game.getPlayers().stream().anyMatch(p -> Objects.equals(p.getId(), playerId))) {
                                available.setPlayer(player);
                                available.setGame(game);
                                return this.cardRepository.saveAndFlush(available);
                            } else {
                                throw new PlayerNotInGameException(playerId, gameId);
                            }
                        } else {
                            throw new EmptyGamePlayersException(gameId);
                        }
                    } else {
                        throw new InvalidPlayerIDException(playerId);
                    }
                } else {
                    return null;
                }
            } else {
                throw new EmptyGameDeckException(gameId);
            }
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }

    @Transactional
    public void shuffle(UUID gameId) throws EmptyGameDeckException, InvalidGameIDException {
        Game game = this.repository.findById(gameId).orElse(null);
        if (Objects.nonNull(game)) {
            if (Objects.nonNull(game.getDeck()) && !game.getDeck().isEmpty()) {
                game.getDeck().stream()
                    .filter(c -> Objects.isNull(c.getPlayer()))
                    .forEach(c -> {
                        c.setSequence(UUID.randomUUID());
                    });
                this.repository.saveAndFlush(game);
            } else {
                throw new EmptyGameDeckException(gameId);
            }
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }

    public String peek(UUID gameId) throws InvalidGameIDException, EmptyGameDeckException {
        Game game = this.repository.findById(gameId).orElse(null);
        if (Objects.nonNull(game)) {
            if (Objects.nonNull(game.getDeck()) && !game.getDeck().isEmpty()) {
                return game.getDeck().stream()
                    .filter(c -> Objects.isNull(c.getPlayer()))
                    .sorted(Comparator.comparing(Card::getSequence))
                    .map(c -> String.format("%s - %s", c.getSuit(), c.getFace()))
                    .collect(Collectors.joining(";"));
            } else {
                throw new EmptyGameDeckException(gameId);
            }
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }
}
