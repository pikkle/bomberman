package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Point;
import ch.heigvd.bomberman.common.game.Wall;
import ch.heigvd.bomberman.server.database.DBManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by matthieu.villard on 18.05.2016.
 */
public class ElementTest {

	DBManager db;
	Arena arena;

	@Before
	public void setUp() throws Exception {
		db = DBManager.getInstance();
		arena = new Arena();
		db.arenas().create(arena);
	}

	@After
	public void tearDown() throws Exception {
		db.arenas().delete(arena);
	}

	@Test
	public void testFind() throws Exception {
		Collection<Element> all = arena.getElements();
		assertEquals(all.size(), 69);
		all.forEach(element -> assertEquals(element.getArena().getId(), arena.getId()));
	}

	@Test
	public void testCreate() throws Exception {
		Element element = new Wall(new Point(13, 6), arena);
		db.elements().create(element);
		assertNotNull(element.getId());

		assertEquals(arena.getElements().size(), 70);
		assertTrue(arena.getElements().contains(element));
	}

	@Test
	public void testDelete() throws Exception {
		Element element = arena.getElements().stream().findFirst().get();

		db.elements().delete(element);

		assertNull(db.elements().find(Element.class, element.getId()));
		assertEquals(arena.getElements().size(), 68);
		assertFalse(arena.getElements().contains(element));
	}

	@Test
	public void testWall() throws Exception {
		Wall wall = new Wall(new Point(5, 7), arena);

		db.walls().create(wall);
	}
}