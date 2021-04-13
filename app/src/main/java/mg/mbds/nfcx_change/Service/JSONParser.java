package mg.mbds.nfcx_change.Service;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 21/06/2016.
 */
public class JSONParser {

    private String response;
    public static final int GET = 1;
    public static final int POST = 2;

    public String makeRequest (String url, int requestType, HashMap<String,Object> params, JSONObject jsonObject) {

        // Object URL
        URL urlObject;
        // Response
        String response="";

        try {
            // Create a new URL Object from the url String
            urlObject = new URL (url);
            // Open the connection to that URL
            HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection();
            if(jsonObject != null)
                connection.setRequestProperty("Content-Type", "application/json");

                // Setting the connection's properties
            connection.setReadTimeout(15001);
            connection.setConnectTimeout(15001);
            if(requestType == POST) {
                connection.setDoInput(true);
                connection.setDoOutput(true);
            }
            
            // Check the request type and change the connection type
            if (requestType == POST)
                connection.setRequestMethod("POST");
            else
                connection.setRequestMethod("GET");

            // If there are parameters
            if (params != null) {
                // Create an object outputstream from the connection
                if(requestType == POST) {
                    OutputStream outputStream = connection.getOutputStream();
                    // Create BufferedWriter to write the HTTP request
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    // Create string builder to write the http request
                    StringBuilder request = new StringBuilder();
                    // Boolean to indcate if it is the first value
                    boolean first = true;
                    // Loop the parameters
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        // First entry ?
                        if (first)
                            first = false;
                        else
                            // Construct the query string
                            request.append("&");

                        request.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                        request.append("=");
                        request.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));


                    }
                    if(jsonObject != null){
                        //connection.setRequestProperty("Content-Type", "application/json");

                        bufferedWriter.write(jsonObject.toString());

                    }
                    else {
                        //Log.e("suite ",request.toString());
                        // Write the request string
                        bufferedWriter.write(request.toString());
                    }
                    // Release all resources
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                }

                // Get the reposponse returned
                int responseCode = connection.getResponseCode();
                // If OK
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // String to get each line
                    String line = "";
                    // To read the response
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    // Read the response
                    while((line = bufferedReader.readLine()) != null) {
                        // Concat into the string response
                        response += line;
                        //Log.e("suite ",response);
                    }
                }
                else
                    response ="";
            }

        } catch (MalformedURLException e) {
            Log.e("ERREUR URL",e.getMessage());
        } catch (IOException e) {
            Log.e("ERREUR IO",e.getMessage());
        }catch (Exception e) {
            Log.e("ERREUR",e.getMessage());
        }
        return response;
    }


    private static void sendGET(String GET_URL) throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }

    }

    private static void sendPOST(String POST_URL) throws IOException {
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        //os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }


}
