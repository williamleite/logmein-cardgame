package com.cardgame.service;

import com.cardgame.entity.Card;
import com.cardgame.entity.Game;
import com.cardgame.enums.FACE;
import com.cardgame.enums.SUIT;
import com.cardgame.exception.InvalidGameIDException;
import com.cardgame.repository.GameRepository;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class DeckService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private GameRepository gameRepository;

    public List<Card> createDeck() {
        final List<Card> result = new ArrayList<>();
        Arrays.stream(SUIT.values()).forEach(s -> {
            Arrays.stream(FACE.values()).forEach(f -> {
                Card card = new Card();
                card.setFace(f);
                card.setSuit(s);
                card.setId(UUID.randomUUID());
                card.setSequence(card.getId());
                result.add(card);
            });
        });
        return result;
    }

    @Transactional
    public void addDeck(final UUID gameId) throws InvalidGameIDException {
        Game game = this.gameRepository.findById(gameId).orElse(null);
        if (Objects.nonNull(game)) {
            game.getDeck().addAll(this.createDeck());
            this.gameRepository.saveAndFlush(game);
        } else {
            throw new InvalidGameIDException(gameId);
        }
    }
}
