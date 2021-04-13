package mg.mbds.nfcx_change.Utilitaire;

/**
 * Created by BillySycher on 06/04/2017.
 */

public class Url_Base {

    public static String IMAGE = "";
    public static final String URL = "https://nfc-x-change-web.herokuapp.com";
    public static final String URL_image = "http://" + IMAGE;
    public static final String URL_LOGIN = URL + "/api/user/login";
    public static final String URL_TYPE_SERVICE = URL + "/api/type-of-services";
    public static final String URL_SERVICES = URL + "/api/products-and-services";
    public static final String URL_SERVICES_OTHER = URL + "/api/products-and-services/getAvailableProducts/%s";
    public static final String URL_SERVICES_BY_ID = URL + "/api/products-and-services/%s";
    public static final String URL_TOP_FIVE = URL + "/api/products-and-services/getTopFive/%s/%s";
    public static final String URL_SERVICE_BY_OWNER = URL + "/api/products-and-services/getProductsByOwner/%s";
    public static final String URL_UPDATE_SERVICE = URL + "/api/products-and-services/update/%s";
    public static final String URL_CREATE_REQUEST = URL + "/api/request";
    public static final String URL_GET_REQUEST_BY_ID = URL + "/api/request/%s";
    public static final String URL_REQUEST_BY_USER_AND_TPE = URL + "/api/request/getRequestsByTypeRequest/%s/%s";
    public static final String URL_REQUEST_NOT_ACCEPT = URL + "/api/request/getRequestsNotAccept/%s";
    public static final String URL_VALIDATE_DEMANDE = URL + "/api/request/updateRequestMultiple";
    public static final String URL_XCHANGE = URL + "/api/payments/exchange";
    public static final String URL_BUY = URL + "/api/payments/buy";
    public static final String URL_USER_BY_ID = URL + "/api/user/%s";
    public static final String URL_SEARCH = URL + "/api/products-and-services/search";

}
