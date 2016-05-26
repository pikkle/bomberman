package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.server.database.PlayerORM;
import ch.heigvd.bomberman.server.database.arena.ArenaORM;

import java.sql.SQLException;
import java.util.Optional;

public class RequestProcessor implements RequestVisitor {
	private RequestManager requestManager;
	private static Server server = Server.getInstance();
	private static PlayerORM db;

	public RequestProcessor(RequestManager requestManager){
		this.requestManager = requestManager;
		try {
			db = server.getDatabase().getOrm(PlayerORM.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Response visit(HelloRequest request){
		System.out.println("Received message: ");
		System.out.println(request.getMessage());
		return new HelloResponse(request.getID(), "Hello !");
	}

	@Override
	public Response visit(MoveRequest request) {
		return null;
	}

	@Override
	public Response visit(ReadyRequest request) {
		return null;
	}

	@Override
	public Response visit(AccountCreationRequest request) {
		if (requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "Already logged in");
		Optional<Player> player;
		try {
			player = db.findOneByPseudo(request.getUsername());
		} catch (SQLException e) {
			player = Optional.empty();
		}
		return player.map(p -> (Response) new ErrorResponse(request.getID(), "Account name already exists !"))
				.orElseGet(()->{
					Player newPlayer = new Player(request.getUsername(), request.getPassword());
					try {
						db.create(newPlayer);
					} catch (SQLException e) {
						return new ErrorResponse(request.getID(), "Error while creating the user");
					}
					return new NoResponse(request.getID());
				});
	}

	@Override
	public Response visit(LoginRequest request) {
		if (requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "Already logged in");
		Optional<Player> player;
		try {
			player = db.findOneByPseudo(request.getUsername());
		} catch (SQLException e) {
			player = Optional.empty();
		}
		return player.filter(p->p.getPassword().equals(request.getPassword()))
				.map(p->{
					requestManager.setLoggedIn(true);
					requestManager.setPlayer(p);
					return (Response) new SuccessResponse(request.getID(), "Successfully logged in !");
				}).orElseGet(()-> new ErrorResponse(request.getID(), "Wrong credentials"));
	}

	@Override
	public Response visit(CreateRoomRequest request) {
		if (!requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "You must be logged to create a room !");

		if(request.getName() == null || request.getName().isEmpty() || request.getMinPlayer() < 2 || request.getMinPlayer() > 4)
			return new ErrorResponse(request.getID(), "Some fields are wrong or are missing !");

		Optional<Arena> arena;
		try {
			arena = server.getDatabase().getOrm(ArenaORM.class).find(request.getArena());
		} catch (SQLException e) {
			arena = Optional.empty();
		}

		return arena.filter(a->a.getId() == request.getArena())
				.map(a->{
					server.addRoom(new Room(request.getName(), request.getPassword(), request.getMinPlayer(), a));
					return (Response) new SuccessResponse(request.getID(), "Room successfully created !");
				}).orElseGet(()-> new ErrorResponse(request.getID(), "Wrong credentials"));
	}
}