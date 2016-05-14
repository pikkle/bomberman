package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.util.Observable;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public abstract class Element extends Observable {
	protected Point2D position;
	private ImageView renderView;

	protected Arena arena;

	public Element(Point2D position, ImageView renderView, Arena arena) {
		this.position = position;
		this.renderView = renderView;
		this.arena = arena;
	}

	public Point2D getPosition() {
		return position;
	}

	public ImageView render() {
		return renderView;
	}
}
