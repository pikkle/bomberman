package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.DestructibleElement;
import javafx.geometry.Point2D;

public abstract class Bomb extends DestructibleElement {
   private int countdown;
   private int blastRange;

   public Bomb(Point2D position, int countdown, int blastRange) {
	  super(position);
	  this.countdown = countdown;
	  this.blastRange = blastRange;
   }

   public abstract void explose();

   public boolean isInRange(Point2D position){ // TODO Gérer les collisions avec les murs
	  return false;
   }

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
