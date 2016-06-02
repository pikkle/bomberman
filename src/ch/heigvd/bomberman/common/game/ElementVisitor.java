package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.explosion.*;
import ch.heigvd.bomberman.common.game.powerups.AddBlastRangePowerUp;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public interface ElementVisitor {
	void visit(Wall wall);
	void visit(Box box);
	void visit(AddBombPowerUp powerUp);
	void visit(AddBlastRangePowerUp powerUp);
	void visit(Bomb bomb);
	void visit(Bomberman bomberman);
	void visit(StartPoint startPoint);
	void visit(HorizontalExplosion explosion);
	void visit(LeftExplosion explosion);
	void visit(RightExplosion explosion);
	void visit(VerticalExplosion explosion);
	void visit(TopExplosion explosion);
	void visit(BottomExplosion explosion);
	void visit(CentralExplosion explosion);
}
