package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.DestructibleElement;
import ch.heigvd.bomberman.common.game.Element;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;


public abstract class Bomb extends DestructibleElement {
   private int countdown;
   private int blastRange;
   private Arena arena;

   public Bomb(Point2D position, int countdown, int blastRange, Arena arena) {
	  super(position, new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/bomb.png")));
	  this.countdown = countdown;
	  this.blastRange = blastRange;
	  this.arena = arena;
   }

   public void explose() {
	  int y = (int) position.getY();
	  int x = (int) position.getX();

   }

   public abstract boolean isInRange(Element e); // TODO Gérer les collisions avec les murs

   public int getBlastRange() {
	  return blastRange;
   }

   public void setBlastRange(int blastRange) {
	  this.blastRange = blastRange;
   }

   public int getCountdown() {
	  return countdown;
   }

   public void setCountdown(int countdown) {
	  this.countdown = countdown;
   }
}
