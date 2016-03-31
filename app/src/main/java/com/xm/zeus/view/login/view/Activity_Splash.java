package com.xm.zeus.view.login.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xm.zeus.R;
import com.xm.zeus.utils.Logger;
import com.xm.zeus.view.login.interactor.ISplashInteractor;
import com.xm.zeus.view.login.presenter.ISplashPresenter;
import com.xm.zeus.view.login.presenter.SplashPresenterImpl;

public class Activity_Splash extends AppCompatActivity implements ISplashView {

    private final static String TAG = "SplashTag";

    private ISplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Logger.i(TAG, "onCreate");
        presenter = new SplashPresenterImpl(this, this);
        presenter.init();

    }

    @Override
    public void initStart() {
        Logger.i(TAG, "initStart");
    }

    @Override
    public void toHome() {
        Logger.i(TAG, "toHome");
    }

    @Override
    public void toLogin() {
        Logger.i(TAG, "toLogin");
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
