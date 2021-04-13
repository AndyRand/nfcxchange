package mg.mbds.nfcx_change.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import mg.mbds.nfcx_change.Fragment.ShopBuyListFragment;
import mg.mbds.nfcx_change.Fragment.ShopXchangeListFragment;
import mg.mbds.nfcx_change.Fragment.ViewPagerFragment;

public class ShopViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();

    public ShopViewPagerAdapter(FragmentManager fm) {
        super(fm);
        addFragment();

    }

    public void addFragment(){
        fragmentList.add(new ShopBuyListFragment());
        fragmentList.add(new ShopXchangeListFragment());
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
