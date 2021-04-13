package mg.mbds.nfcx_change.Utilitaire;

import android.app.Activity;
import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

/**
 * Created by BillySycher on 05/04/2017.
 */

public class PopUpSearch {
    private Activity activity;
    FloatingActionButton close;
    public PopUpSearch(Activity activity){
        this.setActivity(activity);
    }
    public void showPopUp(){
        try{

            /*final Dialog myCustomDialog = new Dialog(getActivity());
            myCustomDialog.setTitle("Recherche");
            myCustomDialog.setContentView(R.layout.pop_up_default);
            myCustomDialog.show();

            EditText dateMin = (EditText)myCustomDialog.findViewById(R.id.dateMin);
            EditText dateMax = (EditText)myCustomDialog.findViewById(R.id.dateMax);
            //dateMin.setOnClickListener(new DatyPicker(dateMin,getActivity()));
            //dateMax.setOnClickListener(new DatyPicker(dateMax,getActivity()));

            close = (FloatingActionButton) myCustomDialog.findViewById(R.id.close_btn);
            close.setOnClickListener(new Cancel_popup(myCustomDialog));*/
        }
        catch (Exception e){

        }

    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    class Cancel_popup implements View.OnClickListener{
        Dialog dialog;
        public Cancel_popup(Dialog dialog){
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    }
}
