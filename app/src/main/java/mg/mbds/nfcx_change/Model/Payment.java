package mg.mbds.nfcx_change.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Payment extends ClassMapTable{

    @JsonProperty("_id")
    String id;
    @JsonProperty("datePayment")
    Date datePayment;
    @JsonProperty("serviceRequest")
    ServiceRequest servicerequest;
    @JsonProperty("label")
    private
    String label;

    public ServiceRequest getServicerequest() {
        return servicerequest;
    }

    public void setServicerequest(ServiceRequest servicerequest) {
        this.servicerequest = servicerequest;
    }

    public Payment(){
    }
    public String getId() {
        return this.id;
    }
    public void setId(String value) {
        this.id = value;
    }
    public Date getDatePayment() {
        return this.datePayment;
    }
    public void setDatePayment(Date value) {
        this.datePayment = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
