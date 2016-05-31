package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.explosion.CentralExplosion;
import ch.heigvd.bomberman.common.game.explosion.HorizontalExplosion;
import ch.heigvd.bomberman.common.game.explosion.VerticalExplosion;
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

    @Override
    public void visit(HorizontalExplosion explosion) {
        arena.insert(explosion);
    }

    @Override
    public void visit(VerticalExplosion explosion) {
        arena.insert(explosion);
    }

    @Override
    public void visit(CentralExplosion explosion) {
        arena.insert(explosion);
    }
}
