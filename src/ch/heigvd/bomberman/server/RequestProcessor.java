package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.server.database.PlayerORM;

import java.sql.SQLException;
import java.util.Optional;

public class RequestProcessor implements RequestVisitor {
	private RequestManager requestManager;
	private static Server server = Server.getInstance();
	private static PlayerORM db = server.getDatabase();

	public RequestProcessor(RequestManager requestManager){
		this.requestManager = requestManager;
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
}
