package ch.heigvd.bomberman.common.game;

import javafx.scene.image.ImageView;

import java.awt.*;


/**
 * Created by matthieu.villard on 09.05.2016.
 */
public abstract class DestructibleElement extends Element
{
    public DestructibleElement(Point position, ImageView renderView) {
        super(position, renderView);
    }
}
