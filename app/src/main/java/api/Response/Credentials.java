package api.Response;

public class Credentials {

    private String grant_type;
    private int client_id;
    private String client_secret;
    private String scope;

    public Credentials(String grant_type, int client_id, String client_secret, String scope){
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.client_secret = client_secret;
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


}
