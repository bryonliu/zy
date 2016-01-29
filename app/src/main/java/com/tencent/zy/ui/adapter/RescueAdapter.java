package com.tencent.zy.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.zy.R;
import com.tencent.zy.entity.RescueMessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bryonliu on 2016/1/28.
 */
public class RescueAdapter extends BaseAdapter {
    private List<RescueMessage> rescueMessages = new ArrayList<>();

    private LayoutInflater inflater;
    private Context mContext;

    public RescueAdapter(Context mContext) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return rescueMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return rescueMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_rescue_state, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RescueMessage rescueMessage = rescueMessages.get(position);
        viewHolder.tvNickName.setText(rescueMessage.toPerson.nickName);
        viewHolder.tvRescueState.setText(rescueMessage.rescurState.des());
        Glide.with(mContext).load(rescueMessage.toPerson.avatarUrl).crossFade().into(viewHolder.ivAvatar);
        return convertView;
    }

    public void clear() {
        rescueMessages.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder {

        @Bind(R.id.tv_nickname)
        TextView tvNickName;
        @Bind(R.id.tv_rescue_state)
        TextView tvRescueState;

        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void update(List<RescueMessage> rescueMessages) {
        this.rescueMessages = rescueMessages;
        notifyDataSetChanged();
    }
}
