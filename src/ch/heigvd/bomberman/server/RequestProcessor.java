package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.Arena;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.common.game.Room;
import ch.heigvd.bomberman.common.game.StartPoint;
import ch.heigvd.bomberman.common.game.bombs.Bomb;
import ch.heigvd.bomberman.server.database.DBManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The processor of the requests received by the client. Implements the visitor pattern.
 */
public class RequestProcessor implements RequestVisitor{
	private static Log logger = LogFactory.getLog(RequestProcessor.class);
	private RequestManager requestManager;
	private static Server server = Server.getInstance();
	private static DBManager db;

	/**
	 * Constructs the request processor.
	 * @param requestManager the request manager caller.
     */
	public RequestProcessor(RequestManager requestManager){
		this.requestManager = requestManager;
		db = DBManager.getInstance();
	}

	/**
	 * Processes a Hello Request.
	 * @param request the HelloRequest received
	 * @return an HelloResponse
     */
	@Override
	public Response visit(HelloRequest request){
		System.out.println("Received message: ");
		System.out.println(request.getMessage());
		return new HelloResponse(request.getID(), "Hello !");
	}
	/**
	 * Processes an AccountCreationRequest.
	 * @param request the HelloRequest received
	 * @return an ErrorResponse or a SuccessResponse
	 */
	@Override
	public Response visit(AccountCreationRequest request) {
		if (requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "Already logged in");
		Optional<Player> player = db.players().findOneByPseudo(request.getUsername());
		return player.map(p -> (Response) new ErrorResponse(request.getID(), "Account name already exists !"))
				.orElseGet(()->{
					Player newPlayer = new Player(request.getUsername(), request.getPassword());
					db.players().create(newPlayer);
					return new SuccessResponse(request.getID(), "Account successfully created !");
				});
	}

	/**
	 * Processes an AccountModifyRequest.
	 * @param request the AccountModifyRequest received
	 * @return an ErrorResponse or a SuccessResponse
	 */
	@Override
	public Response visit(AccountModifyRequest request) {
		if (!requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "You are not logged in");
		if((request.getUsername() == null || request.getUsername().isEmpty()) && (request.getPassword() == null ||
				request.getPassword().isEmpty()))
			return new ErrorResponse(request.getID(), "Wrong values");

		if(request.getUsername() != null && !request.getUsername().isEmpty()){
			Optional<Player> player = db.players()
			                            .findOneByPseudo(request.getUsername())
			                            .filter(p -> !p.id().equals(requestManager.getPlayer().id()));
			if(player.isPresent())
				return new ErrorResponse(request.getID(), "Account name already exists !");
			requestManager.getPlayer().setPseudo(request.getUsername());
		}

		if(request.getPassword() != null && !request.getPassword().isEmpty()){
			requestManager.getPlayer().setPassword(request.getPassword());
		}

		db.players().update(requestManager.getPlayer());
		return new SuccessResponse(request.getID(), "Account successfully updated !");
	}

	/**
	 * Processes a LoginRequest.
	 * @param request the LoginRequest received
	 * @return an ErrorResponse or a SuccessResponse
	 */
	@Override
	public Response visit(LoginRequest request) {
		if (requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "Already logged in");
		Optional<Player> player;
		player = db.players().findOneByPseudo(request.getUsername());
		return player.filter(p->p.getPassword().equals(request.getPassword()))
				.map(p->{
					requestManager.setLoggedIn(true);
					requestManager.setPlayer(p);
					return (Response) new SuccessResponse(request.getID(), "Successfully logged in !");
				}).orElseGet(()-> new ErrorResponse(request.getID(), "Wrong credentials"));
	}

	/**
	 * Processes a PlayerRequest.
	 * @param request the PlayerRequest received
	 * @return a NoResponse or a PlayerResponse
	 */
	@Override
	public Response visit(PlayerRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		return new PlayerResponse(request.getID(), requestManager.getPlayer());
	}

	/**
	 * Processes an ArenasRequest.
	 * @param request the ArenasRequest received
	 * @return an ArenasResponse
	 */
	@Override
	public Response visit(ArenasRequest request) {
		if (!requestManager.isLoggedIn())
			return new ArenasResponse(request.getID(), null);
		return new ArenasResponse(request.getID(), db.arenas().findAll());
	}

	/**
	 * Processes a SaveArenaRequest.
	 * @param request the SaveArenaRequest received
	 * @return an ErrorResponse or a SuccessResponse
	 */
	@Override
	public Response visit(SaveArenaRequest request) {
		if (!requestManager.isLoggedIn() || !requestManager.getPlayer().isAdmin())
			return new ErrorResponse(request.getID(), "Permission denied");

		if (request.getArena().elements(StartPoint.class).size() < 4)
			return new ErrorResponse(request.getID(), "You have to specify 4 start points");

		db.arenas().createOrUpdate(request.getArena());
		return new SuccessResponse(request.getID(), "Arean successfully saved");
	}

	/**
	 * Processes a RemoveArenaRequest.
	 * @param request the RemoveArenaRequest received
	 * @return an ErrorResponse or a SuccessResponse
	 */
	@Override
	public Response visit(RemoveArenaRequest request) {
		if (!requestManager.isLoggedIn() || !requestManager.getPlayer().isAdmin())
			return new ErrorResponse(request.getID(), "Permission denied");

		db.arenas().delete(request.getArena());
		return new SuccessResponse(request.getID(), "Arean successfully removed");
	}

	/**
	 * Processes a CreateRoomRequest.
	 * @param request the CreateRoomRequest received
	 * @return an ErrorResponse or a SuccessResponse
	 */
	@Override
	public Response visit(CreateRoomRequest request) {
		if (!requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "You must be logged !");

		if(request.getName() == null || request.getName().isEmpty() || request.getMinPlayer() < 2 || request.getMinPlayer() > 4)
			return new ErrorResponse(request.getID(), "Some fields are wrong or are missing !");

		if(server.getRoomSessions().stream().filter(session -> session.getName().equals(request.getName())).findFirst().isPresent())
			return new ErrorResponse(request.getID(), "This name is already used!");


		Optional<Arena> arena = db.arenas().find(request.getArena());

		return arena.filter(a-> a .getId() != null && a.getId().equals(request.getArena()))
				.map(a->{
					server.addRoom(new RoomSession(request.getName(), request.getPassword(), request.getMinPlayer(),
					                               a, requestManager.getPlayer()));
					return (Response) new SuccessResponse(request.getID(), "Room successfully created !");
				}).orElseGet(()-> new ErrorResponse(request.getID(), "Wrong credentials"));
	}

	/**
	 * Processes a RoomsRequest.
	 * @param request the RoomsRequest received
	 * @return a NoResponse
	 */
	@Override
	public Response visit(RoomsRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		requestManager.setRoomsCallback(request.getID());

		server.sendRooms();

		return new NoResponse(request.getID());
	}

	/**
	 * Processes a RoomsRequest.
	 * @param request the RoomsRequest received
	 * @return a NoResponse
	 */
	@Override
	public Response visit(JoinRoomRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if (request.getRoom() == null)
			return new NoResponse(request.getID());

		Optional<RoomSession> roomSession = server.getRoomSessions()
		                                                      .stream()
		                                                      .filter(r -> r.getName().equals(request.getRoom()))
		                                                      .findFirst();

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
				player.getRequestManager().send(new JoinRoomResponse(player.getReadyUuid(), new Room(roomSession.get().getName(), roomSession.get().getPassword() != null && ! roomSession.get().getPassword().isEmpty(), roomSession.get().getMinPlayer(), roomSession.get().getPlayers().stream().map(p -> p.getPlayer().getPseudo()).collect(Collectors.toList()), roomSession.get().getArena(), roomSession.get().getPlayers().contains(player))));
				player.setReadyUuid(null);
			});
		}

		return new NoResponse(request.getID());
	}

	/**
	 * Processes a ReadyRequest.
	 * @param request the ReadyRequest received
	 * @return a NoResponse
	 */
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

		requestManager.getPlayerSession().get().getRoomSession().getPlayers().stream().filter(player -> player.getStartUuid() != null).forEach(player -> {
			player.getRoomSession().getArena().addObserver(player.getRequestManager());
			player.getRequestManager().send(new ReadyResponse(player.getStartUuid(), player.getBomberman()));
			player.setStartUuid(null);
		});

		return new NoResponse(request.getID());
	}

	/**
	 * Processes a MoveRequest.
	 * @param request the MoveRequest received
	 * @return a NoResponse
	 */
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

	/**
	 * Processes an AddElementRequest.
	 * @param request the AddElementRequest received
	 * @return a NoResponse
	 */
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

	/**
	 * Processes a DestroyElementsRequest.
	 * @param request the DestroyElementsRequest received
	 * @return a NoResponse
	 */
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

	/**
	 * Processes a DropBombRequest.
	 * @param request the DropBombRequest received
	 * @return a NoResponse
	 */
	@Override
	public Response visit(DropBombRequest request) {
		if (!requestManager.isLoggedIn())
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().isPresent() || requestManager.getPlayerSession().get().getRoomSession() == null)
			return new NoResponse(request.getID());

		if(!requestManager.getPlayerSession().get().getRoomSession().isRunning())
			return new NoResponse(request.getID());

		Optional<? extends Bomb> bomb = requestManager.getPlayerSession().get().getBomberman().dropBomb();
		bomb.ifPresent(c -> {
			bomb.get().addObserver(requestManager);
			requestManager.getPlayerSession().get().getStatistic().dropBomb();
		});

		return new NoResponse(request.getID());
	}

	/**
	 * Processes an EndGameRequest.
	 * @param request the EndGameRequest received
	 * @return a NoResponse
	 */
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
}