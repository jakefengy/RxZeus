package com.xm.zeus.view.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xm.zeus.R;
import com.xm.zeus.view.home.adapter.RecentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Fragment_Recent extends Fragment {

    @Bind(R.id.recent_list)
    RecyclerView recyclerView;

    public Fragment_Recent() {

    }

    public static Fragment_Recent newInstance() {
        return new Fragment_Recent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init();
    }

    private void init() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<String> items = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            items.add("item " + i);
        }

        RecentAdapter adapter = new RecentAdapter(getActivity(), items);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
