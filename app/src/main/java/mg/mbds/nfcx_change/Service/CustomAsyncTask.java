package mg.mbds.nfcx_change.Service;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

/**
 * Created by BillySycher on 19/02/2019.
 */

public class CustomAsyncTask extends AsyncTask<String,String,Object> {
    private GenericAsyncTask genericAsyncTask;
    private HashMap<String,Object> parameter;
    private int type;
    private String url;
    private ClassMapTable classMapTable;
    private JSONObject jsonObject;

    public CustomAsyncTask(GenericAsyncTask genericAsyncTask,String url, HashMap<String,Object> parameter,JSONObject jsonObject, int type,ClassMapTable classMapTable){
        setGenericAsyncTask(genericAsyncTask);
        setParameter(parameter);
        setType(type);
        setUrl(url);
        setClassMapTable(classMapTable);
        setJsonObject(jsonObject);
    }

    @Override
    protected Object doInBackground(String... params) {
        try {
            String response = response();

            return Utilitaire.isJSONValid(response);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("erreur doInBackground: ",e.getMessage());
        }
        return null;

    }
    @Override
    public void onPostExecute(Object json){
        if(json != null) {
            try {

                List<ClassMapTable> result = new ArrayList<>();
                ObjectMapper objectMapper = new ObjectMapper();
                if(json instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) json;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try{
                            setClassMapTable(objectMapper.readValue(String.valueOf(jsonArray.get(i)), getClassMapTable().getClass()));
                            result.add(getClassMapTable());
                        }
                        catch (Exception e){
                            continue;
                        }

                    }
                }
                else if(json instanceof JSONObject){
                    JSONObject jsonObject = (JSONObject) json;
                    getClassMapTable().setSuccess(jsonObject.getBoolean("success"));
                    if(getClassMapTable().isSuccess()) {
                        Object data = null;
                        if(jsonObject.has("data")) {
                            data = jsonObject.get("data");
                        }

                        if (data != null) {

                            if (data instanceof JSONObject) {
                                try{
                                    setClassMapTable(objectMapper.readValue(String.valueOf(data), getClassMapTable().getClass()));
                                    getClassMapTable().setSuccess(jsonObject.getBoolean("success"));
                                    result.add(getClassMapTable());
                                }
                                catch (Exception e){

                                }


                            } else if (data instanceof JSONArray) {
                                JSONArray jsonArray = (JSONArray) data;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try{
                                        setClassMapTable(objectMapper.readValue(String.valueOf(jsonArray.get(i)), getClassMapTable().getClass()));
                                        result.add(getClassMapTable());
                                    }
                                    catch (Exception e){
                                        continue;
                                    }

                                }
                            }
                        } else {
                            try{
                                setClassMapTable(objectMapper.readValue(String.valueOf(jsonObject), getClassMapTable().getClass()));
                                result.add(getClassMapTable());
                            }
                            catch (Exception e){

                            }

                        }
                    }else {
                        try{
                            setClassMapTable(objectMapper.readValue(String.valueOf(jsonObject), getClassMapTable().getClass()));
                            result.add(getClassMapTable());
                        }
                        catch (Exception e){

                        }

                    }
                }

                getGenericAsyncTask().execute(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String response(){
        return new JSONParser().makeRequest(getUrl(), getType(),getParameter(),getJsonObject());
    }



    public GenericAsyncTask getGenericAsyncTask() {
        return genericAsyncTask;
    }

    public void setGenericAsyncTask(GenericAsyncTask genericAsyncTask) {
        this.genericAsyncTask = genericAsyncTask;
    }

    public HashMap<String, Object> getParameter() {
        return parameter;
    }

    public void setParameter(HashMap<String, Object> parameter) {
        this.parameter = parameter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ClassMapTable getClassMapTable() {
        return classMapTable;
    }

    public void setClassMapTable(ClassMapTable classMapTable) {
        this.classMapTable = classMapTable;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
