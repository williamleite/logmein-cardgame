package com.cardgame.service;

import com.cardgame.entity.Card;
import com.cardgame.entity.Game;
import com.cardgame.enums.SUIT;
import com.cardgame.exception.EmptyGameDeckException;
import com.cardgame.exception.EmptyGamePlayersException;
import com.cardgame.exception.InvalidGameIDException;
import com.cardgame.repository.GameRepository;
import com.cardgame.vo.PlayersTotal;
import com.cardgame.vo.RemainingCard;
import com.cardgame.vo.SuitsTotal;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AnalyticsService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private GameRepository gameRepository;

    public List<Card> cards(final UUID gameId, final UUID playerId) throws InvalidGameIDException, EmptyGameDeckException {
        Game game = this.gameRepository.findById(gameId).orElse(null);
        if (Objects.nonNull(game)) {
            if (Objects.nonNull(game.getDeck()) && !game.getDeck().isEmpty()) {
                return game.getDeck().stream()
                    .filter(c -> Objects.nonNull(c.getPlayer()))
                    .filter(c -> Objects.equals(c.getPlayer().getId(), playerId))
                    .collect(Collectors.toList());
            } else {
                throw new EmptyGameDeckException(gameId);
            }
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }

    public List<PlayersTotal> playersTotal(final UUID gameId) throws InvalidGameIDException, EmptyGamePlayersException {
        Game game = this.gameRepository.getOne(gameId);
        if (Objects.nonNull(game)) {
            if (Objects.nonNull(game.getPlayers()) && !game.getPlayers().isEmpty()) {
                return game.getPlayers().stream().map(p -> {
                    PlayersTotal pt = new PlayersTotal();
                    pt.setPlayer(p);
                    pt.setTotal(game.getDeck().stream()
                        .filter(c -> Objects.nonNull(c.getPlayer()))
                        .filter(c -> Objects.equals(c.getPlayer().getId(), p.getId()))
                        .map(c -> c.getFace().getValue())
                        .reduce(0, (a, b) -> a + b));
                    return pt;
                }).sorted((pt1, pt2) -> pt2.getTotal() - pt1.getTotal()).collect(Collectors.toList());
            } else {
                throw new EmptyGamePlayersException(gameId);
            }
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }

    public List<SuitsTotal> suitsTotal(final UUID gameId) throws InvalidGameIDException, EmptyGameDeckException {
        Game game = this.gameRepository.getOne(gameId);
        if (Objects.nonNull(game)) {
            if (Objects.nonNull(game.getDeck()) && !game.getDeck().isEmpty()) {
                return game.getDeck().stream()
                    .filter(c -> Objects.isNull(c.getPlayer()))
                    .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting())).entrySet().stream().map(e -> {
                    SuitsTotal st = new SuitsTotal();
                    st.setSuit(e.getKey());
                    st.setTotal(e.getValue());
                    return st;
                }).collect(Collectors.toList());
            } else {
                throw new EmptyGameDeckException(gameId);
            }
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }

    public List<RemainingCard> remainingCards(UUID gameId) throws EmptyGameDeckException, InvalidGameIDException {
        Game game = this.gameRepository.getOne(gameId);
        if (Objects.nonNull(game)) {
            if (Objects.nonNull(game.getDeck()) && !game.getDeck().isEmpty()) {
                return game.getDeck().stream()
                    .filter(c -> Objects.isNull(c.getPlayer()))
                    .collect(Collectors.groupingBy(Function.identity(), () -> new TreeMap<>(Comparator.<Card, SUIT>comparing(c -> c.getSuit()).thenComparing(c -> c.getFace())), Collectors.counting()))
                    .entrySet().stream().map(e -> {
                        RemainingCard rc = new RemainingCard();
                        rc.setFace(e.getKey().getFace());
                        rc.setSuit(e.getKey().getSuit());
                        rc.setCount(e.getValue());
                        return rc;
                    })
                    .sorted(Comparator.<RemainingCard, Integer>comparing(rc -> rc.getSuit().getSequence()).reversed().thenComparing(rc -> rc.getFace().getValue()).reversed())
                    .collect(Collectors.toList());
            } else {
                throw new EmptyGameDeckException(gameId);
            }
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }
}