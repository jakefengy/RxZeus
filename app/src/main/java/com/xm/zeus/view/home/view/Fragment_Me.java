package com.xm.zeus.view.home.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xm.zeus.R;
import com.xm.zeus.app.Constant;
import com.xm.zeus.db.app.entity.Colleague;
import com.xm.zeus.view.home.entity.CheckVersionResult;
import com.xm.zeus.view.home.iview.IMeView;
import com.xm.zeus.view.home.presenter.IMePresenter;
import com.xm.zeus.view.home.presenter.MePresenterImpl;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Fragment_Me extends Fragment implements IMeView {

    private IMePresenter mePresenter;

    @Bind(R.id.tv_fragment_me_name)
    TextView txtName;
    @Bind(R.id.tv_fragment_me_motto)
    TextView txtMotto;
    @Bind(R.id.iv_fragment_me)
    SimpleDraweeView ivAvatar;
    @Bind(R.id.rl_header)
    RelativeLayout btnHeader;
    @Bind(R.id.rl_action_botton_set)
    RelativeLayout btnSetting;
    @Bind(R.id.iv_about_remind)
    ImageView ivRemind;
    @Bind(R.id.rl_action_botton_about)
    RelativeLayout btnAbout;
    @Bind(R.id.btn_login_out)
    RelativeLayout btnLoginOut;

    public Fragment_Me() {

    }

    public static Fragment_Me newInstance() {
        return new Fragment_Me();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mePresenter = new MePresenterImpl(getActivity(), this);
        mePresenter.getUserInfo();
        mePresenter.checkAppVersion();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mePresenter.onDestroy();
    }

    @OnClick({R.id.rl_header, R.id.rl_action_botton_set, R.id.rl_action_botton_about, R.id.btn_login_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_header:
                // to card
                break;
            case R.id.rl_action_botton_set:
                // to setting
                break;
            case R.id.rl_action_botton_about:
                // to about
                break;
            case R.id.btn_login_out:
//                mePresenter.loginOut();
                break;
        }
    }

    @Override
    public void setUserInfo(Colleague userInfo) {
        txtName.setText(userInfo.getUsername());
        txtMotto.setText("啊哈");
        Uri imageUri = Uri.parse(Constant.ImageUrl + userInfo.getAvatarid());
        ivAvatar.setImageURI(imageUri);

    }

    @Override
    public void updateApp(CheckVersionResult newVersion) {
        ivRemind.setVisibility(View.VISIBLE);
    }

    @Override
    public void signedOutComplete() {

    }

    @Override
    public void signedOutFail() {

    }

    @Override
    public void error(String error) {

    }

    private void to(Intent intent) {

    }


}
