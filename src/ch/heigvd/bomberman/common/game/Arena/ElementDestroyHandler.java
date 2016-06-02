package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.explosion.*;
import ch.heigvd.bomberman.common.game.powerups.AddBlastRangePowerUp;
import ch.heigvd.bomberman.common.game.powerups.AddBombPowerUp;

/**
 * Created by matthieu.villard on 20.05.2016.
 */
public class ElementDestroyHandler implements ElementVisitor {
    private Arena arena;

    public ElementDestroyHandler(Arena arena){
        this.arena = arena;
    }

    @Override
    public void visit(Wall wall) {
        arena.delete(wall);
    }

    @Override
    public void visit(Box box) {
        arena.destroy(box);
    }

    @Override
    public void visit(AddBombPowerUp powerUp) {
        arena.delete(powerUp);
    }

    @Override
    public void visit(AddBlastRangePowerUp powerUp) {
        arena.delete(powerUp);
    }

    @Override
    public void visit(Bomb bomb) {
        arena.destroy(bomb);
    }

    @Override
    public void visit(Bomberman bomberman) {
        arena.delete(bomberman);
    }

    @Override
    public void visit(StartPoint startPoint) {
        arena.delete(startPoint);
    }

    @Override
    public void visit(HorizontalExplosion explosion) {
        arena.delete(explosion);
    }

    @Override
    public void visit(LeftExplosion explosion) {
        arena.delete(explosion);
    }

    @Override
    public void visit(RightExplosion explosion) {
        arena.delete(explosion);
    }

    @Override
    public void visit(VerticalExplosion explosion) {
        arena.delete(explosion);
    }

    @Override
    public void visit(TopExplosion explosion) {
        arena.delete(explosion);
    }

    @Override
    public void visit(BottomExplosion explosion) {
        arena.delete(explosion);
    }

    @Override
    public void visit(CentralExplosion explosion) {
        arena.delete(explosion);
    }
}
