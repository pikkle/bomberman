package ch.heigvd.bomberman.common.game.Arena;

import ch.heigvd.bomberman.common.game.Box;
import ch.heigvd.bomberman.common.game.Point;
import ch.heigvd.bomberman.common.game.Wall;

import java.net.URISyntaxException;

/**
 * Projet : GEN_Projet
 * Créé le 19.05.2016.
 *
 * @author Adriano Ruberto
 */
public class BasicArena extends Arena {
	public BasicArena(int width, int height) throws URISyntaxException {
		super(width, height);
		for(int l = 2; l < height - 1; l += 2){
			for(int c = 2; c < width - 1; c += 2){
				new Wall(new Point(l, c), this);
			}
		}

		new Box(new Point(5, 4), this);
		new Box(new Point(5, 5), this);

		for(int l = 1; l < height - 1; ++l){
			for(int c = 1; c < width - 1; ++c){

			}
		}
	}
}