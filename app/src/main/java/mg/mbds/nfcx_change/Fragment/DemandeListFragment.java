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

import java.util.HashMap;
import java.util.List;

import mg.mbds.nfcx_change.Activity.NFCActivity;
import mg.mbds.nfcx_change.Adapter.ShopBuyAdapter;
import mg.mbds.nfcx_change.Adapter.ShopXChangeAdapter;
import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.ServiceRequest;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Service.CustomAsyncTask;
import mg.mbds.nfcx_change.Service.GenericAsyncTask;
import mg.mbds.nfcx_change.Service.JSONParser;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Url_Base;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

public class DemandeListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View view;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView mRecycleView1;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager1;

    private ProgressBar loading;

    private Button button_top;
    private SessionManager sessionManager;

    private SwipeRefreshLayout swipeToRefresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_demande_list, container, false);
        this.view = view;
        init();
        return view;
    }

    public void initSession(){
        sessionManager = new SessionManager();
        sessionManager.remove(getActivity(),"checkbox");
    }

    public void init(){
        initView();
        setBanner();
        setRecycleView();
        initSession();
        ((MainActivity)getActivity()).setTabInvisible();

        clickValidateDemande();
    }

    public void initView(){
        button_top = view.findViewById(R.id.button_top);
        loading = view.findViewById(R.id.loading);
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(this);
        getActivity().setTitle(R.string.demande);
    }

    public void setRecycleView(){
        mRecycleView = view.findViewById(R.id.my_recycler_view);

        mRecycleView.setHasFixedSize(true);
        //Pour le layout
        mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecycleView.setLayoutManager(mLayoutManager);

        setRecycleView1();

        SessionManager sessionManager = new SessionManager();
        final String userId = sessionManager.getPreferences(getActivity(),"UserId");
        //Pour l'adapter

        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {

                List<ClassMapTable>[] lists = Utilitaire.filterRequest(classMapTables,userId);
                Utilitaire.noData(lists[0].size(),view);
                mAdapter = new ShopBuyAdapter(getActivity(),lists[0],true);
                mRecycleView.setAdapter(mAdapter);

                mAdapter1 = new ShopXChangeAdapter(getActivity(),lists[1],true);
                mRecycleView1.setAdapter(mAdapter1);

                loading.setVisibility(View.GONE);

                swipeToRefresh.setRefreshing(false);

            }
        };

        String url = String.format(Url_Base.URL_REQUEST_NOT_ACCEPT,userId);
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask,url,new HashMap<String, Object>(),null, JSONParser.GET,new ServiceRequest());
        customAsyncTask.execute();

        /*mAdapter = new ShopBuyAdapter(getActivity(),serviceList);

        mRecycleView.setAdapter(mAdapter);*/
    }

    public void setRecycleView1(){
        mRecycleView1 = view.findViewById(R.id.my_recycler_view1);

        mRecycleView1.setHasFixedSize(true);
        //Pour le layout
        mLayoutManager1 = new LinearLayoutManager(this.getContext());

        mRecycleView1.setLayoutManager(mLayoutManager1);
    }

    public void setBanner(){
        ImageView banner = ((MainActivity)getActivity()).findViewById(R.id.banner_img);
        banner.setImageDrawable(getResources().getDrawable(R.drawable.phone7));
    }

    public void clickValidateDemande(){
        button_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String checkbox = sessionManager.getPreferences(getActivity(),"checkbox");
                if(checkbox != null && !checkbox.isEmpty()){
                        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
                            @Override
                            public void execute(List<ClassMapTable> classMapTables) {
                                if(classMapTables.size() > 0){
                                    if(classMapTables.get(0).isSuccess()){
                                        Toast.makeText(getActivity(),classMapTables.get(0).getMessage(),Toast.LENGTH_LONG).show();
                                        getFragmentManager().beginTransaction().replace(R.id.container,new DemandeListFragment(),"").commit();
                                        sessionManager.remove(getActivity(),"checkbox");
                                    }
                                }
                            }
                        };

                        String url = Url_Base.URL_VALIDATE_DEMANDE;
                        HashMap<String,Object> parameter = new HashMap<>();
                        parameter.put("idServiceRequest",checkbox);
                        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask,url,parameter,null,JSONParser.POST,new ServiceRequest());
                        customAsyncTask.execute();
                }
                else{
                    Toast.makeText(getActivity(),R.string.update_service_no_data,Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        //Toast.makeText(getActivity(),"Refresh",Toast.LENGTH_LONG).show();

        init();
    }
}
