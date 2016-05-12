package ch.heigvd.bomberman.common.game;

import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.common.game.powerups.PowerUp;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.util.Observable;
import java.util.Observer;

/**
 * Projet : GEN_Projet
 * Créé le 05.05.2016.
 *
 * @author Adriano Ruberto
 */
public class Box extends DestructibleElement implements Observer {

    public Box(Point2D position) {
        super(position, new ImageView(new javafx.scene.image.Image("ch/heigvd/bomberman/client/img/box.png")));
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Bomb){ // Une bombe à explosé
            Bomb b = (Bomb)o;
            if(b.isInRange(position)){
                open(); // TODO add the power up to the map
            }

        }
    }

    public PowerUp open(){
        // TODO return random powerup
        return null;
    }
}
