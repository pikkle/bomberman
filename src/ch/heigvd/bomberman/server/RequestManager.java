package ch.heigvd.bomberman.server;

import java.io.*;
import java.net.Socket;

public class RequestManager extends Thread {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public RequestManager(Socket socket) {
        this.socket = socket;
        try {
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run(){
        while (true) {
            try {
                String line = reader.readLine();
                System.out.println(line);
                writer.write(line);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //TODO: à la fermeture d'un socket, supprimer le client manager de la liste du server
}
