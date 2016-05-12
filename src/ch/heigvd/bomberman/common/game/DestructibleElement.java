package ch.heigvd.bomberman.common.game;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;



/**
 * Created by matthieu.villard on 09.05.2016.
 */
public abstract class DestructibleElement extends Element
{
    public DestructibleElement(Point2D position, ImageView renderView) {
        super(position, renderView);
    }

}
