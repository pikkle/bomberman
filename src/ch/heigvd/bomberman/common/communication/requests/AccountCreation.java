package ch.heigvd.bomberman.common.communication.requests;


public final class AccountCreation extends Request {
    private String username;
    private String password;

    /**
     * Creates an AccountCreation request. The parameters should be encrypted.
     * @param username of the account
     * @param password of the account
     */
    public AccountCreation(String username,  String password){
        super(Type.ACCOUNT_CREATION);
        this.username = username;
        this.password = password;
    }
}
