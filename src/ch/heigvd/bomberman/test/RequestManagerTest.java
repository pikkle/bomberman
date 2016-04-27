package ch.heigvd.bomberman.test;

import ch.heigvd.bomberman.common.communication.requests.AccountCreation;
import ch.heigvd.bomberman.common.communication.requests.Request;
import ch.heigvd.bomberman.server.RequestManager;
import org.junit.Test;

import java.net.Socket;

import static org.junit.Assert.*;

public class RequestManagerTest {

    @Test
    public void testRun() throws Exception {
        RequestManager manager = new RequestManager(new Socket());
        Request ac = new AccountCreation("","");
        manager.process(ac);
    }
}