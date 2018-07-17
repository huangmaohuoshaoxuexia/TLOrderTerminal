package tl.com.tlsl.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 2018/5/16.
 */

public class FragmentAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> mList;
    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }
}
