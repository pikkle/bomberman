package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.*;
import ch.heigvd.bomberman.common.game.Arena.Arena;
import ch.heigvd.bomberman.server.database.DBManager;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class RequestProcessor implements RequestVisitor, Observer{
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

	@Override
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
	public Response visit(PlayerRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		return new PlayerResponse(request.getID(), requestManager.getPlayer());
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
	public Response visit(SaveArenaRequest request) {
		System.out.println(request.getArena());
		if (!requestManager.isLoggedIn() || !requestManager.getPlayer().isAdmin())
			return new ErrorResponse(request.getID(), "Permission denied");

		try {
			db.arenas().createOrUpdate(request.getArena());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new SuccessResponse(request.getID(), "Arean successfully saved");
	}

	@Override
	public Response visit(RemoveArenaRequest request) {
		if (!requestManager.isLoggedIn() || !requestManager.getPlayer().isAdmin())
			return new ErrorResponse(request.getID(), "Permission denied");

		try {
			db.arenas().delete(request.getArena());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new SuccessResponse(request.getID(), "Arean successfully removed");
	}

	@Override
	public Response visit(CreateRoomRequest request) {
		if (!requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "You must be logged !");

		if(request.getName() == null || request.getName().isEmpty() || request.getMinPlayer() < 2 || request.getMinPlayer() > 4)
			return new ErrorResponse(request.getID(), "Some fields are wrong or are missing !");

		if(server.getRoomSessions().stream().filter(session -> session.getName().equals(request.getName())).findFirst().isPresent())
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
					return (Response) new SuccessResponse(request.getID(), "Room successfully created !");
				}).orElseGet(()-> new ErrorResponse(request.getID(), "Wrong credentials"));
	}

	@Override
	public Response visit(RoomsRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		requestManager.setRoomsCallback(request.getID());

		server.sendRooms();

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(JoinRoomRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if (request.getRoom() == null)
			return new NoResponse(request.getID());

		Optional<RoomSession> roomSession = server.getRoomSessions().stream().filter(r -> r.getName().equals(request.getRoom().name())).findFirst();

		if(!roomSession.isPresent())
			return new NoResponse(request.getID());

		if(roomSession.get().isRunning())
			return new NoResponse(request.getID());

		if(roomSession.get().getPlayers().stream().filter(playerSession -> playerSession.getPlayer().id() == requestManager.getPlayer().id()).findFirst().isPresent())
			return new NoResponse(request.getID());

		if(roomSession.get().getPassword() != null && !roomSession.get().getPassword().isEmpty() && !roomSession.get().getPassword().equals(request.getPassword()))
			return new NoResponse(request.getID());

		try {
			requestManager.openPlayerSession(roomSession.get(), request.getID());
		} catch (Exception e) {
			return new NoResponse(request.getID());
		}

		if(roomSession.get().getPlayers().size() >= roomSession.get().getMinPlayer()){
			roomSession.get().getPlayers().stream().filter(player -> player.getReadyUuid() != null).forEach(player -> {
				player.getRequestManager().send(new JoinRoomResponse(player.getReadyUuid(), new Room(roomSession.get().getName(), roomSession.get().getPassword() != null && ! roomSession.get().getPassword().isEmpty(), roomSession.get().getMinPlayer(), roomSession.get().getPlayers().size(), roomSession.get().getArena(), roomSession.get().getPlayers().contains(player))));
				player.setReadyUuid(null);
			});
		}

		return new NoResponse(request.getID());
	}

	// Game requests, allowed
	@Override
	public Response visit(ReadyRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().isPresent() || requestManager.getPlayerSession().get().getRoomSession() == null)
			return new NoResponse(request.getID());

		if(!request.getState()){
			//if(requestManager.getPlayerSession().get().getRoomSession().isRunning())
				//requestManager.getPlayerSession().get().getRoomSession().arena().destroy(requestManager.getPlayerSession().get().getBomberman());

			requestManager.closePlayerSession();

			return new NoResponse(request.getID());
		}

		requestManager.getPlayerSession().get().setStartUuid(request.getID());
		requestManager.getPlayerSession().get().ready(request.getState());

		if(!requestManager.getPlayerSession().get().getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getPlayerSession().get().getRoomSession().getArena().addObserver(this);

		requestManager.getPlayerSession().get().getRoomSession().getPlayers().stream().filter(player -> player.getStartUuid() != null).forEach(player -> {
			player.getRequestManager().send(new ReadyResponse(player.getStartUuid(), player.getBomberman()));
			player.setStartUuid(null);
		});

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(MoveRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().isPresent() || requestManager.getPlayerSession().get().getRoomSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().get().getRoomSession().isRunning())
			return new NoResponse(request.getID());

		if(request.getDirection() == null){
			requestManager.getPlayerSession().get().setMoveUuid(request.getID());
		}
		else{
			requestManager.getPlayerSession().get().getBomberman().move(request.getDirection());
			requestManager.getPlayerSession().get().getRoomSession().getPlayers().stream().filter(player -> player.getMoveUuid() != null).forEach(player -> {
				player.getRequestManager().send(new MoveResponse(player.getMoveUuid(), requestManager.getPlayerSession().get().getBomberman()));
			});
		}

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(AddElementRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().isPresent() || requestManager.getPlayerSession().get().getRoomSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().get().getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getPlayerSession().get().setAddUuid(request.getID());

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(DestroyElementsRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().isPresent() ||requestManager.getPlayerSession().get().getRoomSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().get().getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getPlayerSession().get().setDestroyUuid(request.getID());

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(DropBombRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().isPresent() || requestManager.getPlayerSession().get().getRoomSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().get().getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getPlayerSession().get().getBomberman().dropBomb();

		return new NoResponse(request.getID());
	}

	@Override
	public Response visit(EndGameRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().isPresent() || requestManager.getPlayerSession().get().getRoomSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().get().getRoomSession().isRunning())
			return new NoResponse(request.getID());

		requestManager.getPlayerSession().get().setEndUuid(request.getID());

		return new NoResponse(request.getID());
	}

	@Override
	public void update(Observable o, Object arg) {
		Element element = (Element)arg;
		updateElement(element);
	}

	private void updateElement(Element element){
		if(!requestManager.getPlayerSession().isPresent() || requestManager.getPlayerSession().get().getRoomSession() == null)
			return;
		if(requestManager.getPlayerSession().get().getRoomSession().getArena().elements().contains(element)){
			requestManager.getPlayerSession().get().getRoomSession().getPlayers().stream().filter(player -> player.getAddUuid() != null).forEach(player -> {
				player.getRequestManager().send(new AddElementResponse(player.getAddUuid(), element));
			});
		}
		else {
			if(element instanceof Bomberman) {
				requestManager.getPlayerSession().get().getRoomSession().getPlayers().stream().filter(playerSession -> playerSession.getBomberman().equals(element)).findFirst().ifPresent(playerSession -> playerSession.close());
			}
			requestManager.getPlayerSession().get().getRoomSession().getPlayers().stream().filter(player -> player.getDestroyUuid() != null).forEach(player -> {
				player.getRequestManager().send(new DestroyElementsResponse(player.getDestroyUuid(), element));
			});
		}
		checkIfEnded();
	}

	private void checkIfEnded() {
		if(!requestManager.getPlayerSession().isPresent() || requestManager.getPlayerSession().get().getRoomSession() == null)
			return;
		if(requestManager.getPlayerSession().get().getRoomSession().getPlayers().stream().filter(playerSession -> !playerSession.getRank().isPresent()).count() <= 1){

			requestManager.getPlayerSession().get().getRoomSession().close();
		}
	}
}