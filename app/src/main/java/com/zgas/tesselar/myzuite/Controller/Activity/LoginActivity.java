package com.zgas.tesselar.myzuite.Controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zgas.tesselar.myzuite.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "LoginActivity";

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(DEBUG_TAG, "OnCreate");
        initUi();
    }

    private void initUi() {
        getSupportActionBar().hide();
        mEmail = (EditText) findViewById(R.id.activity_login_et_email);
        mPassword = (EditText) findViewById(R.id.activity_login_et_password);
        mLogin = (Button) findViewById(R.id.activity_login_btn_login);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_btn_login:
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);
                this.finish();
                break;
        }
    }
}
