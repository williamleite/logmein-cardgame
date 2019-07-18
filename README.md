# A Basic Deck of Cards Game
The requirements were met through the following endpoints:
- api/v1/analytics/cards/{game_id}/{player_id} | GET
Retrieves the Player's (player_id) hand in a Game (game_id).

- api/v1/analytics/players/{game_id} | GET
Retrieves all Player's with their hand's total, sorted by the total in decreasing order.
- api/v1/analytics/suits/{game_id} | GET
Retrieves number of undealt cards in the Game's (game_id) deck by SUIT.
- api/v1/analytics/remaining/{game_id} | GET
Retrieves count of each card (SUIT and FACE) sorted by SUIT, then FACE value in decreasing order.
- api/v1/deck | GET
Retrieves a new deck of cards.
- api/v1/deck/{game_id} | POST
Creates a new deck and adds to the Game (game_id) deck.
- api/v1/game | POST
Creates a new game. UUID of the New Game is returned.
- api/v1/game/{id} | DELETE
Deletes the Game (id).
- api/v1/game/{game_id}/{player_id_name} | POST
Adds the Player (player_id_name) to the Game (game_id). If the supplied value for the player is an UUID of an existing Player, adds the corresponding Player; if anything else, creates a new Player with the supplied value as its name and adds the newly created Player to the Game.
- api/v1/game/{game_id}/{player_id} | DELETE
Deletes the Player (player_id) from the Game (game_id).
- api/v1/game/deal/{game_id}/{player_id} | POST
Deals a Card from the Game (game_id) to the Player (player_id). The dealt card is returned.
- api/v1/game/peek/{game_id} | GET
Retrieves the Game's (game_id) deck sorted in the order it will be dealt.
- api/v1/game/shuffle/{game_id} | POST
Shuffles the Game's (game_id) deck.
- api/v1/player | POST
Creates a new player, with the name contained in the body of the request, e.g. ` { "name" : "Player 1" } `. UUID of the New Player is returned.
- api/v1/player/{id} | DELETE
Deletes the Player (id).