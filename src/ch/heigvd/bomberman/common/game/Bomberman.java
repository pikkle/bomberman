package ch.heigvd.bomberman.common.game;


import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import javafx.geometry.Point2D;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a bomberman character in-game
 */
public class Bomberman extends DestructibleElement implements KeyListener {
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

   public void move(Direction dir) {
	  switch (dir) {
		 case RIGHT:
			position.add(speed, 0);
			break;
		 case LEFT:
			position.add(-speed, 0);
			break;
		 case UP:
			position.add(0, speed);
			break;
		 case DOWN:
			position.add(0, -speed);
			break;
	  }

   }

   public void dropBomb() {

   }

   public void givePowerup(PowerUp powerUp) {
	  powerUps.add(powerUp);
	  powerUp.apply(this);
   }

   public Bomb getBomb() {
	  return bomb;
   }

   @Override
   public void keyTyped(KeyEvent e) {
	  switch (e.getKeyCode()) {
		 case KeyEvent.VK_A | KeyEvent.VK_LEFT:
			move(Direction.RIGHT);
			break;
		 case KeyEvent.VK_D | KeyEvent.VK_RIGHT:
			move(Direction.LEFT);
			break;
		 case KeyEvent.VK_S | KeyEvent.VK_DOWN:
			move(Direction.DOWN);
			break;
		 case KeyEvent.VK_W | KeyEvent.VK_UP:
			move(Direction.UP);
			break;
		 case KeyEvent.VK_SPACE:
			dropBomb();
			break;
	  }

   }

   @Override
   public void keyPressed(KeyEvent e) {

   }

   @Override
   public void keyReleased(KeyEvent e) {

   }

   private enum Direction {
	  RIGHT, LEFT, UP, DOWN
   }
}

