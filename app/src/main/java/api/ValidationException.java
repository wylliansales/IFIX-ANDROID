package api;

import com.google.gson.JsonObject;

public class ValidationException {

    private int code;
    private String error;
    private String message;
    private JsonObject error_description;



    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the error_description
     */
    public JsonObject getError_description() {
        return error_description;
    }

    /**
     * @param error_description the error_description to set
     */
    public void setError_description(JsonObject error_description) {
        this.error_description = error_description;
    }
}
