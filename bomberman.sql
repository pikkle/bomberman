-- -----------------------------------------------------
-- Table player
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS player (
  id INTEGER PRIMARY KEY AUTOINCREMENT, 
  pseudo VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL, 
  CONSTRAINT pseudo_unique UNIQUE (pseudo)
);


-- -----------------------------------------------------
-- Table game
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS game (
  id INTEGER PRIMARY KEY AUTOINCREMENT, 
  winnerId INT NOT NULL,
  CONSTRAINT fk_game_player1 FOREIGN KEY (winnerId) REFERENCES player (id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table kill
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS kill (
  moment TIME(4) NOT NULL,
  killerId INT NOT NULL,
  victimId INT NOT NULL,
  gameId INT NOT NULL,
  PRIMARY KEY (moment, killerId, victimId, gameId),
  CONSTRAINT fk_kill_player
    FOREIGN KEY (killerId)
    REFERENCES player (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_kill_player1
    FOREIGN KEY (victimId)
    REFERENCES player (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_kill_game1
    FOREIGN KEY (gameId)
    REFERENCES game (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table participation
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS participation (
  playerId INT NOT NULL,
  gameId INT NOT NULL,
  PRIMARY KEY (playerId, gameId),
  CONSTRAINT fk_player_has_game_player1
    FOREIGN KEY (playerId)
    REFERENCES player (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_player_has_game_game1
    FOREIGN KEY (gameId)
    REFERENCES game (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table putBombs
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS putBombs (
  number INT NOT NULL,
  gameId INT NOT NULL,
  playerId INT NOT NULL,
  PRIMARY KEY (number, gameId, playerId),
  CONSTRAINT fk_putBombs_game1
    FOREIGN KEY (gameId)
    REFERENCES game (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_putBombs_player1
    FOREIGN KEY (playerId)
    REFERENCES player (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
