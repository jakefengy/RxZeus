/*
 * Copyright (C) 2014 VenomVendor <info@VenomVendor.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xm.zeus.view.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xm.zeus.R;
import com.xm.zeus.app.Constant;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.db.app.entity.ColleagueDept;
import com.xm.zeus.db.app.helper.ColleagueHelper;
import com.xm.zeus.extend.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;


/**
 * ColleagueAdapter For Fragment_Colleague RecyclerView
 */
public class ColleagueAdapter extends RecyclerView.Adapter<ColleagueAdapter.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {

    private final Context mActivity;
    private List<Colleague> mDataSource;
    private OnItemClickListener mItemClickListener;

    private ColleagueHelper colleagueHelper;

    public ColleagueAdapter(Context context, List<Colleague> colleagueList) {
        this.mActivity = context;
        this.mDataSource = new ArrayList<>();
        if (colleagueList != null) {
            mDataSource.addAll(colleagueList);
        }

        colleagueHelper = new ColleagueHelper();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from(mActivity);
        final View sView = mInflater.inflate(R.layout.listitem_colleague, parent, false);
        return new ViewHolder(sView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Colleague colleague = mDataSource.get(position);
        holder.vName.setText(colleague.getUsername());
        ColleagueDept dept = colleagueHelper.getDefaultDeptByPersonId(colleague.getUid());
        if (dept == null) {
            holder.vDepartment.setText("");
        } else {
            holder.vDepartment.setText(dept.getName());
        }

        Uri imageUri = Uri.parse(Constant.ImageUrl + colleague.getAvatarid());
        holder.vAvatar.setImageURI(imageUri);
        if (colleague.getSex().equals("") || colleague.getSex().equals("男")) {

        } else {

        }
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataSource.get(position).hashCode();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView vName, vDepartment;
        SimpleDraweeView vAvatar;

        public ViewHolder(View view) {
            super(view);
            vName = (TextView) view.findViewById(R.id.tv_contacts_name);
            vDepartment = (TextView) view.findViewById(R.id.tv_contacts_org);
            vAvatar = (SimpleDraweeView) view.findViewById(R.id.iv_contacts_head);

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
        void onItemClick(View view, Colleague bizEnterpriseDirectory);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void updateSource(List<Colleague> data) {
        if (data == null) {
            return;
        }
        mDataSource.clear();
        mDataSource.addAll(data);
        notifyDataSetChanged();

    }

    public void updatePerson(Colleague person) {
        for (int i = 0; i < mDataSource.size(); i++) {
            if (mDataSource.get(i).getUid().equals(person.getUid())) {
                mDataSource.remove(i);
                mDataSource.add(i, person);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return Character.toString(mDataSource.get(pos).getSpelling().charAt(0));
    }
}
