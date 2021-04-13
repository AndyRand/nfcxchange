package mg.mbds.nfcx_change.Dialog;


import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;


public class ChoixLangue extends DialogFragment {

    private String[] langues;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final SessionManager sessionManager = new SessionManager();

        getLangues();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.langue))
                .setItems(langues, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                       switch (which) {
                           case 0:
                               Utilitaire.changeLanguage(getActivity(),"mg");
                               sessionManager.setPreferences(getActivity(),"langue","mg");
                               break;
                           case 1:
                               Utilitaire.changeLanguage(getActivity(),"en");
                               sessionManager.setPreferences(getActivity(),"langue","mg");
                               break;
                           case 2:
                               Utilitaire.changeLanguage(getActivity(),"fr");
                               sessionManager.setPreferences(getActivity(),"langue","mg");
                               break;
                       }
                    }
                });
        return builder.create();
    }

    private void getLangues () {
        langues = new String[3];
        langues[0] = getResources().getString(R.string.malgache);
        langues[1] = getResources().getString(R.string.anglais);
        langues[2] = getResources().getString(R.string.francais);
    }
}
