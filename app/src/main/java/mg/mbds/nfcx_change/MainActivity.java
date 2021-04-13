package mg.mbds.nfcx_change;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import fr.ganfra.materialspinner.MaterialSpinner;
import mg.mbds.nfcx_change.Activity.BaseActivity;
import mg.mbds.nfcx_change.Activity.LoginActivity;
import mg.mbds.nfcx_change.Dialog.ChoixLangue;
import mg.mbds.nfcx_change.Fragment.DemandeListFragment;
import mg.mbds.nfcx_change.Fragment.MyServicesListFragment;
import mg.mbds.nfcx_change.Fragment.MyTokenFragment;
import mg.mbds.nfcx_change.Fragment.ProductListFragment;
import mg.mbds.nfcx_change.Fragment.ViewPagerFragment;
import mg.mbds.nfcx_change.Model.ClassMapTable;
import mg.mbds.nfcx_change.Model.StringWithTag;
import mg.mbds.nfcx_change.Model.TypeService;
import mg.mbds.nfcx_change.Model.Utilisateur;
import mg.mbds.nfcx_change.Service.CustomAsyncTask;
import mg.mbds.nfcx_change.Service.GenericAsyncTask;
import mg.mbds.nfcx_change.Service.JSONParser;
import mg.mbds.nfcx_change.Session.SessionManager;
import mg.mbds.nfcx_change.Utilitaire.Url_Base;
import mg.mbds.nfcx_change.Utilitaire.Utilitaire;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private NavigationView nav_view_right;
    private DrawerLayout drawer;
    private RangeBar rangeBar;
    private TextView min_price;
    private TextView max_price;
    private MaterialSpinner spinnerTypeService;
    private TabLayout tabLayout;

    /*User*/
    private TextView userName;
    private TextView userMail;
    private ImageView userAvatar;

    private Activity activity;
    private AppBarLayout app_bar;

    private Utilisateur utilisateur;
    private Utilisateur utilisateur2;
    private Button logout;

    /*recherche*/
    private EditText serviceName;
    private MaterialSpinner spinner_type_service;
    private RangeBar range_price;
    private HashMap<String,String> hash_spinner_type_service = new HashMap<>();
    private String idSelectedTypeService;
    private String selectedTypeService;
    private Button rechercher;

    public MainActivity() {
        super(R.layout.activity_main);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init(){
        initView();
        initToolBar();
        initFragmentShowFirst();
        onClickSearchButton();
        movePriceBar();
        addSpinnerTypeServiceData();
        setDataUser();
        logout();
    }

    public void initView(){
        navigationView = findViewById(R.id.nav_view);
        nav_view_right = findViewById(R.id.nav_view_right);
        navigationView.setNavigationItemSelectedListener(this);
        rangeBar = (RangeBar) findViewById(R.id.range_price);
        min_price = (TextView)findViewById(R.id.min_price);
        max_price = (TextView) findViewById(R.id.max_price);
        spinnerTypeService = (MaterialSpinner) findViewById(R.id.spinner_type_service);
        setTabLayout((TabLayout) findViewById(R.id.tab));
        setFab((FloatingActionButton) findViewById(R.id.fab));
        app_bar = findViewById(R.id.app_bar);


        View headerLayout = navigationView.getHeaderView(0);
        userName = headerLayout.findViewById(R.id.user_session_name);
        userMail = headerLayout.findViewById(R.id.user_session_mail);
        userAvatar = headerLayout.findViewById(R.id.user_avatar);

        logout = navigationView.findViewById(R.id.logout);

        /*recherche*/
        setServiceName((EditText) nav_view_right.findViewById(R.id.serviceName));
        setSpinner_type_service((MaterialSpinner) nav_view_right.findViewById(R.id.spinner_type_service));
        setRange_price((RangeBar) nav_view_right.findViewById(R.id.range_price));
        setRechercher((Button) nav_view_right.findViewById(R.id.rechercher));

    }

    public void setDataUser(){
        SessionManager sessionManager = new SessionManager();
        String userName_String = sessionManager.getPreferences(this,"UserName") + " " +  sessionManager.getPreferences(this,"UserFirstName");
        String avatar = sessionManager.getPreferences(this,"UserAvatar");

        userName.setText(userName_String);
        userMail.setText(sessionManager.getPreferences(this,"UserMailAddress"));

        Utilisateur utilisateur = (Utilisateur) (getIntent().getSerializableExtra("user"));
        if(utilisateur == null){
            utilisateur = new Utilisateur();
            utilisateur.setId(sessionManager.getPreferences(this,"UserId"));
            utilisateur.setName(sessionManager.getPreferences(this,"UserName"));
            utilisateur.setFirstname(sessionManager.getPreferences(this,"UserFirstName"));
            utilisateur.setMail(sessionManager.getPreferences(this,"UserMailAddress"));
            utilisateur.setAvatar(sessionManager.getPreferences(this,"UserAvatar"));
            utilisateur.setToken(Double.parseDouble(sessionManager.getPreferences(this,"Token")));
        }
        setUtilisateur(utilisateur);

        if(!avatar.isEmpty() && avatar != null && avatar != "") {
            //Utilitaire.downloadImage(this,userAvatar,avatar);
            Utilitaire.decodeBase64Image(this,userAvatar,avatar);
        }
    }


    public void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    public void setTabInvisible(){
        getTabLayout().setVisibility(View.INVISIBLE);
    }

    public void initFragmentShowFirst(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ProductListFragment(),"Product_List_Fragment").commit();

    }

    public void onClickSearchButton(){
        getFab().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
    }

    public void movePriceBar(){
        rangeBar.setTickHeight(0);
        rangeBar.setBarColor(this.getResources().getColor(R.color.colorPrimary));
        rangeBar.setConnectingLineColor(this.getResources().getColor(R.color.colorPrimary));
        rangeBar.setThumbColorNormal(this.getResources().getColor(R.color.colorPrimary));
        rangeBar.setThumbColorPressed(this.getResources().getColor(R.color.colorPrimaryDark));
        rangeBar.setThumbRadius(10);
        rangeBar.setTickCount(Integer.valueOf(getResources().getString(R.string.max_price)) + 1);
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1) {
                min_price.setText(i + "");
                max_price.setText(i1 + "");
            }
        });
    }

    public void addSpinnerTypeServiceData(){
        GenericAsyncTask genericAsyncTask = new GenericAsyncTask() {
            @Override
            public void execute(List<ClassMapTable> classMapTables) {
                List<StringWithTag> listTypeService = new ArrayList<>();
                listTypeService.add(new StringWithTag(getResources().getString(R.string.selectionner),"","Tout"));
                for(int i = 0;i<classMapTables.size();i++){
                    TypeService typeService = (TypeService) classMapTables.get(i);
                    listTypeService.add(new StringWithTag(typeService.getTypeService(),typeService.getId(),typeService.getTypeService()));
                }
                ArrayAdapter<StringWithTag> adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,listTypeService);
                spinnerTypeService.setAdapter(adapter);
            }
        };
        HashMap<String,Object> parameter = new HashMap<>();
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(genericAsyncTask, Url_Base.URL_TYPE_SERVICE,parameter,null, JSONParser.GET, new TypeService());
        customAsyncTask.execute();
    }

    public void initTabLayout(){
        getTabLayout().removeAllTabs();
        getTabLayout().setVisibility(View.VISIBLE);
        TabLayout.Tab tab_Buy = getTabLayout().newTab();
        TabLayout.Tab tab_Xchange = getTabLayout().newTab();

        tab_Buy.setText(getResources().getString(R.string.buy));
        tab_Xchange.setText(getResources().getString(R.string.xchange));

        getTabLayout().addTab(tab_Buy,0);
        getTabLayout().addTab(tab_Xchange,1);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment;
        String tagFramgent = "";
        switch (id){
            case R.id.list_product:
                tagFramgent = "Product_List_Fragment";
                fragment = new ProductListFragment();
                break;
            case R.id.my_shop:
                tagFramgent = "View Page Fragment";
                fragment = new ViewPagerFragment();
                break;
            case R.id.shop_fragment:
                tagFramgent = "View Page Fragment";
                fragment = new ViewPagerFragment();
                break;
            case R.id.my_request:
                fragment = new DemandeListFragment();
                break;
            case R.id.my_product:
                fragment = new MyServicesListFragment();
                break;
            case R.id.my_tokens:
                fragment = new MyTokenFragment();
                break;
            case R.id.my_profile:
                tagFramgent = "Product_List_Fragment";
                fragment = new ProductListFragment();
                break;
            case R.id.language:
                fragment = new ChoixLangue();
                break;
            default:
                tagFramgent = "Product_List_Fragment";
                fragment = new ProductListFragment();
                break;

        }
        if(id != R.id.language) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tagFramgent).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            ((ChoixLangue) fragment).show(getSupportFragmentManager(),"");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.shop_fragment:
                fragment = new ViewPagerFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, "").commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void logout(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager();
                sessionManager.remove(getActivity(),"UserId");
                sessionManager.remove(getActivity(),"UserName");
                sessionManager.remove(getActivity(),"UserFirstName");
                sessionManager.remove(getActivity(),"UserMailAddress");
                sessionManager.remove(getActivity(),"UserAvatar");
                sessionManager.remove(getActivity(),"UserBirthdayDate");
                sessionManager.remove(getActivity(),"Token");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void closeRightNavigation(){
        drawer.closeDrawer(GravityCompat.END);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    public Activity getActivity() {
        return this;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Override
    public void onBackPressed() {
        app_bar.setExpanded(true);
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur2() {
        return utilisateur2;
    }

    public void setUtilisateur2(Utilisateur utilisateur2) {
        this.utilisateur2 = utilisateur2;
    }

    public EditText getServiceName() {
        return serviceName;
    }

    public void setServiceName(EditText serviceName) {
        this.serviceName = serviceName;
    }

    public MaterialSpinner getSpinner_type_service() {
        return spinner_type_service;
    }

    public void setSpinner_type_service(MaterialSpinner spinner_type_service) {
        this.spinner_type_service = spinner_type_service;
    }

    public RangeBar getRange_price() {
        return range_price;
    }

    public void setRange_price(RangeBar range_price) {
        this.range_price = range_price;
    }

    public HashMap<String, String> getHash_spinner_type_service() {
        return hash_spinner_type_service;
    }

    public void setHash_spinner_type_service(HashMap<String, String> hash_spinner_type_service) {
        this.hash_spinner_type_service = hash_spinner_type_service;
    }

    public Button getRechercher() {
        return rechercher;
    }

    public void setRechercher(Button rechercher) {
        this.rechercher = rechercher;
    }

    public String getIdSelectedTypeService() {
        return idSelectedTypeService;
    }

    public void setIdSelectedTypeService(String idSelectedTypeService) {
        this.idSelectedTypeService = idSelectedTypeService;
    }

    public String getSelectedTypeService() {
        return selectedTypeService;
    }

    public void setSelectedTypeService(String selectedTypeService) {
        this.selectedTypeService = selectedTypeService;
    }
}
