package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Arena.SimpleArena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;
import ch.heigvd.bomberman.common.game.Wall;
import ch.heigvd.bomberman.server.database.arena.ArenaORM;
import ch.heigvd.bomberman.server.database.arena.elements.ElementORM;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ElementTest {

	ElementORM<Element> elementOrm;
	ArenaORM arenaOrm;
	Arena arena;

	@Before
	public void setUp() throws Exception {
		elementOrm = ElementORM.getInstance();
		arenaOrm = ArenaORM.getInstance();
		arena = new SimpleArena();

		arenaOrm.create(arena);
	}

	@After
	public void tearDown() throws Exception {
		arenaOrm.delete(arena);
	}

	@Test
	public void testFind() throws Exception {
		List<Element> all = elementOrm.findByArena(arena);
		assertEquals(all.size(), 69);
		all.forEach(element -> assertEquals(element.getArena().getId(), arena.getId()));
	}

	@Test
	public void testCreate() throws Exception {
		Element element = new Wall(new Point(13, 6), arena);
		elementOrm.create(element);
		assertNotNull(element.getId());
		List<Element> all = elementOrm.findByArena(arena);
		assertEquals(all.size(), 70);
		assertTrue(arena.getElements().contains(element));
	}

	@Test
	public void testDelete() throws Exception {
		Element element = elementOrm.findOneByArena(arena);
		elementOrm.delete(element);
		assertNull(elementOrm.find(element.getId()));
		List<Element> all = elementOrm.findByArena(arena);
		assertEquals(all.size(), 68);
		assertFalse(arena.getElements().contains(element));
	}
}