package com.xm.zeus.view.login.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.xm.zeus.R;
import com.xm.zeus.app.Constant;
import com.xm.zeus.utils.Logger;
import com.xm.zeus.utils.Tip;
import com.xm.zeus.utils.Utils;
import com.xm.zeus.view.login.presenter.ILoginPresenter;
import com.xm.zeus.view.login.presenter.LoginPresenterImpl;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_Login extends AppCompatActivity implements ILoginView {

    private final static String TAG = Activity_Login.class.getName();

    @Bind(R.id.login_et_username)
    EditText loginEtUsername;
    @Bind(R.id.login_et_psw)
    EditText loginEtPsw;
    @Bind(R.id.login_btn_login)
    Button loginBtnLogin;

    public static Intent getLoginIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Activity_Login.class);
        return intent;
    }

    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Logger.i(TAG, "onCreate");
        loginPresenter = new LoginPresenterImpl(this, this);
    }

    @Override
    public void error(String msg) {
        Tip.toast(this, msg);
    }

    @Override
    public void toHome() {
        Logger.i(TAG, "toHome");
    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.login_btn_login)
    public void onClick() {
        Logger.i(TAG, "login");
        String username = loginEtUsername.getText().toString();
        String psw = loginEtPsw.getText().toString();
        loginPresenter.login(username, psw, Constant.Organization, "");
    }
}
