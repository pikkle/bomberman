package ch.heigvd.bomberman.common.game;


import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a bomberman character in-game
 */
public class Bomberman extends DestructibleElement {
   private Skin skin;
   private Bomb bomb;
   private double speed = 1;
   private int maxBombs = 1;
   private List<PowerUp> powerUps = new LinkedList<>();

   /**
	* Constructs a Bomberman at a given position and a given skin
	*
	* @param position The position of the bomberman
	* @param skin     The skin of the bomberman
	*/
   public Bomberman(Point2D position, Skin skin) {
	  super(position);
	  this.skin = skin;
   }

   /**
	* Move the bomberman in the direction wanted
	*
	* @param direction the direction
	*/
   public void move(Direction direction) { // TODO Add hitbox collision detection
	  switch (direction) {
		 case RIGHT:
			position = position.add(speed, 0);
			break;
		 case LEFT:
			position = position.add(-speed, 0);
			break;
		 case UP:
			position = position.add(0, speed);
			break;
		 case DOWN:
			position = position.add(0, -speed);
			break;
	  }

   }

   /**
	* Drop the bomb
	*/
   public void dropBomb() {
	  // TODO add the bomb to the map

   }

   /**
	* Add a power to the bomberman and apply it.
	*
	* @param powerUp the power up
	*/
   public void givePowerup(PowerUp powerUp) {
	  powerUps.add(powerUp);
	  powerUp.apply(this);
   }

   /**
	* @return the bomb of the bomberman
	*/
   public Bomb getBomb() {
	  return bomb;
   }

   public double getSpeed() {return speed;}

   public void setSpeed(double speed) {this.speed = speed;}
}

