package mg.mbds.nfcx_change.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mg.mbds.nfcx_change.Activity.BaseActivity;
import mg.mbds.nfcx_change.Adapter.EndlessRecyclerViewScrollListener;
import mg.mbds.nfcx_change.Adapter.ListServiceAdapter;
import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.Model.StringWithTag;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Service.CustomAsyncTask;
import mg.mbds.nfcx_change.Service.GenericAsyncTask;
import mg.mbds.nfcx_change.Service.JSONParser;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Url_Base;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

public class ProductListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button detailButton;
    private View view;
    private ProgressBar loading;
    private TextView type_service_name;

    private SwipeRefreshLayout swipeToRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);
        this.view = view;
        init();
        return view;
    }


    public void init(){
        initView();
        setRecycleView(view);
        utils();
        setBanner();
        pagination();
        ((MainActivity)getActivity()).setTabInvisible();
        rechercher();

    }

    public void utils(){
        Utilitaire.hideFabOnScrolling(getActivity(),mRecycleView);
        getActivity().setTitle(R.string.service_list_title);

    }

    public void setBanner(){
        ImageView banner = ((MainActivity)getActivity()).findViewById(R.id.banner_img);
        banner.setImageDrawable(getResources().getDrawable(R.drawable.phone2));
    }

    public void initView(){
        loading = view.findViewById(R.id.loading);
        type_service_name = view.findViewById(R.id.type_service_name);
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setOnRefreshListener(this);

    }

    public void setRecycleView(View view){
        mRecycleView = view.findViewById(R.id.my_recycler_view);

        mRecycleView.setHasFixedSize(true);
        //Pour le layout
        mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecycleView.setLayoutManager(mLayoutManager);

        //Pour l'adapter

        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                loading.setVisibility(View.GONE);
                type_service_name.setText(getResources().getString(R.string.all));
                mAdapter = new ListServiceAdapter(getActivity(),classMapTables);
                mRecycleView.setAdapter(mAdapter);
                swipeToRefresh.setRefreshing(false);
            }
        };

        HashMap<String, Object> parameters = new HashMap<>();
        SessionManager sessionManager = new SessionManager();
        String userID = sessionManager.getPreferences(getActivity(), "UserId");
        String url = String.format(Url_Base.URL_SERVICES_OTHER,userID);
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask, url,parameters,null, JSONParser.GET,new Service());
        customAsyncTask.execute();


    }

    public void pagination (){

    }

    public void rechercher(){
        final MainActivity activity = (MainActivity)getActivity();
        activity.getSpinner_type_service().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object object = parent.getItemAtPosition(position);
                if(object instanceof StringWithTag) {
                    StringWithTag s = (StringWithTag) (parent.getItemAtPosition(position));
                    activity.setIdSelectedTypeService(s.getId());
                    activity.setSelectedTypeService(s.getNomColonne());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activity.getRechercher().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Fragment fragment = (Fragment) getFragmentManager().findFragmentByTag("Product_List_Fragment");
                    if (fragment != null && fragment.isVisible()) {
                        mRecycleView.setVisibility(View.GONE);
                        loading.setVisibility(View.VISIBLE);
                        activity.closeRightNavigation();
                        type_service_name.setText(getResources().getString(R.string.en_cours));
                        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
                            @Override
                            public void execute(List<ClassMapTable> classMapTables) {
                                Utilitaire.noData(classMapTables.size(), view);
                                String serviceName = activity.getServiceName().getText().toString();
                                String serviceTypeName = activity.getSelectedTypeService();
                                if (!serviceName.isEmpty())
                                    serviceTypeName += " (" + activity.getServiceName().getText().toString() + " , " + activity.getRange_price().getLeftIndex() + "-" + activity.getRange_price().getRightIndex() + ")";
                                else
                                    serviceTypeName += " (" + activity.getRange_price().getLeftIndex() + "-" + activity.getRange_price().getRightIndex() + ")";
                                type_service_name.setText(serviceTypeName);
                                mAdapter = new ListServiceAdapter(getActivity(), classMapTables);
                                mRecycleView.setAdapter(mAdapter);
                                mRecycleView.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                            }
                        };
                        String serviceName = activity.getServiceName().getText().toString();
                        int prixMin = activity.getRange_price().getLeftIndex();
                        int prixMax = activity.getRange_price().getRightIndex();
                        String typeServiceId = activity.getIdSelectedTypeService();
                        String userId = new SessionManager().getPreferences(getActivity(), "UserId");
                        HashMap<String, Object> parameters = new HashMap<>();
                        parameters.put("serviceName", serviceName);
                        parameters.put("typeServiceId", typeServiceId);
                        parameters.put("prixMin", prixMin);
                        parameters.put("prixMax", prixMax);
                        parameters.put("userId", userId);
                        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask, Url_Base.URL_SEARCH, parameters, null, JSONParser.POST, new Service());
                        customAsyncTask.execute();
                    }
                }
                catch (Exception e){
                    //Toast.makeText(getFragmentManager()),R.string.not_impl,Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(),"Refresh",Toast.LENGTH_LONG).show();

        init();
    }
}
