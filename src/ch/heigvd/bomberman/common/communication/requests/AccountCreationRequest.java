package ch.heigvd.bomberman.common.communication.requests;


import ch.heigvd.bomberman.common.communication.Message;
import ch.heigvd.bomberman.common.communication.responses.Response;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class AccountCreationRequest extends Request<Message> {
    private String username;
    private String password;

    /**
     * Creates an AccountCreation request. The parameters should be encrypted.
     * @param username of the account
     * @param password of the account
     */
    public AccountCreationRequest(String username, String password){
        this.username = username;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            this.password = new String(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Response<Message> accept(RequestVisitor visitor) {
        return visitor.visit(this);
    }
}