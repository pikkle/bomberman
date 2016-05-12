package ch.heigvd.bomberman.common.game;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class Wall extends Element {
	public Wall(Point2D position) throws URISyntaxException {
		super(position, new ImageView(new Image("ch/heigvd/bomberman/client/img/wall.png")));
	}
}
