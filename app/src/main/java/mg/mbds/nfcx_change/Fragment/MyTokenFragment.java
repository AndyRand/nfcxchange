package mg.mbds.nfcx_change.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mg.mbds.nfcx_change.Adapter.ShopBuyAdapter;
import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.Model.Utilisateur;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Service.CustomAsyncTask;
import mg.mbds.nfcx_change.Service.GenericAsyncTask;
import mg.mbds.nfcx_change.Service.JSONParser;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Url_Base;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

public class MyTokenFragment extends Fragment {

    View view;
    TextView token_price;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_token, container, false);
        this.view = view;
        init();
        return view;
    }

    public void init(){
        sessionManager = new SessionManager();
        initView();
        setBanner();
        ((MainActivity)getActivity()).setTabInvisible();
        getToken();
    }

    public void initView(){
        token_price = view.findViewById(R.id.token_price);

        //token_price.setText(sessionManager.getPreferences(getActivity(),"Token"));
    }

    public void getToken(){
        String userID = sessionManager.getPreferences(getActivity(),"UserId");
        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                if(classMapTables.size() > 0){
                    Utilisateur utilisateur = (Utilisateur) classMapTables.get(0);
                    token_price.setText(String.valueOf(utilisateur.getToken()));
                }
            }
        };

        String url = String.format(Url_Base.URL_USER_BY_ID,userID);
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask, url,new HashMap<String, Object>(), null, JSONParser.GET, new Utilisateur());
        customAsyncTask.execute();
    }

    public void setBanner(){
        ImageView banner = ((MainActivity)getActivity()).findViewById(R.id.banner_img);
        banner.setImageDrawable(getResources().getDrawable(R.drawable.token1));
    }


}
