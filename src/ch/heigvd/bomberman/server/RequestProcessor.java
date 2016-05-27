package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.server.database.PlayerORM;
import ch.heigvd.bomberman.server.database.arena.ArenaORM;

import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public Response visit(ArenasRequest request) {
		if (!requestManager.isLoggedIn())
			return new ArenasResponse(request.getID(), null);

		try {
			return new ArenasResponse(request.getID(), server.getDatabase().getOrm(ArenaORM.class).findAll());
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArenasResponse(request.getID(), null);
		}
	}

	@Override
	public Response visit(CreateRoomRequest request) {
		if (!requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "You must be logged !");

		if(request.getName() == null || request.getName().isEmpty() || request.getMinPlayer() < 2 || request.getMinPlayer() > 4)
			return new ErrorResponse(request.getID(), "Some fields are wrong or are missing !");

		if(server.getRooms().stream().filter(room -> room.getName().equals(request.getName())).findFirst().isPresent())
			return new ErrorResponse(request.getID(), "This name is already used!");

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

	@Override
	public Response visit(RoomsRequest request) {
		if (!requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "You must be logged !");

		return new RoomsResponse(request.getID(), server.getRooms().stream().map(r -> r.getClientRoom()).collect(Collectors.toList()));
	}

	@Override
	public Response visit(JoinRoomRequest request) {
		if (!requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "You must be logged !");

		if (request.getRoom() == null)
			return new ErrorResponse(request.getID(), "Choose a room !");

		Optional<Room> room = server.getRooms().stream().filter(r -> r.getName().equals(request.getRoom().getName())).findFirst();

		if(!room.isPresent())
			return new ErrorResponse(request.getID(), "Room not found !");

		if(room.get().isRunning())
			return new ErrorResponse(request.getID(), "Room already running !");

		if(room.get().getPlayers().stream().filter(playerSession -> playerSession.getPlayer().getId() == requestManager.getPlayer().getId()).findFirst().isPresent())
			return new ErrorResponse(request.getID(), "You already joined this room !");

		PlayerSession playerSession = new PlayerSession(requestManager.getPlayer(), room.get());
		room.get().addPlayer(playerSession);

		requestManager.setRoom(room.get());
		requestManager.setPlayerSession(playerSession);

		return new SuccessResponse(request.getID(), "Are you ready ?");
	}

	@Override
	public Response visit(ReadyRequest request) {
		if (!requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "You must be logged !");

		if(requestManager.getRoom() == null || requestManager.getPlayerSession() == null)
			return new ErrorResponse(request.getID(), "You have not joined a room yet !");

		requestManager.getPlayerSession().ready(request.getState());

		if(!requestManager.getRoom().isRunning())
			return new ReadyResponse(request.getID(), null);

		return new ReadyResponse(request.getID(), requestManager.getPlayerSession().getBomberman());
	}
}