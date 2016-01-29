package com.tencent.zy.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.zy.R;

/**
 * Created by bryonliu on 2016/1/25.
 */
public class OtherFragment extends BaseFragment{
    @Override
    public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, null);
        return view;
    }
}
