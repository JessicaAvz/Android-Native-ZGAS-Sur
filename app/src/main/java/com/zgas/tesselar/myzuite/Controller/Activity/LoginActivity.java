package com.zgas.tesselar.myzuite.Controller.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Model.Login;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.GetUserInfoTask;
import com.zgas.tesselar.myzuite.Service.LoginTask;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginTask.LoginTaskListener, GetUserInfoTask.UserInfoListener {

    private static final String DEBUG_TAG = "LoginActivity";
    private static final String EMAIL_TAG = "email";
    private static final String PASS_TAG = "password";
    private static final String ADMIN_EMAIL_TAG = "adminEmail";
    private static final String ADMIN_PASS_TAG = "adminPass";
    private static final String USER_ID = "userId";
    private static final String ADMIN_TOKEN = "access_token";

    private static final String ADMIN_EMAIL = "mbravo@grupozeta.biz.dev1";
    private static final String ADMIN_PASSWORD = "sfgrupozeta16";


    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mLogin;
    private UserPreferences mUserPreferences;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(this);

        if (mUserPreferences.isLoggedIn()) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        } else {
            setContentView(R.layout.activity_login);
            Log.d(DEBUG_TAG, "OnCreate");
            initUi();
        }
    }

    private void initUi() {
        getSupportActionBar().hide();
        mEmail = (TextInputEditText) findViewById(R.id.activity_login_et_email);
        mPassword = (TextInputEditText) findViewById(R.id.activity_login_et_password);
        mLogin = (Button) findViewById(R.id.activity_login_btn_login);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_btn_login:
                paramsCapture();
                break;
        }
    }

    private void paramsCapture() {
        if (isEmpty(mEmail) || isEmpty(mPassword)) {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese todos los datos.", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject params = new JSONObject();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            try {
                params.put(EMAIL_TAG, email);
                params.put(PASS_TAG, password);
                params.put(ADMIN_EMAIL_TAG, ADMIN_EMAIL);
                params.put(ADMIN_PASS_TAG, ADMIN_PASSWORD);

                Log.d(DEBUG_TAG, "Parámetro: " + params.getString(EMAIL_TAG));
                Log.d(DEBUG_TAG, "Parámetro: " + params.getString(PASS_TAG));
                Log.d(DEBUG_TAG, "Parámetro: " + params.getString(ADMIN_EMAIL_TAG));
                Log.d(DEBUG_TAG, "Parámetro: " + params.getString(ADMIN_PASS_TAG));

                LoginTask loginTask = new LoginTask(this, params);
                loginTask.setLoginTaskListener(this);
                loginTask.execute();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void loginErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccessResponse(Login login) {
        try {
            JSONObject params = new JSONObject();
            login.setLoginAccessToken(login.getLoginAccessToken());
            login.setLoginId(login.getLoginId());
            login.setLoginInstanceUrl(login.getLoginInstanceUrl());
            login.setLoginIssuedAt(login.getLoginIssuedAt());
            login.setLoginSignature(login.getLoginSignature());
            login.setLoginTokenType(login.getLoginTokenType());
            login.setLoginEmail(login.getLoginEmail());

            mUserPreferences.setLoginToken(login.getLoginAccessToken());
            mUserPreferences.setLoginId(login.getLoginId());
            mUserPreferences.setLoginInstanceUrl(login.getLoginInstanceUrl());
            mUserPreferences.setLoginSignature(login.getLoginSignature());
            mUserPreferences.setLoginIssuedAt(login.getLoginIssuedAt());
            mUserPreferences.setLoginTokenType(login.getLoginTokenType());
            mUserPreferences.setLoginEmail(mEmail.getText().toString());

            mUserPreferences.setLoginData(login);

            Log.d(DEBUG_TAG, "Login preference token: " + mUserPreferences.getLoginToken());
            Log.d(DEBUG_TAG, "Login preference id: " + mUserPreferences.getLoginId());
            Log.d(DEBUG_TAG, "Login preference instance url: " + mUserPreferences.getLoginInstanceUrl());
            Log.d(DEBUG_TAG, "Login preference issued at: " + mUserPreferences.getLoginIssuedAt());
            Log.d(DEBUG_TAG, "Login preference signature: " + mUserPreferences.getLoginSignature());
            Log.d(DEBUG_TAG, "Login preference token type: " + mUserPreferences.getLoginTokenType());
            Log.d(DEBUG_TAG, "Login preference email: " + mUserPreferences.getLoginEmail());

            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            params.put(EMAIL_TAG, mUserPreferences.getLoginEmail());
            params.put(ADMIN_TOKEN, mUserPreferences.getLoginToken());

            GetUserInfoTask userInfoTask = new GetUserInfoTask(this, params);
            userInfoTask.setUserInfoListener(this);
            userInfoTask.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void userInfoErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void userInfoSuccessResponse(User user) {
        //UserPreferences mUserPreferences = new UserPreferences(this);
        //mUserPreferences.setUser(user);
        mUserPreferences.createLoginSession(mUserPreferences.getLoginEmail());
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}

