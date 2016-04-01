package com.xm.zeus.view.login.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xm.zeus.R;

public class Activity_Login extends AppCompatActivity implements ILoginView {

    public static Intent getLoginIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Activity_Login.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void loginFail() {

    }

    @Override
    public void downloadContactsFail() {

    }

    @Override
    public void toHome() {

    }
}
