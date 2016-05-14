package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Arena.SimpleArena;
import ch.heigvd.bomberman.server.database.arena.ArenaORM;
import org.junit.Test;

public class ServerTest {

	@Test
	public void testArena() throws Exception {
		Arena arena = new SimpleArena();
		ArenaORM orm = new ArenaORM();
		orm.create(arena);

		orm.findAll().forEach(storedArena -> {
			System.out.println("arena : ");
			storedArena.getElements().forEach(element -> {
				System.out.println("  Element:");
				System.out.println("    type : " + element.getClass().getSimpleName());
				System.out.println("    x : " + element.getPosition().getX());
				System.out.println("    y : " + element.getPosition().getY());
			});
		});
	}

}