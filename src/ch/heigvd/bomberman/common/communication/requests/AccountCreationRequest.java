package ch.heigvd.bomberman.common.communication.requests;


import ch.heigvd.bomberman.common.communication.responses.Response;

public final class AccountCreationRequest extends Request<Boolean> {
    private String username;
    private String password;

    /**
     * Creates an AccountCreation request. The parameters should be encrypted.
     * @param username of the account
     * @param password of the account
     */
    public AccountCreationRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    @Override
    public Response<Boolean> accept(RequestVisitor visitor) {
        return visitor.visit(this);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}