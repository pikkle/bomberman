package ch.heigvd.bomberman.client.views.render;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ElementRenderer implements ElementVisitor
{
	private HashMap<Element, ImageViewPane> rendered = new HashMap<>();

	@Override
	public void visit(Wall wall) {
		if(!rendered.containsKey(wall))
			rendered.put(wall, new ImageViewPane(new ImageView(new Image("ch/heigvd/bomberman/client/img/wall.png"))));
	}

	@Override
	public void visit(Box box) {
		if(!rendered.containsKey(box))
			rendered.put(box, new ImageViewPane(new ImageView(new Image("ch/heigvd/bomberman/client/img/box.png"))));
	}

	@Override
	public void visit(AddBombPowerUp powerUp) {
		if(!rendered.containsKey(powerUp))
			rendered.put(powerUp, new ImageViewPane(new ImageView(new Image("ch/heigvd/bomberman/client/img/powerups/addBomb.png"))));
	}

	@Override
	public void visit(Bomb bomb) {
		if(!rendered.containsKey(bomb))
			rendered.put(bomb, new ImageViewPane(new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/bomb.png"))));
	}

	@Override
	public void visit(Bomberman bomberman) {
		if(!rendered.containsKey(bomberman))
			rendered.put(bomberman, new ImageViewPane(new ImageView(new Image("ch/heigvd/bomberman/client/img/skins/" + bomberman.getSkin() + ".png"))));
	}

	@Override
	public void visit(StartPoint startPoint) {
		if(!rendered.containsKey(startPoint)) {
			Iterator<StartPoint> iterator = startPoint.getArena().getStartPoints().iterator();
			int index;
			for(index = 0; iterator.hasNext() && iterator.next() != startPoint; index++);
			rendered.put(startPoint, new ImageViewPane(new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/skins/" + Skin.values()[index % Skin.values().length] + ".png"))));
		}
	}

	public ImageViewPane getSprite(Element element){
		if(!rendered.containsKey(element))
			return null;
		return rendered.get(element);
	}
}
