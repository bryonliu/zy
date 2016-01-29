package com.tencent.zy.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tencent.zy.exception.ArgumentException;
import com.tencent.zy.ui.fragment.HomePageFragment;
import com.tencent.zy.ui.fragment.OtherFragment;
import com.tencent.zy.ui.fragment.PersonInfoFragment;
import com.tencent.zy.ui.fragment.ResuceStateFragment;

/**
 * Created by bryonliu on 2016/1/25.
 */
public class MainAdapter extends FragmentPagerAdapter {

    private static final int TAB_NUM = 4;

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0:
                return new HomePageFragment();
            case 1:
                return new ResuceStateFragment();
            case 2:
                return new OtherFragment();
            case 3:
                return new PersonInfoFragment();
            default:
                throw new ArgumentException();
        }
    }

    @Override
    public int getCount() {
        return TAB_NUM;
    }
}
