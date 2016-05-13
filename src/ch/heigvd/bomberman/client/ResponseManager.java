package ch.heigvd.bomberman.client;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import javafx.util.Callback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;


public class ResponseManager {
	private static ResponseManager instance;
	private Socket socket;
	private ObjectOutputStream writer;
	private ObjectInputStream reader;
	private Map<UUID, Thread> threads;
	private Map<UUID, Response<?>> responses;

	private ResponseManager() {
		threads = new HashMap<>();
		responses = new HashMap<>();
	}

	public static ResponseManager getInstance(){
		if (instance == null)
			instance = new ResponseManager();
		return instance;
	}

	public void connect(String address, int port) {
		try {
			socket = new Socket(address, port);
			writer = new ObjectOutputStream(socket.getOutputStream());
			reader = new ObjectInputStream(socket.getInputStream());

			Thread receiver = new Thread(() -> {
				while(true) {
					try {
						Object o = reader.readObject();
						Response response = (Response) o;
						responses.put(response.getID(), response);
						Thread t = threads.get(response.getID());
						threads.remove(t);
						synchronized (this) {
							this.notify();
						}
						response.accept(ResponseProcessor.getInstance());
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			});
			receiver.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

	public void loginRequest(String username, String password, Consumer<Boolean> callback) {
		LoginRequest r = new LoginRequest(username, password);
		send(r, callback);
	}

	private <T> void send(Request<T> r, Consumer<? super T> callback) {
		try {
			writer.writeObject(r);
			Thread sender = new Thread(() -> {
				try {
					synchronized (this) {
						this.wait();
					}
					System.out.println("Hello world");
					Response<T> response = (Response<T>) responses.get(r.getID());
					responses.remove(response);
					callback.accept(response.accept(ResponseProcessor.getInstance()));

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			threads.put(r.getID(), sender);
			sender.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}