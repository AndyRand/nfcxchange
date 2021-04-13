package mg.mbds.nfcx_change.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypeService extends ClassMapTable{

    @JsonProperty("_id")
    private String id;

    /** Property typeService */
    @JsonProperty("designation")
    String typeService;

    /**
     * Constructor
     */
    public TypeService() {
    }

    /**
     * Gets the typeService
     */
    public String getTypeService() {
        return this.typeService;
    }

    /**
     * Sets the typeService
     */
    public void setTypeService(String value) {
        this.typeService = value;
    }

    /** Property id */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
