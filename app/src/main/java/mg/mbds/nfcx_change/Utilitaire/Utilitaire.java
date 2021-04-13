package mg.mbds.nfcx_change.Utilitaire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import mg.mbds.nfcx_change.Activity.BaseActivity;
import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.ServiceRequest;
import mg.mbds.nfcx_change.Model.Utilisateur;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Session.SessionManager;

import static com.android.volley.VolleyLog.TAG;
import static mg.mbds.nfcx_change.Utilitaire.BlurBuilder.blur;

/**
 * Created by BillySycher on 07/04/2017.
 */

public class Utilitaire {
    public static String changeFirstMaj(String field){
        return field.substring(0,1).toUpperCase() + field.substring(1,field.length());
    }

    public static Date formatDateString(String date) throws ParseException {
        SimpleDateFormat textFormat = new SimpleDateFormat("yyyy-MM-dd");
        return textFormat.parse(date);
    }


    public static void showPopUpThreeDots(Activity activity, View v, int idMenu){
        PopupMenu popupMenu = new PopupMenu(activity,v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(idMenu,popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }



    public static void hideFabOnScrolling(final Activity activity, RecyclerView mRecycleView){
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && ((BaseActivity)activity).getFab().isShown())
                    ((BaseActivity)activity).getFab().hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    ((BaseActivity)activity).getFab().show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }




    public static void downloadImage(final Activity activity, final ImageView imageView, final CardView cardView, String url){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        ImageRequest imageRequest = new ImageRequest(Url_Base.URL_image + url,new Response.Listener<Bitmap>(){

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Bitmap response) {
                if(response != null) {
                    imageView.setImageBitmap(response);
                    Bitmap temp = blur(activity, response);
                    BitmapDrawable drawable = new BitmapDrawable(activity.getResources(), temp);
                    cardView.setBackgroundDrawable(drawable);
                }
            }

        },0,0,null,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });
        requestQueue.add(imageRequest);
    }

    public static void downloadImage(Activity activity, final ImageView imageView, String url){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        ImageRequest imageRequest = new ImageRequest(Url_Base.URL_image + url,new Response.Listener<Bitmap>(){

            @Override
            public void onResponse(Bitmap response) {
                if(response != null) {
                    imageView.setImageBitmap(response);
                }
            }

        },0,0,null,new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });
        requestQueue.add(imageRequest);
    }

    public static boolean isNetworkAvailable(Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


    public static boolean changeLanguage (Activity activity, String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resource = activity.getResources();
        Configuration configuration = resource.getConfiguration();
        configuration.locale = locale;
        resource.updateConfiguration(configuration, resource.getDisplayMetrics());
        Intent intent = new Intent(activity,mg.mbds.nfcx_change.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        return true;
    }

    public static boolean checkUrl(String url) throws Exception {
        URL u = new URL(url);
        HttpURLConnection huc =  (HttpURLConnection)  u.openConnection ();
        huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD");
        huc.connect () ;
        int code = huc.getResponseCode() ;
        Log.e("code","" + code);
        return true;
    }

    public static String securiser(String string) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(string.getBytes());
        byte[] mdbytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String getDateNow(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String formatDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    public static String changeFormatDate(String string){
        String[] strings = string.split("/");
        if(strings.length > 0 ) {
            try {
                return strings[2] + "/" + strings[1] + "/" + strings[0];
            }
            catch (Exception e){
                return string;
            }
        }
        else{
            try {
                strings = string.split("-");
                return strings[2] + "/" + strings[1] + "/" + strings[0];
            }
            catch (Exception e){
                return string;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void blurImage(BaseActivity baseActivity, View view,ImageView imageView){

        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();

        Bitmap temp = BlurBuilder.blur(baseActivity, bitmap);
        BitmapDrawable drawable = new BitmapDrawable(baseActivity.getResources(), temp);
        view.setBackgroundDrawable(drawable);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void blurDrawableImage(BaseActivity baseActivity, View view, int drawableImage){

        Bitmap bitmap = BitmapFactory.decodeResource(baseActivity.getResources(),drawableImage);
        Bitmap temp = BlurBuilder.blur(baseActivity, bitmap);
        BitmapDrawable drawable = new BitmapDrawable(baseActivity.getResources(), temp);
        view.setBackgroundDrawable(drawable);
    }

    public static class Cancel_popup implements View.OnClickListener{
        Dialog dialog;
        public Cancel_popup(Dialog dialog){
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    }

    public static Object isJSONValid(String test) {
        try {
            return new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                return new JSONArray(test);
            } catch (JSONException ex1) {
                return null;
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public Bitmap ByteArrayToBitmap(byte[] byteArray)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    public static void decodeBase64Image(Activity activity,ImageView imageView,String image){

        final String pureBase64Encoded = image.substring(image.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        BitmapDrawable drawable = new BitmapDrawable(activity.getResources(), decodedBitmap);
        imageView.setImageDrawable(drawable);

    }

    public static JSONObject buildAndDeleteNullJson(ClassMapTable classMapTable) throws JsonProcessingException, JSONException {
        ObjectMapper Obj = new ObjectMapper();
        // get Oraganisation object as a json string
        String jsonStr = Obj.writeValueAsString(classMapTable);
        // Displaying JSON String
        System.out.println(jsonStr);

        JSONObject jsonObject = new JSONObject(jsonStr);

        return jsonObject;
    }

    public static JSONObject deleteAllNullJson(JSONObject json) throws JSONException {
        String string = json.toString().replaceAll(".*\": null(,)?\\r\\n", "");

        return new JSONObject(string);
    }

    public static double difference(double p1,double p2){
        if(p1 >= p2){
            double x = p1 - p2;
            return Math.floor(x * 100) / 100;
        }
        else{
            double x = p2 - p1;
            return Math.floor(x * 100) / 100;
        }
    }

    public static List<ClassMapTable>[] filterRequest(List<ClassMapTable> serviceRequestList, String userId){
        List<ClassMapTable> [] result = new List[2];
        for(int i = 0 ; i<result.length;i++){
            result[i] = new ArrayList<>();
        }
        for (int i = 0;i<serviceRequestList.size();i++){
            ServiceRequest serviceRequest = (ServiceRequest) serviceRequestList.get(i);
            if(serviceRequest.getServiceXchange().getId() == null){
                result[0].add(serviceRequest);
            }
            else{
                result[1].add(serviceRequest);
            }
        }
        return result;
    }

    public static void setOnCheckBox(final Activity activity, final CheckBox checkBox, final String idServiceReq, final Utilisateur utilisateur){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((MainActivity)activity).setUtilisateur2(utilisateur);
                SessionManager sessionManager = new SessionManager();
                if(isChecked) {
                    String checkBox = sessionManager.getPreferences(activity, "checkbox");
                    if (checkBox != null && !checkBox.isEmpty()) {
                        sessionManager.setPreferences(activity, "checkbox", checkBox + "|" + idServiceReq);
                    } else {
                        sessionManager.setPreferences(activity, "checkbox", idServiceReq);
                    }
                }
                else{
                    String checkBox = sessionManager.getPreferences(activity, "checkbox");
                    if (checkBox != null) {
                        String a = checkBox.replace(idServiceReq + "|","");
                        String b = a.replace("|" + idServiceReq ,"");
                        String c = b.replace(idServiceReq,"");
                        sessionManager.setPreferences(activity,"checkbox",c);
                    }
                }
            }
        });

    }
    public static void noData(int length, View view){
        ImageView no_data = view.findViewById(R.id.no_data);
        if(length == 0 ){
            no_data.setVisibility(View.VISIBLE);
        }
        else {
            no_data.setVisibility(View.GONE);
        }
    }
}


