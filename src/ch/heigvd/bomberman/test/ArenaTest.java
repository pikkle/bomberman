package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Arena.SimpleArena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.server.database.arena.ArenaORM;
import ch.heigvd.bomberman.server.database.arena.elements.ElementORM;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ArenaTest {
	ArenaORM orm;

	@Before
	public void setUp() throws Exception {
		orm = ArenaORM.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		List<Arena> all = orm.findAll();
		all.forEach(arena -> {
			try {
				orm.delete(arena);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void testCreate() throws Exception {
		List<Arena> before = orm.findAll();
		Arena arena = new SimpleArena();
		orm.create(arena);
		List<Arena> after = orm.findAll();
		assertEquals(before.size() + 1, after.size());
		assertNotNull(arena.getId());
	}

	@Test
	public void testDelete() throws Exception {
		Arena arena = new SimpleArena();
		orm.create(arena);
		List<Arena> before = orm.findAll();
		orm.delete(arena);
		Arena after = orm.find(arena.getId());
		assertNull(after);
		List<Element> all = ElementORM.getInstance().findByArena(arena);
		assertTrue(all.isEmpty());
	}

	@Test
	public void testUpdate() throws Exception {
		Arena arena = new SimpleArena();
		orm.create(arena);
		arena.remove(arena.getElements().stream().findFirst().get());
		orm.update(arena);

		Arena after = orm.find(arena.getId());
		assertEquals(68, after.getElements().size());
	}
}