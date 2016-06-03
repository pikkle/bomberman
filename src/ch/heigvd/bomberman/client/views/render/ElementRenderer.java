package ch.heigvd.bomberman.client.views.render;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.explosion.*;
import ch.heigvd.bomberman.common.game.powerups.AddBlastRangePowerUp;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ElementRenderer implements ElementVisitor {
	private HashMap<UUID, ImageViewPane> rendered = new HashMap<>();

	@Override
	public void visit(Wall wall) {
		if (!rendered.containsKey(wall.getUuid()))
			rendered.put(wall.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/wall.png"));
	}

	@Override
	public void visit(Box box) {
		if (!rendered.containsKey(box.getUuid()))
			rendered.put(box.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/box.png"));
	}

	@Override
	public void visit(AddBombPowerUp powerUp) {
		if (!rendered.containsKey(powerUp.getUuid()))
			rendered.put(powerUp.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/powerups/addBomb.png"));
	}

	@Override
	public void visit(AddBlastRangePowerUp powerUp) {
		if (!rendered.containsKey(powerUp.getUuid()))
			rendered.put(powerUp.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/powerups/blastRange.png"));
	}

	@Override
	public void visit(Bomb bomb) {
		if (!rendered.containsKey(bomb.getUuid()))
			rendered.put(bomb.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/bomb.png"));
	}

	@Override
	public void visit(Bomberman bomberman) {
		if (!rendered.containsKey(bomberman.getUuid()))
			rendered.put(bomberman.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/skins/" + bomberman.getSkin() + ".png"));
	}

	@Override
	public void visit(StartPoint startPoint) {
		if (!rendered.containsKey(startPoint.getUuid())) {
			Iterator<StartPoint> iterator = startPoint.arena().getStartPoints().iterator();
			int index;
			for (index = 0; iterator.hasNext() && iterator.next() != startPoint; index++) ;
			rendered.put(startPoint.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/skins/" + Skin.values()[index % Skin.values().length] + ".png"));
		}
	}

	@Override
	public void visit(HorizontalExplosion explosion) {
		if (!rendered.containsKey(explosion.getUuid()))
			rendered.put(explosion.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/explosion/explosionH.png"));
	}

	@Override
	public void visit(LeftExplosion explosion) {
		if (!rendered.containsKey(explosion.getUuid()))
			rendered.put(explosion.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/explosion/explosionL.png"));
	}

	@Override
	public void visit(RightExplosion explosion) {
		if (!rendered.containsKey(explosion.getUuid()))
			rendered.put(explosion.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/explosion/explosionR.png"));
	}

	@Override
	public void visit(VerticalExplosion explosion) {
		if (!rendered.containsKey(explosion.getUuid()))
			rendered.put(explosion.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/explosion/explosionV.png"));
	}

	@Override
	public void visit(TopExplosion explosion) {
		if (!rendered.containsKey(explosion.getUuid()))
			rendered.put(explosion.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/explosion/explosionT.png"));
	}

	@Override
	public void visit(BottomExplosion explosion) {
		if (!rendered.containsKey(explosion.getUuid()))
			rendered.put(explosion.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/explosion/explosionB.png"));
	}

	@Override
	public void visit(CentralExplosion explosion) {
		if (!rendered.containsKey(explosion.getUuid()))
			rendered.put(explosion.getUuid(), getImageViewPane("ch/heigvd/bomberman/client/img/explosion/explosionC.png"));
	}

	public ImageViewPane getSprite(Element element) {
		if (!rendered.containsKey(element.getUuid()))
			return null;
		return rendered.get(element.getUuid());
	}

	/**
	 * Return a ImageViewPane from a path.
	 *
	 * @param path the path to the image
	 * @return the ImageViewPane
	 */
	private ImageViewPane getImageViewPane(String path) {
		return new ImageViewPane(new ImageView(new Image(path)));
	}
}
