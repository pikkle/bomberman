package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.explosion.CentralExplosion;
import ch.heigvd.bomberman.common.game.explosion.HorizontalExplosion;
import ch.heigvd.bomberman.common.game.explosion.VerticalExplosion;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public interface ElementVisitor {
	public void visit(Wall wall);
	public void visit(Box box);
	public void visit(AddBombPowerUp powerUp);
	public void visit(Bomb bomb);
	public void visit(Bomberman bomberman);
	public void visit(StartPoint startPoint);
	public void visit(HorizontalExplosion explosion);
	public void visit(VerticalExplosion explosion);
	public void visit(CentralExplosion explosion);
}
