package ch.heigvd.bomberman.common.game;

import javafx.geometry.Point2D;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public abstract class Element {
   protected Point2D position;

   public Element(Point2D point2D) {
	  position = point2D;
   }
}
