package ch.heigvd.bomberman.common.game.bombs;

import ch.heigvd.bomberman.common.game.DestructibleElement;
import javafx.scene.image.ImageView;

import java.awt.*;

public abstract class Bomb extends DestructibleElement {
    private int countdown;
    private int blastRange;

    public Bomb(Point position, int countdown, int blastRange) {
        super(position, new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/bomb.png")));
        this.countdown = countdown;
        this.blastRange = blastRange;
    }

    public abstract void explose();

    public boolean isInRange(Point position){ // TODO Gérer les collisions avec les murs
        return false;
    }

    public int getBlastRange() {
        return blastRange;
    }

    public void setBlastRange(int blastRange) {
        this.blastRange = blastRange;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }
}