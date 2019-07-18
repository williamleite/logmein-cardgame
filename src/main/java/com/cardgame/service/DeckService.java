package com.cardgame.service;

import com.cardgame.entity.Card;
import com.cardgame.entity.Game;
import com.cardgame.enums.FACE;
import com.cardgame.enums.SUIT;
import com.cardgame.repository.GameRepository;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
public class DeckService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private GameRepository gameRepository;

    public List<Card> createDeck() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Boolean addDeck(final UUID gameId) {
        try {
            Game game = this.gameRepository.getOne(gameId);
            game.getDeck().addAll(this.createDeck());
            this.gameRepository.saveAndFlush(game);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
