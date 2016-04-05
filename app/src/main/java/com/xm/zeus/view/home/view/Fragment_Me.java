package com.xm.zeus.view.home.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xm.zeus.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Fragment_Me extends Fragment {


    public Fragment_Me() {

    }

    public static Fragment_Me newInstance() {
        return new Fragment_Me();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        return view;
    }


}
