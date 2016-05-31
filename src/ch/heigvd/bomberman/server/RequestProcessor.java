package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.common.game.Bomberman;
import ch.heigvd.bomberman.common.game.Element;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.common.game.Statistic;
import ch.heigvd.bomberman.server.database.DBManager;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequestProcessor implements RequestVisitor, Observer {
	private RequestManager requestManager;
	private static Server server = Server.getInstance();
	private static DBManager db;

	public RequestProcessor(RequestManager requestManager){
		this.requestManager = requestManager;
		try {
			db = DBManager.getInstance();
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
	public Response visit(AccountCreationRequest request) {
		if (requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "Already logged in");
		Optional<Player> player;
		try {
			player = db.players().findOneByPseudo(request.getUsername());
		} catch (SQLException e) {
			player = Optional.empty();
		}
		return player.map(p -> (Response) new ErrorResponse(request.getID(), "Account name already exists !"))
				.orElseGet(()->{
					Player newPlayer = new Player(request.getUsername(), request.getPassword());
					try {
						db.players().create(newPlayer);
					} catch (SQLException e) {
						return new ErrorResponse(request.getID(), "Error while creating the user");
					}
					return new SuccessResponse(request.getID(), "Account successfully created !");
				});
	}

	@Override
	public Response visit(LoginRequest request) {
		if (requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "Already logged in");
		Optional<Player> player;
		try {
			player = db.players().findOneByPseudo(request.getUsername());
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
			return new ArenasResponse(request.getID(), db.arenas().findAll());
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

		if(server.getRoomSessions().stream().filter(session -> session.getRoom().getName().equals(request.getName())).findFirst().isPresent())
			return new ErrorResponse(request.getID(), "This name is already used!");


		Optional<Arena> arena;
		try {
			arena = db.arenas().find(request.getArena());
		} catch (SQLException e) {
			arena = Optional.empty();
		}

		return arena.filter(a-> a .getId() != null && a.getId().equals(request.getArena()))
				.map(a->{
					server.addRoom(new RoomSession(request.getName(), request.getPassword(), request.getMinPlayer(), a));
					server.getClients().stream().filter(client -> client.getRoomsCallback() != null).forEach(client -> client.send(new RoomsResponse(client.getRoomsCallback(), server.getRoomSessions().stream().map(r -> r.getRoom()).collect(Collectors.toList()))));
					return (Response) new SuccessResponse(request.getID(), "Room successfully created !");
				}).orElseGet(()-> new ErrorResponse(request.getID(), "Wrong credentials"));
	}

	@Override
	public Response visit(RoomsRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		requestManager.setRoomsCallback(request.getID());

		requestManager.send(new RoomsResponse(request.getID(), server.getRoomSessions().stream().map(r -> r.getRoom()).collect(Collectors.toList())));

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(JoinRoomRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if (request.getRoom() == null)
			return new NoResponse(request.getID());

		Optional<RoomSession> roomSession = server.getRoomSessions().stream().filter(r -> r.getRoom().getName().equals(request.getRoom().getName())).findFirst();

		if(!roomSession.isPresent())
			return new NoResponse(request.getID());

		if(roomSession.get().isRunning())
			return new NoResponse(request.getID());

		if(roomSession.get().getPlayers().stream().filter(playerSession -> playerSession.getPlayer().getId() == requestManager.getPlayer().getId()).findFirst().isPresent())
			return new NoResponse(request.getID());

		PlayerSession playerSession;
		if(requestManager.getPlayerSession() != null){
			playerSession = requestManager.getPlayerSession();
			playerSession.getRoomSession().removePlayer(playerSession);
			playerSession.setRoomSession(roomSession.get());
			playerSession.setReadyUuid(request.getID());
		}
		else{
			playerSession = new PlayerSession(requestManager.getPlayer(), roomSession.get(), request.getID(), requestManager);
		}

		try {
			roomSession.get().addPlayer(playerSession);
		} catch (Exception e) {
			return new NoResponse(request.getID());
		}

		requestManager.setRoomSession(roomSession.get());
		requestManager.setPlayerSession(playerSession);

		server.getClients().stream().filter(client -> client.getRoomsCallback() != null).forEach(client -> client.send(new RoomsResponse(client.getRoomsCallback(), server.getRoomSessions().stream().map(r -> r.getRoom()).collect(Collectors.toList()))));

		if(roomSession.get().getPlayers().size() >= roomSession.get().getRoom().getMinPlayer()){
			roomSession.get().getPlayers().stream().filter(player -> player.getReadyUuid() != null).forEach(player -> {
				player.getRequestManager().send(new JoinRoomResponse(player.getReadyUuid(), roomSession.get().getRoom()));
				player.setReadyUuid(null);
			});
		}

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(ReadyRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(requestManager.getRoomSession() == null || requestManager.getPlayerSession() == null)
			return new NoResponse(request.getID());

		if(!request.getState()){
			requestManager.getRoomSession().removePlayer(requestManager.getPlayerSession());

			server.getClients().stream().filter(client -> client.getRoomsCallback() != null).forEach(client -> client.send(new RoomsResponse(client.getRoomsCallback(), server.getRoomSessions().stream().map(r -> r.getRoom()).collect(Collectors.toList()))));

			requestManager.getPlayerSession().setStartUuid(null);

			return new NoResponse(request.getID());
		}

		requestManager.getPlayerSession().setStartUuid(request.getID());
		requestManager.getPlayerSession().ready(request.getState());

		if(!requestManager.getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getRoomSession().getRoom().getArena().addObserver(this);

		requestManager.getRoomSession().getPlayers().stream().filter(player -> player.getStartUuid() != null).forEach(player -> {
			player.getRequestManager().send(new ReadyResponse(player.getStartUuid(), player.getBomberman()));
			player.setStartUuid(null);
		});

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(MoveRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(requestManager.getRoomSession() == null || requestManager.getPlayerSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getRoomSession().isRunning())
			return new NoResponse(request.getID());

		if(request.getDirection() == null){
			requestManager.getPlayerSession().setMoveUuid(request.getID());
		}
		else{
			requestManager.getPlayerSession().getBomberman().move(request.getDirection());
			requestManager.getRoomSession().getPlayers().stream().filter(player -> player.getMoveUuid() != null).forEach(player -> {
				player.getRequestManager().send(new MoveResponse(player.getMoveUuid(), requestManager.getPlayerSession().getBomberman()));
			});
		}

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(AddElementRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(requestManager.getRoomSession() == null || requestManager.getPlayerSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getPlayerSession().setAddUuid(request.getID());

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(DestroyElementsRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(requestManager.getRoomSession() == null || requestManager.getPlayerSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getPlayerSession().setDestroyUuid(request.getID());

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(DropBombRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(requestManager.getRoomSession() == null || requestManager.getPlayerSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getPlayerSession().getBomberman().dropBomb();

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(EndGameRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(requestManager.getRoomSession() == null || requestManager.getPlayerSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getPlayerSession().setEndUuid(request.getID());

		return new NoResponse(request.getID());
	}

	@Override
	public void update(Observable o, Object arg) {
		Element element = (Element)arg;
		updateElement(element);
	}

	private void updateElement(Element element){
		if(requestManager.getRoomSession().getRoom().getArena().getElements().contains(element)){
			requestManager.getRoomSession().getPlayers().stream().filter(player -> player.getAddUuid() != null).forEach(player -> {
				player.getRequestManager().send(new AddElementResponse(player.getAddUuid(), element));
			});
		}
		else {
			if(element instanceof Bomberman)
				requestManager.getRoomSession().getPlayers().stream().filter(playerSession -> playerSession.getBomberman().equals(element)).forEach(playerSession -> playerSession.setRank(requestManager.getRoomSession().getPlayers().stream().filter(p -> !p.getRank().isPresent()).count()));
			requestManager.getRoomSession().getPlayers().stream().filter(player -> player.getDestroyUuid() != null).forEach(player -> {
				player.getRequestManager().send(new DestroyElementsResponse(player.getDestroyUuid(), element));
			});
		}
		if(requestManager.getRoomSession().getPlayers().stream().filter(playerSession -> !playerSession.getRank().isPresent()).count() <= 1){
			requestManager.getRoomSession().getPlayers().stream().filter(playerSession -> !playerSession.getRank().isPresent()).forEach(playerSession -> playerSession.setRank(1));
			requestManager.getRoomSession().getPlayers().stream().filter(player -> player.getDestroyUuid() != null).forEach(player -> {
				player.getRequestManager().send(new EndGameResponse(player.getEndUuid(), new Statistic(player.getRank().get())));
			});
			server.removeRoom(requestManager.getRoomSession());
			requestManager.getRoomSession().getPlayers().forEach(playerSession -> {
				playerSession.getRequestManager().setRoomSession(null);
				playerSession.getRequestManager().setPlayerSession(null);
			});

			server.getClients().stream().filter(client -> client.getRoomsCallback() != null).forEach(client -> client.send(new RoomsResponse(client.getRoomsCallback(), server.getRoomSessions().stream().map(r -> r.getRoom()).collect(Collectors.toList()))));
		}
	}
}