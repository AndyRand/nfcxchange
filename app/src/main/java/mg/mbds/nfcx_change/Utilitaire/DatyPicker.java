package mg.mbds.nfcx_change.Utilitaire;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by BillySycher on 05/04/2017.
 */

public class DatyPicker implements android.view.View.OnTouchListener, DatePickerDialog.OnDateSetListener{
    private Calendar calendar = Calendar.getInstance();
    private EditText editText;
    private Activity activity;
    private int compteur;
    public DatyPicker(EditText editText, Activity activity) {
        this.setEditText(editText);
        this.setActivity(activity);
    }


    public void updateLabel(){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        getEditText().setText(sdf.format(getCalendar().getTime()));
    }
    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        getCalendar().set(Calendar.YEAR, year);
        getCalendar().set(Calendar.MONTH, month);
        getCalendar().set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                setCompteur(0);
                break;

            case MotionEvent.ACTION_MOVE:
                setCompteur(getCompteur() + 1 );
                break;

            case MotionEvent.ACTION_UP:

                //Si on n'a pas fait bouger le menu recherche mais si on veut la cliquer
                if(getCompteur() <= 5){
                    //Pour cacher le clavier quand on affiche la date picker
                    //InputMethodManager inputManager = (InputMethodManager) this.getActivity().getSystemService(this.getActivity().INPUT_METHOD_SERVICE);
                    //inputManager.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    new DatePickerDialog(getActivity(), this, getCalendar()
                            .get(Calendar.YEAR), getCalendar().get(Calendar.MONTH),
                            getCalendar().get(Calendar.DAY_OF_MONTH)).show();
                }
                break;

            default:
                return false;
        }
        return true;
    }

    public int getCompteur() {
        return compteur;
    }

    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }
}
