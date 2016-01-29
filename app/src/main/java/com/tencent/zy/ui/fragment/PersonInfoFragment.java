package com.tencent.zy.ui.fragment;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.zy.R;
import com.tencent.zy.entity.Person;
import com.tencent.zy.orm.file.AccountConfig;

import butterknife.Bind;

/**
 * Created by bryonliu on 2016/1/25.
 */
public class PersonInfoFragment extends BaseFragment {
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;

    @Bind(R.id.tv_nickname)
    TextView tvNickName;

    @Bind(R.id.tv_category)
    TextView tvCategory;

    @Override
    public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_info, null);
        return view;
    }

    @Override
    protected void initView() {
        Person person = AccountConfig.getAccount();
        Glide.with(getActivity()).load(person.avatarUrl).into(ivAvatar);
        tvCategory.setText(person.catory.des);
        tvNickName.setText(person.nickName);
    }
}
