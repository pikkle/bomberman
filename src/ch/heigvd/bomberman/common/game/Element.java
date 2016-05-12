package ch.heigvd.bomberman.common.game;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.util.Observable;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public abstract class Element extends Observable {
	protected Point2D position;
	private ImageView renderView;

	public Element(Point2D position, ImageView renderView) {
		this.position = position;
		this.renderView = renderView;
	}

	public Point2D getPosition() {
		return position;
	}

	public ImageView render() {
		return renderView;
	}
}
