package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.Request;
import ch.heigvd.bomberman.common.communication.responses.NoResponse;
import ch.heigvd.bomberman.common.communication.responses.Response;

import java.io.*;
import java.net.Socket;

public class RequestManager extends Thread {
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

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
    public void run(){
        while (true) {
            try {
                Request request = (Request) reader.readObject();
                Response response = process(request);
                send(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Interprets the request, and makes a response to it
     * @param request
     * @return
     */
    private Response process(Request request) {
        Response response = new NoResponse();
        switch (request.getType()){
            case ACCOUNT_CREATION:
                // TODO: check the credentials of the account and create it
                break;
            // TODO: other request types
        }
        return response;
    }

    /**
     * Sends the response to the client
     * @param response
     */
    private void send(Response response) throws IOException {
        if (response.getType() != Response.Type.NO_RESPONSE)
            writer.writeObject(response);
    }

}
