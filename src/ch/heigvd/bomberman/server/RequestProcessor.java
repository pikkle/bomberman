package ch.heigvd.bomberman.server;

import ch.heigvd.bomberman.common.communication.requests.*;
import ch.heigvd.bomberman.common.communication.responses.*;
import ch.heigvd.bomberman.common.game.Player;
import ch.heigvd.bomberman.server.database.PlayerORM;

import java.sql.SQLException;

public class RequestProcessor implements RequestVisitor {
	private static RequestProcessor instance = new RequestProcessor();
	private static Server server = Server.getInstance();

	private RequestProcessor(){	}

	public static RequestVisitor getInstance() {
		return instance;
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
		try {
			PlayerORM db = PlayerORM.getInstance();
			Player p = db.findOneByPseudo(request.getUsername());
			if (p == null){
				System.out.println("Creating a new entry in db");
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
	public Response visit(LoginRequest loginRequest) {
		System.out.println("Connection from user");
		System.out.println("Username: " + loginRequest.getUsername());
		System.out.println("Password: " + loginRequest.getPassword());
		return new SuccessResponse(loginRequest.getID(), "Connection successful");
	}

}