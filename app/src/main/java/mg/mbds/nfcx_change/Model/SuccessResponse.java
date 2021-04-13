package mg.mbds.nfcx_change.Model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessResponse extends ClassMapTable {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
