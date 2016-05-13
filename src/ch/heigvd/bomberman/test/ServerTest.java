package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Arena.SimpleArena;
import ch.heigvd.bomberman.server.database.ArenaORM;
import org.junit.Test;

public class ServerTest {

	@Test
	public void testArena() throws Exception {
		Arena arena = new SimpleArena();
		ArenaORM orm = new ArenaORM();
		orm.create(arena);

		orm.findAll().forEach(storedArena -> {
			System.out.println(storedArena.getElements().size());
		});
	}

}