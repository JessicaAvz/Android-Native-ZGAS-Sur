package com.zgas.tesselar.myzuite.View.Activity;

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
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * This class manages the login of the user for its data to be used throught the application.
 * In this case, we use two different users to login: the admin and the operator.
 * We execute the login asynctask, we get the admin data, and then we execute the getuserinfo
 * asynctask, to get the user information.
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see Login
 * @see UserPreferences
 * @see Bundle
 * @see android.os.AsyncTask
 * @see LoginTask
 * @see GetUserInfoTask
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        LoginTask.LoginTaskListener, GetUserInfoTask.UserInfoListener {

    private static final String DEBUG_TAG = "LoginActivity";
    private static final String EMAIL_TAG = "email";
    private static final String PASS_TAG = "password";
    private static final String ADMIN_TOKEN = "access_token";

    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mLogin;
    private UserPreferences mUserPreferences;
    private Context context;
    private String email;
    private String password;

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
        mEmail = findViewById(R.id.activity_login_et_email);
        mPassword = findViewById(R.id.activity_login_et_password);
        mLogin = findViewById(R.id.activity_login_btn_login);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_btn_login:
                callAsyncTask();
                break;
        }
    }

    private void callAsyncTask() {
        if (isEmpty(mEmail) || isEmpty(mPassword)) {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese todos los datos.", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject params = new JSONObject();
            email = mEmail.getText().toString();
            password = mPassword.getText().toString();
            try {
                params.put(EMAIL_TAG, email);
                params.put(PASS_TAG, password);
                Log.d(DEBUG_TAG, "Parámetro: " + params.getString(EMAIL_TAG) + " " + params.getString(PASS_TAG));

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
            mUserPreferences.setLoginData(login);
            params.put(EMAIL_TAG, mEmail.getText().toString());
            params.put(ADMIN_TOKEN, mUserPreferences.getLoginObject().getLoginAccessToken());
            mUserPreferences.createLoginSession(email);

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
        mUserPreferences.setUserData(user);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        this.finish();
    }

    @Override
    public void userSupervisedSuccessResponse(List<User> userList) {

    }
}

