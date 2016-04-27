package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.AccountCreation;
import ch.heigvd.bomberman.common.communication.requests.HelloRequest;
import ch.heigvd.bomberman.common.communication.requests.Request;
import ch.heigvd.bomberman.common.communication.responses.HelloResponse;
import ch.heigvd.bomberman.common.communication.responses.NoResponse;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.communication.responses.ResponseType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestManager extends Thread {
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    public boolean running = true;

    public RequestManager(Socket socket) {
        this.socket = socket;
        try {
            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.reader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Request request = (Request) reader.readObject();
                Response response = process(request);
                send(response);
            } catch (EOFException e) {
                System.out.println("Client closed the connection");
                running = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Interprets the request, and makes a response to it
     *
     * @param request
     * @return
     */
    public Response process(Request request) {
        Response response = new NoResponse();
        switch (request.getType()) {
            case ACCOUNT_CREATION:
                break;
            case HELLO_REQUEST:
                HelloRequest req = (HelloRequest) request;
                System.out.println("Received message: ");
                System.out.println(req.getMessage());
                return new HelloResponse("Hello !");
            // TODO: other request types
        }
        return response;
    }


    private Response process(AccountCreation request) {
        System.out.println("coucou");
        return null;
    }

    /**
     * Sends the response to the client
     *
     * @param response
     */
    private void send(Response response) throws IOException {
        if (response.getType() != ResponseType.NO_RESPONSE) writer.writeObject(response);
    }

}