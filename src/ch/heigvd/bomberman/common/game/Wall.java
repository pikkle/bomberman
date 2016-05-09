package ch.heigvd.bomberman.common.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class Wall extends Element
{
    public Wall(Point position) {
        super(position, new ImageView(new Image("bomberman/wall.png")));
    }
}
