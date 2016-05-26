package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;

/**
 * Created by matthieu.villard on 23.05.2016.
 */
public class ElementAddHandler implements ElementVisitor {

    private Arena arena;

    public ElementAddHandler(Arena arena){
        this.arena = arena;
    }

    @Override
    public void visit(Wall wall) {
        arena.insert(wall);
    }

    @Override
    public void visit(Box box) {
        arena.insert(box);
    }

    @Override
    public void visit(AddBombPowerUp powerUp) {
        arena.insert(powerUp);
    }

    @Override
    public void visit(Bomb bomb) {
        arena.insert(bomb);
    }

    @Override
    public void visit(Bomberman bomberman) {
        arena.insert(bomberman);
    }

    @Override
    public void visit(StartPoint startPoint) {
        arena.insert(startPoint);
    }
}
