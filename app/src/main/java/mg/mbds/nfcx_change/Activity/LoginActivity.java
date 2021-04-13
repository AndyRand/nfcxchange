package mg.mbds.nfcx_change.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Utilisateur;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Service.CustomAsyncTask;
import mg.mbds.nfcx_change.Service.GenericAsyncTask;
import mg.mbds.nfcx_change.Service.JSONParser;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Url_Base;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity{

    TextView mailAddress;
    TextView password;
    TextView patientez;
    LinearLayout activity_login;
    Button buttonConnexion;
    Button buttonInscription;
    ProgressBar progressBarLogin;
    ImageView imageLogin;


    private Activity activity = this;

    public LoginActivity(){
        setLayout(R.layout.activity_login);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.init();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void init(){

        this.initView();
        this.setContent();
        this.listenerButton();
    }

    public void initView(){
        mailAddress = (TextView) findViewById(R.id.mailAddress);
        password = (TextView)findViewById(R.id.password);
        activity_login = (LinearLayout) findViewById(R.id.activity_login);
        buttonConnexion = (Button) findViewById(R.id.seconnecter);
        buttonInscription = (Button) findViewById(R.id.sinscrire);
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        patientez = (TextView) findViewById(R.id.patientez);
        imageLogin = findViewById(R.id.imageLogin);

        //Utilitaire.decodeBase64Image(this,getResources().getString(R.string.test_img),imageLogin);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setContent(){
        //Utilitaire.blurDrawableImage(this,activity_login,R.drawable.nfc);
    }

    public Activity getActivity() {
        return activity;
    }

    public void listenerButton(){
        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mailAddress.getText().toString().compareTo("") == 0)
                    Toast.makeText(getActivity(), mailAddress.getHint() + " " + getActivity().getResources().getString(R.string.estVide), Toast.LENGTH_SHORT).show();
                else if (password.getText().toString().compareTo("") == 0)
                    Toast.makeText(getActivity(), password.getHint() + " " + getActivity().getResources().getString(R.string.estVide), Toast.LENGTH_SHORT).show();
                else {
                    progressBarLogin.setVisibility(View.VISIBLE);
                    patientez.setVisibility(View.VISIBLE);
                    Utilitaire.hideKeyboard(getActivity());


                    GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
                        @Override
                        public void execute(List<ClassMapTable> classMapTables) {
                            if(classMapTables.size() != 0) {
                                if(classMapTables.get(0).isSuccess()) {
                                    Utilisateur utilisateur = ((Utilisateur)classMapTables.get(0));

                                    setSessionData(utilisateur);

                                    progressBarLogin.setVisibility(View.VISIBLE);
                                    patientez.setVisibility(View.VISIBLE);

                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    intent.putExtras(bundle);
                                    intent.putExtra("user",(Utilisateur)(classMapTables.get(0)));
                                    getActivity().finish();
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getActivity(), classMapTables.get(0).getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    };

                    HashMap<String, Object> parameter = new HashMap<>();
                    parameter.put("mail",mailAddress.getText().toString().trim());
                    parameter.put("password",password.getText());
                    CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask, Url_Base.URL_LOGIN,parameter,null, JSONParser.POST, new Utilisateur());
                    customAsyncTask.execute();


                }


            }
        });

    }

    public void setSessionData(Utilisateur utilisateur){
        /*Session*/
        final SessionManager sessionManager = new SessionManager();

        sessionManager.setPreferences(getActivity(),"UserId",utilisateur.getId());
        sessionManager.setPreferences(getActivity(),"UserName",utilisateur.getName());
        sessionManager.setPreferences(getActivity(),"UserFirstName",utilisateur.getFirstname());
        sessionManager.setPreferences(getActivity(),"UserMailAddress",utilisateur.getMail());
        sessionManager.setPreferences(getActivity(),"UserAvatar",utilisateur.getAvatar());
        sessionManager.setPreferences(getActivity(),"UserBirthdayDate",String.valueOf(utilisateur.getBirthdaydate()));
        sessionManager.setPreferences(getActivity(),"Token",String.valueOf(utilisateur.getToken()));
    }







}

