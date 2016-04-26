package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.server.Server;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerTest {

    @Test
    public void testDefaultMain() throws Exception {
        Server.main();
    }

    @Test
    public void testMainWithCorrectInput() throws Exception {
        Server.main("4848");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMainWithTooLowInput() throws Exception {
        Server.main("-3737");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMainWithTooHighInput() throws Exception {
        Server.main("1000000");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testMainWithTooManyInputs() throws Exception {
        Server.main("1000000", "hello world");
    }

}