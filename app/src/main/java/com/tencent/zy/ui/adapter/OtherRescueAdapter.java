package com.tencent.zy.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.android.talk.IMCloudCallback;
import com.tencent.android.talk.IMCloudManager;
import com.tencent.zy.R;
import com.tencent.zy.utils.HandlerUtils;
import com.tencent.zy.entity.RescueMessage;
import com.tencent.zy.orm.file.AccountConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bryonliu on 2016/1/28.
 */
public class OtherRescueAdapter extends BaseAdapter {
    private static final String TAG = "bryon";
    private List<RescueMessage> rescueMessages = new ArrayList<>();

    private LayoutInflater inflater;
    private Context mContext;

    public OtherRescueAdapter(Context mContext) {
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_other_rescue_state, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final RescueMessage rescueMessage = rescueMessages.get(position);
        viewHolder.tvNickName.setText(rescueMessage.fromPerson.nickName);

        viewHolder.tvRescueState.setText(rescueMessage.rescurState == RescueMessage.RescurState.WAIT ? "是否接受"
                : rescueMessage.rescurState.des());

        if (rescueMessage.rescurState != RescueMessage.RescurState.WAIT) {

            viewHolder.tvRescueText.setClickable(false);
        }
        viewHolder.tvRescueState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RescueMessage acceptMsg = new RescueMessage();
                acceptMsg.fromPerson = AccountConfig.getAccount();
                acceptMsg.toPerson = rescueMessage.fromPerson;
                acceptMsg.rescurState = RescueMessage.RescurState.ACCEPT;
                IMCloudManager.sendMsgToUserId(mContext, rescueMessage.fromPerson.openId, acceptMsg.toJsonString(),
                        new IMCloudCallback() {
                    @Override
                    public void onSuccess(Object o, int i) {
                        rescueMessage.rescurState = RescueMessage.RescurState.ACCEPT;
                        HandlerUtils.getMainHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                viewHolder.tvRescueState.setText(rescueMessage.rescurState.des());
                            }
                        });

                    }

                    @Override
                    public void onFail(Object o, int i, String s) {
                        Log.e(TAG, "fail to send msg to " + rescueMessage.fromPerson.openId);
                    }
                });
            }
        });

        viewHolder.tvRescueText.setText(rescueMessage.text);

        Glide.with(mContext).load(rescueMessage.fromPerson.avatarUrl).crossFade().into(viewHolder.ivAvatar);

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

        @Bind(R.id.tv_text)
        TextView tvRescueText;

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
