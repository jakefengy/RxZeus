package com.xm.zeus.view.home.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xm.zeus.R;
import com.xm.zeus.extend.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Fragment_Contacts extends Fragment {

    @Bind(R.id.iv_fragment_me)
    CircleImageView ivAvatar;
    @Bind(R.id.tv_fragment_me_name)
    TextView txtName;
    @Bind(R.id.rl_header)
    RelativeLayout rlHeader;
    @Bind(R.id.rl_action_botton_set)
    RelativeLayout btnSetting;
    @Bind(R.id.iv_about_remind)
    ImageView ivRemind;
    @Bind(R.id.rl_action_botton_about)
    RelativeLayout btnAbout;
    @Bind(R.id.btn_login_out)
    RelativeLayout btnLoginOut;

    public Fragment_Contacts() {

    }

    public static Fragment_Contacts newInstance() {
        return new Fragment_Contacts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_header, R.id.rl_action_botton_set, R.id.rl_action_botton_about, R.id.btn_login_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_header:
                break;
            case R.id.rl_action_botton_set:
                break;
            case R.id.rl_action_botton_about:
                break;
            case R.id.btn_login_out:
                break;
        }
    }
}
