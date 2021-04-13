package mg.mbds.nfcx_change.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mg.mbds.nfcx_change.Activity.NFCActivity;
import mg.mbds.nfcx_change.Adapter.ShopBuyAdapter;
import mg.mbds.nfcx_change.Adapter.ShopXChangeAdapter;
import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.Model.ServiceRequest;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Service.CustomAsyncTask;
import mg.mbds.nfcx_change.Service.GenericAsyncTask;
import mg.mbds.nfcx_change.Service.JSONParser;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Url_Base;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

public class ShopXchangeListFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressBar loading;

    private SwipeRefreshLayout swipeToRefresh;


    private Button validateShop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_shop_list, container, false);
        this.view = view;
        init();
        return view;
    }

    public void init(){
        initView();
        setRecycleView();
        goToNFCPage();
    }

    public void initView(){
        validateShop = view.findViewById(R.id.button_top);
        validateShop.setText(getResources().getString(R.string.xchange_process));
        validateShop.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        loading = view.findViewById(R.id.loading);
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(this);
        //setBanner();
    }

    public void goToNFCPage(){


        validateShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager();
                final String idRequest = sessionManager.getPreferences(getActivity(),"checkbox");
                if(idRequest != null && !idRequest.isEmpty()) {
                    Intent intent = new Intent(getActivity(), NFCActivity.class);

                    Bundle bundle = new Bundle();
                    intent.putExtra("utilisateur2", ((MainActivity) getActivity()).getUtilisateur2());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(),R.string.valider_achat, Toast.LENGTH_LONG);
                }
            }
        });
    }
    public void setBanner(){
        ImageView banner = ((MainActivity)getActivity()).findViewById(R.id.banner_img);
        banner.setImageDrawable(getResources().getDrawable(R.drawable.phone5));
    }
    public void setRecycleView(){
        mRecycleView = view.findViewById(R.id.my_recycler_view);

        mRecycleView.setHasFixedSize(true);
        //Pour le layout
        mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecycleView.setLayoutManager(mLayoutManager);

        //Pour l'adapter
        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                Utilitaire.noData(classMapTables.size(),view);
                mAdapter = new ShopXChangeAdapter(getActivity(),classMapTables,false);
                mRecycleView.setAdapter(mAdapter);
                loading.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
            }
        };

        SessionManager sessionManager = new SessionManager();
        String userId = sessionManager.getPreferences(getActivity(),"UserId");
        String url = String.format(Url_Base.URL_REQUEST_BY_USER_AND_TPE,userId,1);
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask,url,new HashMap<String, Object>(),null, JSONParser.GET,new ServiceRequest());
        customAsyncTask.execute();

    }

    @Override
    public void onRefresh() {
        //Toast.makeText(getActivity(),"Refresh",Toast.LENGTH_LONG).show();

        init();
    }
}
