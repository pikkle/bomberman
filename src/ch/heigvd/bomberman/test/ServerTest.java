package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Arena.SimpleArena;
import ch.heigvd.bomberman.server.database.arena.ArenaORM;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServerTest {

	@Test
	public void testArena() throws Exception {
		ArenaORM orm = new ArenaORM();
		List<Arena> all = orm.findAll();
		if(!all.isEmpty()){
			all.forEach(arena -> {
				try {
					orm.delete(arena);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			});
		}
		all = orm.findAll();
		assertTrue(all.isEmpty());

		orm.create(new SimpleArena());
		all = orm.findAll();
		assertFalse(all.isEmpty());
		assertEquals(all.size(), 1);
	}

	@Test
	public void testElement() throws Exception {
		testArena();

	}

}