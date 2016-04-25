package ch.heigvd.bomberman.common.game;

public class Bomb {
    private int countdown;
    private int blastRange;

    public Bomb(int countdown, int blastRange) {
        this.countdown = countdown;
        this.blastRange = blastRange;
    }
}
