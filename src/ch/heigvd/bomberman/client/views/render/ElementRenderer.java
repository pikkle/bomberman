package ch.heigvd.bomberman.client.views.render;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ElementRenderer implements ElementVisitor, Observer
{
	private static HashMap<Element, ImageView> rendered = new HashMap<>();
	private ArenaRenderer renderer;

	public ElementRenderer(ArenaRenderer renderer){
		this.renderer = renderer;
	}

	@Override
	public void visit(Wall wall) {
		if(!rendered.containsKey(wall))
			rendered.put(wall, new ImageView(new Image("ch/heigvd/bomberman/client/img/wall.png")));
		render(wall);
	}

	@Override
	public void visit(Box box) {
		if(!rendered.containsKey(box))
			rendered.put(box, new ImageView(new Image("ch/heigvd/bomberman/client/img/box.png")));
		render(box);
	}

	@Override
	public void visit(Bomb bomb) {
		if(!rendered.containsKey(bomb))
			rendered.put(bomb, new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/bomb.png")));
		render(bomb);
	}

	@Override
	public void visit(Bomberman bomberman) {
		if(!rendered.containsKey(bomberman)) {
			ImageView sprite = new ImageView(new Image("ch/heigvd/bomberman/client/img/skins/" + bomberman.getSkin() + ".png"));

			sprite.setOnKeyPressed(key -> {
				switch (key.getCode()) {
					case RIGHT:
						bomberman.move(Direction.RIGHT);
						break;
					case LEFT:
						bomberman.move(Direction.LEFT);
						break;
					case DOWN:
						bomberman.move(Direction.DOWN);
						break;
					case UP:
						bomberman.move(Direction.UP);
						break;
					case SPACE:
						//bomberman.dropBomb().ifPresent(this::visit);
						break;
					default:
						return;
				}
				key.consume();
			});
			sprite.setFocusTraversable(true);

			rendered.put(bomberman, sprite);
		}
		render(bomberman);
	}

	private void render(Element element){
		renderer.renderElement(rendered.get(element), (int)element.getPosition().getX(), (int)element.getPosition().getY());
	}

	@Override
	public void update(Observable o, Object arg) {
		((Element) o).accept(this);
	}
}
