package com.xm.zeus.view.contacts.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xm.zeus.R;
import com.xm.zeus.app.Constant;
import com.xm.zeus.db.app.entity.Friend;
import com.xm.zeus.extend.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：小孩子xm on 2016-04-12 12:39
 * 邮箱：1065885952@qq.com
 */
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {

    private final Context mActivity;
    private List<Friend> mDataSource;
    private OnItemClickListener mItemClickListener;


    public FriendAdapter(Context context, List<Friend> friends) {
        this.mActivity = context;
        this.mDataSource = new ArrayList<>();
        if (friends != null) {
            mDataSource.addAll(friends);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(mActivity);
        final View sView = mInflater.inflate(R.layout.listitem_friend, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friend friend = mDataSource.get(position);
        holder.vName.setText(friend.getUsername());
        holder.vDepartment.setText(friend.getCompany() + friend.getDept());
        if (holder.vDepartment.getText().equals("")) {
            holder.vDepartment.setVisibility(View.GONE);
        }
        Uri imageUri = Uri.parse(Constant.ImageUrl + friend.getAvatarid());
        holder.vAvatar.setImageURI(imageUri);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataSource.get(position).hashCode();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return Character.toString(mDataSource.get(pos).getSpelling().charAt(0));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView vName, vDepartment;
        SimpleDraweeView vAvatar;

        public ViewHolder(View view) {
            super(view);
            vName = (TextView) view.findViewById(R.id.tv_friend_name);
            vDepartment = (TextView) view.findViewById(R.id.tv_friend_org);
            vAvatar = (SimpleDraweeView) view.findViewById(R.id.iv_friend_head);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, mDataSource.get(getLayoutPosition()));
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, Friend friend);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void updateSource(List<Friend> data) {
        if (data == null) {
            return;
        }
        mDataSource.clear();
        mDataSource.addAll(data);
        notifyDataSetChanged();
    }
}
