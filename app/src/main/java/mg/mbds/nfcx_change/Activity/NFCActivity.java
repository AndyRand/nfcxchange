package mg.mbds.nfcx_change.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import mg.mbds.nfcx_change.Model.Utilisateur;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

public class NFCActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback  {

    Utilisateur utilisateur1;
    Utilisateur utilisateur2;

    ImageView image_user_1;
    ImageView image_user_2;
    TextView user_name_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        init();
    }

    public void init(){
        initView();
        initNFC();
        setData();
    }

    public void initView(){
        image_user_1 = findViewById(R.id.image_user_1);
        image_user_2 = findViewById(R.id.image_user_2);
        user_name_2 = findViewById(R.id.user_name_2);
    }

    public void setData(){
        SessionManager sessionManager = new SessionManager();

        utilisateur1 = new Utilisateur();
        utilisateur1.setId(sessionManager.getPreferences(this,"UserId"));
        utilisateur1.setName(sessionManager.getPreferences(this,"UserName"));
        utilisateur1.setFirstname(sessionManager.getPreferences(this,"UserFirstName"));
        utilisateur1.setMail(sessionManager.getPreferences(this,"UserMailAddress"));
        utilisateur1.setAvatar(sessionManager.getPreferences(this,"UserAvatar"));

        utilisateur2 = (Utilisateur) getIntent().getSerializableExtra("utilisateur2");

        Utilitaire.decodeBase64Image(this,image_user_1,utilisateur1.getAvatar());
        Utilitaire.decodeBase64Image(this,image_user_2,utilisateur2.getAvatar());
    }


    public void initNFC(){

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Sorry this device does not have NFC.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }


    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        SessionManager sessionManager = new SessionManager();
        String userId = sessionManager.getPreferences(this,"UserId");
        String requestServiceId = sessionManager.getPreferences(this,"checkbox");

        //String message = "5c84e4f2b5344f00174cd912";
        String message = sessionManager.getPreferences(this,"checkbox");;
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

}
