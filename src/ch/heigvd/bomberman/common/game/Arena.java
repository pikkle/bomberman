package ch.heigvd.bomberman.common.game;

import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.List;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public class Arena {
   private List<Element> elements = new LinkedList<>();

   public Arena(){
	  elements.add(new Bomberman(new Point2D(10, 10), Skin.SKIN1));
   }

}
