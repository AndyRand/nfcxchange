package mg.mbds.nfcx_change.Model;

import android.app.Activity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class ClassMapTable{

    @JsonProperty("data")
    @JsonIgnoreProperties(ignoreUnknown=true)
    private Object data;

    @JsonProperty("success")
    @JsonIgnoreProperties(ignoreUnknown=true)
    private boolean success;

    @JsonProperty("message")
    @JsonIgnoreProperties(ignoreUnknown=true)
    private String message;

    @JsonProperty("__v")
    @JsonIgnoreProperties(ignoreUnknown=true)
    private int version;

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


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
