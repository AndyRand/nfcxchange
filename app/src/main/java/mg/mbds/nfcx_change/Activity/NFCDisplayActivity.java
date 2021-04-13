package mg.mbds.nfcx_change.Activity;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Payment;
import mg.mbds.nfcx_change.Model.ServiceRequest;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Service.CustomAsyncTask;
import mg.mbds.nfcx_change.Service.GenericAsyncTask;
import mg.mbds.nfcx_change.Service.JSONParser;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Url_Base;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

public class NFCDisplayActivity extends AppCompatActivity{

    ImageView image_user_buy;
    ImageView image_user_buy1;
    ImageView product_img_buy;
    ImageView product_img_buy1;
    TextView user_name_buy;
    TextView user_name_buy1;
    TextView title_buy;
    TextView title_buy1;
    TextView price_buy;
    TextView price_buy1;
    TextView idReq;
    ImageView image_user_buy1_;
    TextView user_name_buy1_;
    LinearLayout buy;
    LinearLayout xchange;
    LinearLayout data;
    ProgressBar loading;
    LinearLayout loading1;
    CardView transaction_ok;
    private String userId;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_display);
        setActivity(this);
        init();
        SessionManager sessionManager = new SessionManager();
        setUserId(sessionManager.getPreferences(this,"UserId"));
    }

    public void init(){
        initView();
    }

    public void initView(){
        image_user_buy = findViewById(R.id.image_user_buy);
        image_user_buy1 = findViewById(R.id.image_user_buy1);
        product_img_buy = findViewById(R.id.product_img_buy);
        product_img_buy1 = findViewById(R.id.product_img_buy1);
        user_name_buy = findViewById(R.id.user_name_buy);
        user_name_buy1 = findViewById(R.id.user_name_buy1);
        title_buy = findViewById(R.id.title_buy);
        title_buy1 = findViewById(R.id.title_buy1);
        price_buy = findViewById(R.id.price_buy);
        price_buy1 = findViewById(R.id.price_buy1);
        image_user_buy1_ = findViewById(R.id.image_user_buy1_);
        user_name_buy1_ = findViewById(R.id.user_name_buy1_);
        buy = findViewById(R.id.buy);
        xchange = findViewById(R.id.xchange);
        data = findViewById(R.id.data);
        loading = findViewById(R.id.loading);
        loading1 = findViewById(R.id.loading1);
        transaction_ok = findViewById(R.id.transaction_ok);
        idReq = findViewById(R.id.idReq);
    }

    @Override
    protected void onResume(){
        super.onResume();
        final Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            final String response  = new String(message.getRecords()[0].getPayload());
            idReq.setText(idReq.getText() + response);
            if(response != null && !response.isEmpty()){
                final GenericAsyncTask genericAsyncTask =  new GenericAsyncTask() {
                    @Override
                    public void execute(List<ClassMapTable> classMapTables) {
                        if(classMapTables.size() > 0){
                            ServiceRequest serviceRequest = (ServiceRequest) classMapTables.get(0);
                            if(getUserId().equals(serviceRequest.getService().getUtilisateur().getId())) {
                                showData(serviceRequest);
                                loading.setVisibility(View.GONE);
                                loading1.setVisibility(View.VISIBLE);
                                data.setVisibility(View.VISIBLE);
                                int typeRequest = 0;
                                if (serviceRequest.getServiceXchange().getId() != null && !serviceRequest.getServiceXchange().getId().isEmpty())
                                    typeRequest = 1;
                                transaction(serviceRequest.getId(), typeRequest);
                            }
                            else{
                                Toast.makeText(getActivity(),R.string.go_authentificate,Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(getActivity(),LoginActivity.class);
                                getActivity().startActivity(intent1);
                                getActivity().finish();

                            }
                        }
                    }
                };
                String url = String.format(Url_Base.URL_GET_REQUEST_BY_ID, response);
                CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask, url ,new HashMap<String, Object>(), null, JSONParser.GET,new ServiceRequest());
                customAsyncTask.execute();
            }


        } else
            Log.e("msg:","Waiting for NDEF Message");

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void showData(ServiceRequest serviceRequest){
        if(serviceRequest.getServiceXchange().getId() != null && !serviceRequest.getServiceXchange().getId().isEmpty()){
            buy.setVisibility(View.GONE);
            xchange.setVisibility(View.VISIBLE);
            Utilitaire.decodeBase64Image(getActivity(),image_user_buy1,serviceRequest.getServiceXchange().getServiceImgUrl());
            user_name_buy1.setText(serviceRequest.getServiceXchange().getUtilisateur().getName());
            title_buy1.setText(serviceRequest.getServiceXchange().getServiceName());
            price_buy1.setText(String.valueOf(serviceRequest.getServiceXchange().getPrice()));
            Utilitaire.decodeBase64Image(getActivity(),image_user_buy1,serviceRequest.getServiceXchange().getUtilisateur().getAvatar());
            Utilitaire.decodeBase64Image(getActivity(),product_img_buy1,serviceRequest.getServiceXchange().getServiceImgUrl());
        }
        else{
            buy.setVisibility(View.VISIBLE);
            xchange.setVisibility(View.GONE);
            Utilitaire.decodeBase64Image(getActivity(),image_user_buy1_,serviceRequest.getUtilisateur().getAvatar());
            user_name_buy1_.setText(serviceRequest.getUtilisateur().getName());
        }

        user_name_buy.setText(serviceRequest.getService().getUtilisateur().getName());
        title_buy.setText(serviceRequest.getService().getServiceName());
        price_buy.setText(String.valueOf(serviceRequest.getService().getPrice()));
        Utilitaire.decodeBase64Image(getActivity(),image_user_buy,serviceRequest.getService().getUtilisateur().getAvatar());
        Utilitaire.decodeBase64Image(getActivity(),product_img_buy,serviceRequest.getService().getServiceImgUrl());
    }

    public void transaction(String idServiceRequest, int typeRequest){
        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                loading1.setVisibility(View.INVISIBLE);
                transaction_ok.setVisibility(View.VISIBLE);
            }
        };

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("idServiceRequest", idServiceRequest);
        String url = Url_Base.URL_BUY;
        if(typeRequest == 1)
            url  = Url_Base.URL_XCHANGE;
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask,url,parameters,null,JSONParser.POST,new Payment());
        customAsyncTask.execute();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
