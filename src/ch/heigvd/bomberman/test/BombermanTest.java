package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Direction;
import ch.heigvd.bomberman.common.game.Skin;
import javafx.geometry.Point2D;

import static org.junit.Assert.*;

public class BombermanTest {

    @org.junit.Test
    public void testMove() {
	   Bomberman bomberman = new Bomberman(Point2D.ZERO, Skin.SKIN1);
	   Point2D positionExpected = bomberman.getPosition().add(bomberman.getSpeed(), 0);
	   bomberman.move(Direction.RIGHT);

	   assertEquals(positionExpected, bomberman.getPosition());
    }

    @org.junit.Test
    public void testDropBomb() throws Exception {

    }

    @org.junit.Test
    public void testGivePowerup() throws Exception {

    }

    @org.junit.Test
    public void testGetBomb() throws Exception {

    }
}