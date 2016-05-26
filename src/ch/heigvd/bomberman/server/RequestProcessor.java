package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.ErrorResponse;
import ch.heigvd.bomberman.common.communication.responses.HelloResponse;
import ch.heigvd.bomberman.common.communication.responses.Response;
import ch.heigvd.bomberman.common.communication.responses.SuccessResponse;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.server.database.DBManager;
import ch.heigvd.bomberman.server.database.PlayerORM;

import java.sql.SQLException;

public class RequestProcessor implements RequestVisitor {
	private RequestManager requestManager;
	private static Server server = Server.getInstance();
	private static PlayerORM db;

	public RequestProcessor(RequestManager requestManager) {
		this.requestManager = requestManager;
		try {
			db = DBManager.getInstance().getOrm(PlayerORM.class);
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
		try {
			Player p = db.findOneByPseudo(request.getUsername());
			if (p == null){
				System.out.println("Creating a new entry in db");
				System.out.println("Username: " + request.getUsername());
				System.out.println("Password: " + request.getPassword());
				p = new Player(request.getUsername(), request.getPassword());
				db.create(p);
				return new SuccessResponse(request.getID(), "Successfully created a new account !");
			} else {
				return new ErrorResponse(request.getID(), "Account name already exists !");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ErrorResponse(request.getID(), "Error with the server database !");
	}

	@Override
	public Response visit(LoginRequest request) {
		if (requestManager.isLoggedIn())
			return new ErrorResponse(request.getID(), "Already logged in");
		System.out.println("Connection from user");
		System.out.println("Username: " + request.getUsername());
		System.out.println("Password: " + request.getPassword());
		try {
			Player p = db.findOneByPseudo(request.getUsername());
			if (p.getPassword().equals(request.getPassword())){
				requestManager.setLoggedIn(true);
				requestManager.setPlayer(p);
				return new SuccessResponse(request.getID(), "Successfully logged in !");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ErrorResponse(request.getID(), "Wrong credentials");
	}

}