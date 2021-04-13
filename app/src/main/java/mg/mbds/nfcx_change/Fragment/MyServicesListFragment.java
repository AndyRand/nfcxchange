package mg.mbds.nfcx_change.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mg.mbds.nfcx_change.Adapter.MyServiceAdapter;
import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.Service;
import mg.mbds.nfcx_change.Model.ServiceRequest;
import mg.mbds.nfcx_change.R;
import mg.mbds.nfcx_change.Service.CustomAsyncTask;
import mg.mbds.nfcx_change.Service.GenericAsyncTask;
import mg.mbds.nfcx_change.Service.JSONParser;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.DatyPicker;
import mg.mbds.nfcx_change.Utilitaire.Url_Base;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

public class MyServicesListFragment extends Fragment {

    View view;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button button_top;

    private ImageView product_img;
    private TextView product_name;
    private TextView price_my_product;
    private EditText service_name;
    private EditText date_publication;
    private SeekBar price_update;
    private TextView price_text;
    private TextView description_service;
    private Button button_update;
    private CardView card_view_update;
    private CardView card_view_my_service;
    private Button button_back;
    private ProgressBar loading;

    private Button button_upload_file;

    public static final int PICK_IMAGE = 1;
    private AppBarLayout app_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_my_service, container, false);
        this.view = view;
        init();
        return view;
    }

    public void init(){
        initView();
        setData();
        setRecycleView();
        setBanner();
        ((MainActivity)getActivity()).setTabInvisible();
        onClickButtonFile();

    }

    public void setBanner(){
        ImageView banner = ((MainActivity)getActivity()).findViewById(R.id.banner_img);
        banner.setImageDrawable(getResources().getDrawable(R.drawable.phone3));

    }

    public void initView(){
        button_top = view.findViewById(R.id.button_top);

        product_img = view.findViewById(R.id.product_img);
        product_name = view.findViewById(R.id.product_name);
        price_my_product = view.findViewById(R.id.price_my_product);
        service_name = view.findViewById(R.id.service_name);
        date_publication = view.findViewById(R.id.date_publication);
        price_update = view.findViewById(R.id.price_update);
        price_text = view.findViewById(R.id.price_text);
        description_service = view.findViewById(R.id.description_service);
        button_update = view.findViewById(R.id.button_update);
        card_view_update = view.findViewById(R.id.card_view_update);
        card_view_my_service = view.findViewById(R.id.card_view_my_service);
        button_back = view.findViewById(R.id.button_back);
        loading = view.findViewById(R.id.loading);

        button_upload_file = view.findViewById(R.id.button_upload_file);

        app_bar = ((MainActivity)getActivity()).findViewById(R.id.app_bar);

    }
    public void setData(){
        button_top.setText(getResources().getString(R.string.new_service));
    }

    public void setRecycleView(){
        setmRecycleView((RecyclerView) view.findViewById(R.id.my_recycler_view));

        getmRecycleView().setHasFixedSize(true);
        //Pour le layout
        mLayoutManager = new GridLayoutManager(this.getContext(),3);

        getmRecycleView().setLayoutManager(mLayoutManager);
        final Fragment fragment = this;
        //Pour l'adapter
        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                mAdapter = new MyServiceAdapter(getActivity(),(MyServicesListFragment) fragment,classMapTables, true, null,null);

                getmRecycleView().setAdapter(mAdapter);
                loading.setVisibility(View.GONE);
            }
        };

        SessionManager sessionManager = new SessionManager();
        String url = String.format(Url_Base.URL_SERVICE_BY_OWNER,sessionManager.getPreferences(getActivity(), "UserId"));

        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask,url,new HashMap<String, Object>(),null, JSONParser.GET,new Service());
        customAsyncTask.execute();


    }

    public void showUpdate(final Service service){
        card_view_update.setVisibility(View.VISIBLE);
        card_view_my_service.setVisibility(View.VISIBLE);
        button_back.setVisibility(View.VISIBLE);

        Utilitaire.decodeBase64Image(getActivity(),product_img,service.getServiceImgUrl());
        product_name.setText(service.getServiceName());
        price_my_product.setText(String.valueOf(service.getPrice()));
        service_name.setText(service.getServiceName());
        date_publication.setText(String.valueOf(Utilitaire.formatDate(service.getPublicationDate())));
        price_update.setProgress((int) service.getPrice());
        price_text.setText(String.valueOf(service.getPrice()));
        description_service.setText(service.getServiceDescription());

        app_bar.setExpanded(false);

        showDatePicker();

        price_update.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                price_text.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final MyServicesListFragment fragment = this;

        button_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    service.setServiceName(service_name.getText().toString());
                    service.setPublicationDate(new Date(date_publication.getText().toString()));
                    service.setPrice(price_update.getProgress());
                    service.setServiceDescription(description_service.getText().toString());
                    GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
                        @Override
                        public void execute(List<ClassMapTable> classMapTables) {
                            Toast.makeText(getActivity(),R.string.update_service,Toast.LENGTH_LONG).show();
                        }
                    };

                    String url = String.format(Url_Base.URL_UPDATE_SERVICE, service.getId());

                    JSONObject parameters = Utilitaire.buildAndDeleteNullJson(service);

                    removeNullData(parameters);
                    CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask, url,new HashMap<String, Object>(), parameters, JSONParser.POST,new Service());
                    customAsyncTask.execute();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_view_update.setVisibility(View.GONE);
                card_view_my_service.setVisibility(View.GONE);
                button_back.setVisibility(View.GONE);
                getmRecycleView().setVisibility(View.VISIBLE);
                app_bar.setExpanded(true);
            }
        });

        getmRecycleView().setVisibility(View.GONE);

    }

   public RecyclerView getmRecycleView() {
        return mRecycleView;
    }

    public void setmRecycleView(RecyclerView mRecycleView) {
        this.mRecycleView = mRecycleView;
    }

    public void removeNullData(JSONObject jsonObject) throws JSONException {
        jsonObject.remove("data");
        jsonObject.remove("message");
        jsonObject.getJSONObject("typeService").remove("data");
        jsonObject.getJSONObject("typeService").remove("message");
        jsonObject.getJSONObject("user").remove("message");
        jsonObject.getJSONObject("user").remove("data");
        jsonObject.getJSONObject("user").remove("user");
    }

    public void showDatePicker(){
        date_publication.setOnTouchListener(new DatyPicker(date_publication,getActivity()));
    }

    public void onClickButtonFile(){
        button_upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            Toast.makeText(getActivity(),"azo ny sary",Toast.LENGTH_LONG);
        }
    }

}
