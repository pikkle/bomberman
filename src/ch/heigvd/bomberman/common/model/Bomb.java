package ch.heigvd.bomberman.common.model;

public class Bomb {
    private int countdown;
    private int blastRange;

    public Bomb(int countdown, int blastRange) {
        this.countdown = countdown;
        this.blastRange = blastRange;
    }
}
