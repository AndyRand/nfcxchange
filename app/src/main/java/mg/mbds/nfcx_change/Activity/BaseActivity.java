package mg.mbds.nfcx_change.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;


/**
 * Created by BillySycher on 19/04/2017.
 */

public class BaseActivity extends AppCompatActivity {
    private int layout;
    private FloatingActionButton fab;
    private float dX;
    private float dY;
    private int width;
    private int height;
    private int lastAction;
    private int compteur;
    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    private LayoutInflater layoutInflater;
    public BaseActivity(){}
    public BaseActivity(int layout){
        setLayout(layout);
    }
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
    }


    //Fonction pour avoir la taille de l'ecran du téléphone
    public void getDisplayMetrics(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setHeight(displayMetrics.heightPixels);
        setWidth(displayMetrics.widthPixels);
    }


    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public void setFab(FloatingActionButton fab) {
        this.fab = fab;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }


}
