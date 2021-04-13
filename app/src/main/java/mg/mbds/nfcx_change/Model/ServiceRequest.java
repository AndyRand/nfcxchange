package mg.mbds.nfcx_change.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ServiceRequest extends ClassMapTable{
    @JsonProperty("_id")
    String id;
    @JsonProperty("dateRequest")
    Date dateRequest;
    @JsonProperty("statusPayment")
    private boolean statusPayment;
    @JsonProperty("statusAccept")
    private boolean statusAccept;
    @JsonProperty("typeRequest")
    private int typeRequest;
    @JsonProperty("utilisateur")
    Utilisateur utilisateur;
    @JsonProperty("service")
    Service service;
    @JsonProperty("serviceXchange")
    private Service serviceXchange;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


    public ServiceRequest() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public Date getDateRequest() {
        return this.dateRequest;
    }

    public void setDateRequest(Date value) {
        this.dateRequest = value;
    }

    public boolean isStatusAccept() {
        return statusAccept;
    }

    public void setStatusAccept(boolean statusAccept) {
        this.statusAccept = statusAccept;
    }

    public Service getServiceXchange() {
        return serviceXchange;
    }

    public void setServiceXchange(Service serviceXchange) {
        this.serviceXchange = serviceXchange;
    }

    public boolean isStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(boolean statusPayment) {
        this.statusPayment = statusPayment;
    }

    public int getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(int typeRequest) {
        this.typeRequest = typeRequest;
    }
}
