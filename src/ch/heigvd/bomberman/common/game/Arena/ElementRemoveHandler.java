package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;

/**
 * Created by matthieu.villard on 20.05.2016.
 */
public class ElementRemoveHandler implements ElementVisitor {
    private Arena arena;

    public ElementRemoveHandler(Arena arena){
        this.arena = arena;
    }

    @Override
    public void visit(Wall wall) {
        arena.delete(wall);
    }

    @Override
    public void visit(Box box) {
        arena.delete(box);
    }

    @Override
    public void visit(AddBombPowerUp powerUp) {
        arena.delete(powerUp);
    }

    @Override
    public void visit(Bomb bomb) {
        arena.delete(bomb);
    }

    @Override
    public void visit(Bomberman bomberman) {
        arena.delete(bomberman);
    }

    @Override
    public void visit(StartPoint startPoint) {
        arena.delete(startPoint);
    }
}
