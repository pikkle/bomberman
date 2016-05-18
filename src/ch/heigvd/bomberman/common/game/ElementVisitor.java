package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.bombs.Bomb;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public interface ElementVisitor {
	public void visit(Wall wall);
	public void visit(Box box);
	public void visit(Bomb bomb);
	public void visit(Bomberman bomberman);
}
