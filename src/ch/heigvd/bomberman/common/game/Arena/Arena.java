package ch.heigvd.bomberman.common.game.Arena;


import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Skin;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by matthieu.villard on 09.05.2016.
 */
public class Arena {
	private int width;
	private int height;
	private List<Element> elements = new LinkedList<Element>();

	public Arena(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Bomberman addPlayer() {
		for (int i = 0; i < 4; i++) {
			Point2D position = new Point2D(1 + i / 2, 1 + i % 2);
			if (isEmpty(position)) {
				Bomberman bomberman = new Bomberman(position, Skin.values()[i], this);
				elements.add(bomberman);
				return bomberman;
			}
		}
		return null;
	}

	public boolean isEmpty(Point2D position) {
		return elements.stream().noneMatch(element -> element.getPosition().equals(position));
	}

	public List<Element> getElements() {
		return elements;
	}

	public List<Element> getElements(Point2D position) {
		return elements.stream().filter(e -> e.getPosition().equals(position)).collect(Collectors.toList());
	}

	public void add(Element element) throws Exception {
		if (getElements(element.getPosition()).stream().noneMatch(e -> e instanceof Bomb)) { // Pas de bombe déjà posée
			elements.add(element);
		} else {
			throw new Exception("Cell already occuped");
		}
	}

	public void remove(Element e) {
		// TODO end the element (kill the player, explose the box)
		elements.remove(e);
	}
}
