package com.xm.zeus.view.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xm.zeus.R;
import com.xm.zeus.utils.Logger;
import com.xm.zeus.utils.RxBus;
import com.xm.zeus.utils.entity.rxbus.TokenError;
import com.xm.zeus.view.home.view.Activity_Home;
import com.xm.zeus.view.login.iview.ISplashView;
import com.xm.zeus.view.login.presenter.ISplashPresenter;
import com.xm.zeus.view.login.presenter.SplashPresenterImpl;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class Activity_Splash extends AppCompatActivity implements ISplashView {

    private final static String TAG = "SplashTag";

    private ISplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        registerRxBus();
        Logger.i(TAG, "onCreate");
        presenter = new SplashPresenterImpl(this, this);
        presenter.init();

    }

    private void registerRxBus() {
        RxBus.getInstance().toObservable(TokenError.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TokenError>() {
                    @Override
                    public void call(TokenError tokenError) {
                        Intent intent = Activity_Login.getIntent(Activity_Splash.this);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    public void initStart() {
        Logger.i(TAG, "initStart");
    }

    @Override
    public void toHome() {

        Intent loginIntent = Activity_Home.getHomeIntent(Activity_Splash.this);
        startActivity(loginIntent);
        finish();

    }

    @Override
    public void toLogin() {
        Logger.i(TAG, "toLogin");
        Intent loginIntent = Activity_Login.getIntent(Activity_Splash.this);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

}
