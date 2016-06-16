package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.server.database.DBManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ArenaTest {
	DBManager db;

	@Before
	public void setUp() throws Exception {
		db = DBManager.getInstance();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testLazy() throws Exception {
		Optional<Arena> arena = db.arenas().find(new Long(1));
		System.out.println(arena.get().getId());
	}

	@Test
	public void testCreate() throws Exception {
		List<Arena> before = db.arenas().findAll();
		Arena arena = new Arena();
		db.arenas().create(arena);
		List<Arena> after = db.arenas().findAll();
		assertEquals(before.size() + 1, after.size());
		assertNotNull(arena.getId());
	}

	@Test
	public void testDelete() throws Exception {
		Arena arena = new Arena();
		db.arenas().create(arena);
		List<Arena> before = db.arenas().findAll();
		db.arenas().delete(arena);
		Optional<Arena> after = db.arenas().find(arena.getId());
		assertTrue(after.isPresent());
	}

	@Test
	public void testUpdate() throws Exception {
		Arena arena = new Arena();
		db.arenas().create(arena);
		arena.elements().stream().findFirst().ifPresent(arena::remove);
		db.arenas().update(arena);

		Optional<Arena> after = db.arenas().find(arena.getId());
		assertEquals(68, after.get().elements().size());
	}
}