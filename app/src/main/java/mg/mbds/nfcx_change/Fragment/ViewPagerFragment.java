package mg.mbds.nfcx_change.Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mg.mbds.nfcx_change.Adapter.ShopViewPagerAdapter;
import mg.mbds.nfcx_change.MainActivity;
import mg.mbds.nfcx_change.R;

public class ViewPagerFragment extends Fragment {
    View view;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private int typeRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.view_pager, container, false);
        this.view = view;
        init();
        return view;
    }

    public void init(){
        initView();
        if(getArguments() != null)
            typeRequest = getArguments().getInt("typeRequest");
        setBanner();

        initPageAdapter();
        ((MainActivity)getActivity()).getTabLayout().setupWithViewPager(viewPager);
        ((MainActivity)getActivity()).initTabLayout();


    }

    public void setBanner(){
        ImageView banner = ((MainActivity)getActivity()).findViewById(R.id.banner_img);
        banner.setImageDrawable(getResources().getDrawable(R.drawable.phone6));
    }

    public  void initView(){
        viewPager = view.findViewById(R.id.view_pager_shop);
        getActivity().setTitle("");

    }

    public void initPageAdapter(){
        pagerAdapter = new ShopViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //((MainActivity)getActivity()).getTabLayout().setupWithViewPager(viewPager);
    }


    @Override
    public void onResume() {
        super.onResume();
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(typeRequest);
            }
        }, 100);
    }
}
