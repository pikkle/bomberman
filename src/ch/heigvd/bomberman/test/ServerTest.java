package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.server.Server;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerTest {

    @Test
    public void testDefaultMain() throws Exception {
        Server.main();
    }

}