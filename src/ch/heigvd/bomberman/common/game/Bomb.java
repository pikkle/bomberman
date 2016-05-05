package ch.heigvd.bomberman.common.game;


import javafx.geometry.Point2D;

public abstract class Bomb extends DestructibleElement{
    private int countdown;
    private int blastRange;

    public Bomb(Point2D position, int countdown, int blastRange) {
        super(position);
        this.countdown = countdown;
        this.blastRange = blastRange;
    }

    public abstract void explose();
}
