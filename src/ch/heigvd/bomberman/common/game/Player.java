package ch.heigvd.bomberman.common.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Projet : GEN_Projet
 * Créé le 07.05.2016.
 *
 * @author Adriano Ruberto
 */
public class Player implements KeyListener {
   private static int ID = 0;
   private final int id = ID++;
   private Bomberman bomberman;

   private String pseudo;

   public Player(String pseudo) {this.pseudo = pseudo;}

   public int getId() {
	  return id;
   }

   public String getPseudo() {
	  return pseudo;
   }

   @Override
   public void keyTyped(KeyEvent e) {
	  switch (e.getKeyCode()) {
		 case KeyEvent.VK_A | KeyEvent.VK_LEFT:
			bomberman.move(Direction.LEFT);
			break;
		 case KeyEvent.VK_D | KeyEvent.VK_RIGHT:
			bomberman.move(Direction.RIGHT);
			break;
		 case KeyEvent.VK_S | KeyEvent.VK_DOWN:
			bomberman.move(Direction.DOWN);
			break;
		 case KeyEvent.VK_W | KeyEvent.VK_UP:
			bomberman.move(Direction.UP);
			break;
		 case KeyEvent.VK_SPACE:
			bomberman.dropBomb();
			break;
	  }

   }

   @Override
   public void keyPressed(KeyEvent e) {

   }

   @Override
   public void keyReleased(KeyEvent e) {

   }
}
