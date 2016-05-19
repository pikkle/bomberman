package ch.heigvd.bomberman.client.views.render;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ElementRenderer implements ElementVisitor
{
	private HashMap<Element, ImageView> rendered = new HashMap<>();
	private ArenaRenderer renderer;

	public ElementRenderer(ArenaRenderer renderer){
		this.renderer = renderer;
	}

	@Override
	public void visit(Wall wall) {
		if(!rendered.containsKey(wall))
			rendered.put(wall, new ImageView(new Image("ch/heigvd/bomberman/client/img/wall.png")));
	}

	@Override
	public void visit(Box box) {
		if(!rendered.containsKey(box))
			rendered.put(box, new ImageView(new Image("ch/heigvd/bomberman/client/img/box.png")));
	}

	@Override
	public void visit(Bomb bomb) {
		if(!rendered.containsKey(bomb))
			rendered.put(bomb, new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/bomb.png")));
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
						bomberman.dropBomb();
						break;
					default:
						return;
				}
				key.consume();
			});
			sprite.setFocusTraversable(true);

			rendered.put(bomberman, sprite);
		}
	}

	public ImageView getSprite(Element element){
		if(!rendered.containsKey(element))
			return null;
		return rendered.get(element);
	}
}
