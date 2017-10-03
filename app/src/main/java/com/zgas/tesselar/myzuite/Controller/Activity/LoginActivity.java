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
    private static final String USER_ID = "userId";
    private static final String ADMIN_TOKEN = "access_token";

    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mLogin;
    private UserPreferences userPreferences;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        userPreferences = new UserPreferences(this);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        initUi();

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

                Log.d(DEBUG_TAG, "Parámetro: " + params.getString(EMAIL_TAG));
                Log.d(DEBUG_TAG, "Parámetro: " + params.getString(PASS_TAG));

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

            userPreferences.setLoginToken(login.getLoginAccessToken());
            userPreferences.setLoginId(login.getLoginId());
            userPreferences.setLoginInstanceUrl(login.getLoginInstanceUrl());
            userPreferences.setLoginSignature(login.getLoginSignature());
            userPreferences.setLoginIssuedAt(login.getLoginIssuedAt());
            userPreferences.setLoginTokenType(login.getLoginTokenType());
            userPreferences.setLoginEmail(mEmail.getText().toString());

            userPreferences.setLoginData(login);

            Log.d(DEBUG_TAG, "Login preference token: " + userPreferences.getLoginToken());
            Log.d(DEBUG_TAG, "Login preference id: " + userPreferences.getLoginId());
            Log.d(DEBUG_TAG, "Login preference instance url: " + userPreferences.getLoginInstanceUrl());
            Log.d(DEBUG_TAG, "Login preference issued at: " + userPreferences.getLoginIssuedAt());
            Log.d(DEBUG_TAG, "Login preference signature: " + userPreferences.getLoginSignature());
            Log.d(DEBUG_TAG, "Login preference token type: " + userPreferences.getLoginTokenType());
            Log.d(DEBUG_TAG, "Login preference email: " + userPreferences.getLoginEmail());

            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            params.put(EMAIL_TAG, userPreferences.getLoginEmail());
            params.put(ADMIN_TOKEN, userPreferences.getLoginToken());

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
        //UserPreferences userPreferences = new UserPreferences(this);
        //userPreferences.setUser(user);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}

