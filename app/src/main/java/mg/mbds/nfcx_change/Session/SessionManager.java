package mg.mbds.nfcx_change.Session;

import android.content.Context;
import android.content.SharedPreferences;


public class SessionManager {
    //method to save status
    public void setPreferences(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences("Session", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
    public String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("Session",Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }

    public void remove(Context context,String key){
        SharedPreferences prefs = context.getSharedPreferences("Session",Context.MODE_PRIVATE);
        prefs.edit().remove(key).commit();
    }
}
