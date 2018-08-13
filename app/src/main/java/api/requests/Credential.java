package api.requests;

public class Credential {

    private String grant_type;
    private int client_id;
    private String client_secret;
    private String username;
    private String password;
    private String scope;

    public Credential(String grant_type, int client_id, String client_secret, String username, String password, String scope){
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.username = username;
        this.password = password;
        this.scope = scope;
    }

    /**
     * @return the grant_type
     */
    public String getGrant_type() {
        return grant_type;
    }

    /**
     * @return the client_id
     */
    public int getClient_id() {
        return client_id;
    }

    /**
     * @return the client_secret
     */
    public String getClient_secret() {
        return client_secret;
    }

    /**
     * @return the scope
     */
    public String getScope() {
        return scope;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
