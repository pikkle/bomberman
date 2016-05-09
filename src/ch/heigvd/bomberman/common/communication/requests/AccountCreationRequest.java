package ch.heigvd.bomberman.common.communication.requests;


public final class AccountCreationRequest extends Request {
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
    public RequestType getType() {
        return RequestType.ACCOUNT_CREATION;
    }
}
