package mg.mbds.nfcx_change.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 *
 * @author christophelai
 */
public class Service extends ClassMapTable{

    /** Property id */
    @JsonProperty("_id")
    String id;

    /** Property serviceName */
    @JsonProperty("name")
    String serviceName;

    @JsonProperty("description")
    private
    String serviceDescription;

    /** Property status */
    @JsonProperty("status")
    boolean status;

    /** Property creationDate */
    @JsonProperty("creationDate")
    Date creationDate;

    /** Property publicationDate */
    @JsonProperty("publicationDate")
    Date publicationDate;

    @JsonProperty("user")
    Utilisateur utilisateur;

    @JsonProperty("price")
    private double price;

    @JsonProperty("imageUrl")
    private String serviceImgUrl;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @JsonProperty("typeService")
    TypeService typeservice;

    public TypeService getTypeservice() {
        return typeservice;
    }

    public void setTypeservice(TypeService typeservice) {
        this.typeservice = typeservice;
    }

    /**
     * Constructor
     */
    public Service() {
    }

    public Service(String id) {
        setId(id);
    }

    /**
     * Gets the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the serviceName
     */
    public String getServiceName() {
        return this.serviceName;
    }

    /**
     * Sets the serviceName
     */
    public void setServiceName(String value) {
        this.serviceName = value;
    }

    /**
     * Gets the status
     */
    public boolean isStatus() {
        return this.status;
    }

    /**
     * Sets the status
     */
    public void setStatus(boolean value) {
        this.status = value;
    }

    /**
     * Gets the creationDate
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * Sets the creationDate
     */
    public void setCreationDate(Date value) {
        this.creationDate = value;
    }

    /**
     * Gets the publicationDate
     */
    public Date getPublicationDate() {
        return this.publicationDate;
    }

    /**
     * Sets the publicationDate
     */
    public void setPublicationDate(Date value) {
        this.publicationDate = value;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getServiceImgUrl() {
        return serviceImgUrl;
    }

    public void setServiceImgUrl(String serviceImgUrl) {
        this.serviceImgUrl = serviceImgUrl;
    }
}