package com.xm.zeus.view.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xm.zeus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：小孩子xm on 2016-04-04 17:38
 * 邮箱：1065885952@qq.com
 */
public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentHolder> {

    private Context ctx;
    private List<String> dataList;
    private LayoutInflater inflater;

    public RecentAdapter(Context context, List<String> data) {
        this.ctx = context;
        this.dataList = new ArrayList<>();
        if (data != null) {
            dataList.addAll(data);
        }
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public RecentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecentHolder(inflater.inflate(R.layout.list_item_recent, parent, false));
    }

    @Override
    public void onBindViewHolder(RecentHolder holder, int position) {
        holder.name.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class RecentHolder extends RecyclerView.ViewHolder {
        TextView name;

        public RecentHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recent_item_name);
        }
    }

}
