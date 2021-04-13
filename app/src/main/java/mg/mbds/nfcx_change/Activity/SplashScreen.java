package mg.mbds.nfcx_change.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Session.SessionManager;


/**
 * Created by User on 20/02/2019.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        SessionManager sessionManager = new SessionManager();
        final String userId = sessionManager.getPreferences(this,"UserId");
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if(userId != null && !userId.isEmpty()){
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
