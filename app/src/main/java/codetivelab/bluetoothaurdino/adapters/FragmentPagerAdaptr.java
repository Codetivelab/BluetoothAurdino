package codetivelab.bluetoothaurdino.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentPagerAdaptr extends android.support.v4.app.FragmentPagerAdapter {
    private final List<Fragment> mFragmentList;
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public FragmentPagerAdaptr(List<Fragment> mFragmentList, FragmentManager manager) {
        super(manager);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}