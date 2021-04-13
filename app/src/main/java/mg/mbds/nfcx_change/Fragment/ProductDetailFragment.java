package mg.mbds.nfcx_change.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mg.mbds.nfcx_change.Adapter.ListSimilarServiceAdapter;
import mg.mbds.nfcx_change.Adapter.MyServiceAdapter;
import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.Model.ServiceRequest;
import mg.mbds.nfcx_change.Model.Utilisateur;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Service.CustomAsyncTask;
import mg.mbds.nfcx_change.Service.GenericAsyncTask;
import mg.mbds.nfcx_change.Service.JSONParser;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Url_Base;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

public class ProductDetailFragment extends Fragment {

    View view;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button button_buy_product;
    private Button button_xchange_product;
    private ImageView close_pop_up;

    private boolean isSuccessAddToCard = false;

    private String idService;
    private String type_Service_Id;

    private ImageView banner;
    private AppBarLayout app_bar;

    private ImageView image_owner_service;
    private TextView owner_service_name;
    private TextView owner_service_adr;
    private TextView token_price;
    private TextView type_service_name;
    private TextView service_name;
    private TextView date_service;
    private TextView description_service;
    private LinearLayout loading;
    private LinearLayout detail_layout;

    private ServiceRequest serviceRequestXChange;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);
        this.view = view;
        init();
        return view;
    }


    public void init(){
        initView();
        utils();
        setRecycleViewSimilar();

        ((MainActivity)getActivity()).setTabInvisible();

        setData();
    }

    public void utils(){
        setIdService(getArguments().getString("Service_Id"));
        setType_Service_Id(getArguments().getString("Type_Service_Id"));
        getActivity().setTitle(R.string.service_detail_title);
        app_bar.setExpanded(true);
    }

    public void initView(){
        button_buy_product = view.findViewById(R.id.product_buy);
        button_xchange_product = view.findViewById(R.id.product_xchange);
        banner = getActivity().findViewById(R.id.banner_img);
        app_bar = ((MainActivity)getActivity()).findViewById(R.id.app_bar);

        image_owner_service = view.findViewById(R.id.image_owner_service);
        owner_service_name = view.findViewById(R.id.owner_service_name);
        owner_service_adr = view.findViewById(R.id.owner_service_adr);
        token_price = view.findViewById(R.id.token_price);
        type_service_name = view.findViewById(R.id.type_service_name);
        service_name = view.findViewById(R.id.service_name);
        date_service = view.findViewById(R.id.date_service);
        description_service = view.findViewById(R.id.description_service);
        loading = view.findViewById(R.id.loading);
        detail_layout = view.findViewById(R.id.detail_layout);
    }

    public void setData(){
        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                if(classMapTables.size() > 0){
                    loading.setVisibility(View.GONE);
                    detail_layout.setVisibility(View.VISIBLE);
                    Service service = (Service) classMapTables.get(0);
                    owner_service_name.setText(service.getUtilisateur().getName());
                    owner_service_adr.setText(service.getUtilisateur().getMail());
                    token_price.setText(String.valueOf(service.getPrice()));
                    type_service_name.setText(service.getTypeservice().getTypeService());
                    service_name.setText(service.getServiceName());
                    date_service.setText(Utilitaire.formatDate(service.getPublicationDate()));
                    description_service.setText(service.getServiceDescription());

                    if(service.getServiceImgUrl() != null && !service.getServiceImgUrl().isEmpty())
                        //Utilitaire.downloadImage(getActivity(),banner,service.getServiceImgUrl());
                        Utilitaire.decodeBase64Image(getActivity(),banner,service.getServiceImgUrl());
                    else
                        banner.setImageDrawable(getResources().getDrawable(R.drawable.noimg));
                    if(service.getUtilisateur().getAvatar() != null && !service.getUtilisateur().getAvatar().isEmpty())
                        //Utilitaire.downloadImage(getActivity(), image_owner_service, service.getUtilisateur().getAvatar());
                        Utilitaire.decodeBase64Image(getActivity(), image_owner_service, service.getUtilisateur().getAvatar());

                    showPopUpOnclickButton((Service) classMapTables.get(0));

                }
            }
        };
        String url = String.format(Url_Base.URL_SERVICES_BY_ID, getIdService());
        HashMap<String,Object> parameters = new HashMap<>();
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask,url,parameters,null, JSONParser.GET,new Service());
        customAsyncTask.execute();
    }

    public void showPopUpOnclickButton(final Service service){
        button_buy_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                ServiceRequest serviceRequest = new ServiceRequest();
                serviceRequest.setDateRequest(new Date());
                serviceRequest.setService(service);

                Utilisateur utilisateur = ((MainActivity)getActivity()).getUtilisateur();
                serviceRequest.setUtilisateur(utilisateur);
                serviceRequest.setServiceXchange(new Service());
                serviceRequest.setStatusAccept(false);
                serviceRequest.setStatusPayment(false);
                serviceRequest.setTypeRequest(0);
                addBuyAndXchange(serviceRequest,0,null);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
        });

        button_xchange_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog myCustomDialog = new Dialog(getActivity());
                myCustomDialog.setContentView(R.layout.pop_up_x_change);
                xChangeListMyService(myCustomDialog, service);
                myCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myCustomDialog.show();
            }
        });

        if(isSuccessAddToCard()) {

        }

    }

    public void xChangeListMyService(final Dialog view, final Service serviceCurrent){
        final RecyclerView mRecycleView;

        RecyclerView.LayoutManager mLayoutManager;

        mRecycleView = view.findViewById(R.id.my_recycler_view);

        final ProgressBar loading = view.findViewById(R.id.loading);

        mRecycleView.setHasFixedSize(true);
        //Pour le layout
        mLayoutManager = new GridLayoutManager(this.getContext(),3);

        mRecycleView.setLayoutManager(mLayoutManager);

        final ProductDetailFragment productDetailFragment = this;
        //Pour l'adapter
        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                ServiceRequest serviceRequest = new ServiceRequest();
                serviceRequest.setDateRequest(new Date());
                serviceRequest.setService(serviceCurrent);

                Utilisateur utilisateur = ((MainActivity)getActivity()).getUtilisateur();
                serviceRequest.setUtilisateur(utilisateur);
                serviceRequest.setServiceXchange(new Service());
                serviceRequest.setStatusAccept(false);
                serviceRequest.setStatusPayment(false);
                serviceRequest.setTypeRequest(1);

                setServiceRequestXChange(serviceRequest);

                RecyclerView.Adapter mAdapter_ = new MyServiceAdapter(getActivity(), null, classMapTables, false, view, productDetailFragment);

                mRecycleView.setAdapter(mAdapter_);
                loading.setVisibility(View.GONE);
            }
        };

        SessionManager sessionManager = new SessionManager();
        String url = String.format(Url_Base.URL_SERVICE_BY_OWNER,sessionManager.getPreferences(getActivity(), "UserId"));

        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask,url,new HashMap<String, Object>(),null, JSONParser.GET,new Service());
        customAsyncTask.execute();


    }

    public void setRecycleViewSimilar(){
        setmRecycleView((RecyclerView) view.findViewById(R.id.my_recycler_view_similar));

        getmRecycleView().setHasFixedSize(true);
        //Pour le layout
        setmLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL, false));

        getmRecycleView().setLayoutManager(getmLayoutManager());

        //Pour l'adapter

        List<Service> serviceList = new ArrayList<>();
        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                setmAdapter(new ListSimilarServiceAdapter(getActivity(),classMapTables));

                getmRecycleView().setAdapter(getmAdapter());
            }
        };

        SessionManager sessionManager = new SessionManager();

        String url = String.format(Url_Base.URL_TOP_FIVE,getType_Service_Id(),sessionManager.getPreferences(getActivity(),"UserId"));
            CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask,url, new HashMap<String, Object>(),null,JSONParser.GET,new Service());
        customAsyncTask.execute();


    }

    public boolean isSuccessAddToCard() {
        return isSuccessAddToCard;
    }

    public void setSuccessAddToCard(boolean successAddToCard) {
        isSuccessAddToCard = successAddToCard;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getType_Service_Id() {
        return type_Service_Id;
    }

    public void setType_Service_Id(String type_Service_Id) {
        this.type_Service_Id = type_Service_Id;
    }

    public void addBuyAndXchange(ServiceRequest serviceRequest, final int typeRequest, final Dialog dialog) throws JsonProcessingException, JSONException {
        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                setSuccessAddToCard(true);
                final Dialog myCustomDialog = new Dialog(getActivity());
                myCustomDialog.setContentView(R.layout.pop_up_add_to_card_status);

                close_pop_up = myCustomDialog.findViewById(R.id.close_pop_up);

                myCustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myCustomDialog.show();

                close_pop_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myCustomDialog.dismiss();
                        if(dialog != null)
                            dialog.dismiss();
                        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("typeRequest",typeRequest);
                        viewPagerFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,viewPagerFragment, "View Page Fragment").commit();
                    }
                });
            }
        };

        JSONObject jsonObject = Utilitaire.buildAndDeleteNullJson(serviceRequest);
        //jsonObject = Utilitaire.deleteAllNullJson(jsonObject);

        HashMap<String,Object> parameters = new HashMap<>();
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask,Url_Base.URL_CREATE_REQUEST,parameters,jsonObject,JSONParser.POST,new ServiceRequest());
        customAsyncTask.execute();
    }

    public void goToBackFragment(){
        getFragmentManager().popBackStackImmediate();
        ((MainActivity)getActivity()).dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));

    }

    public RecyclerView getmRecycleView() {
        return mRecycleView;
    }

    public void setmRecycleView(RecyclerView mRecycleView) {
        this.mRecycleView = mRecycleView;
    }

    public RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public RecyclerView.LayoutManager getmLayoutManager() {
        return mLayoutManager;
    }

    public void setmLayoutManager(RecyclerView.LayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

    public ServiceRequest getServiceRequestXChange() {
        return serviceRequestXChange;
    }

    public void setServiceRequestXChange(ServiceRequest serviceRequestXChange) {
        this.serviceRequestXChange = serviceRequestXChange;
    }


}
