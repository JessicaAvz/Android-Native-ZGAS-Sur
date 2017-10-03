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
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String GRANT_TYPE = "grant_type";
    private static final String USER_ID = "userId";

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
        //if (isEmpty(mEmail) || isEmpty(mPassword)) {
//            Toast.makeText(getApplicationContext(), "Por favor, ingrese todos los datos.", Toast.LENGTH_SHORT).show();
        //      } else {
        JSONObject params = new JSONObject();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        try {
            params.put(EMAIL_TAG, email);
            params.put(PASS_TAG, password);

            LoginTask loginTask = new LoginTask(context, params);
            loginTask.setLoginTaskListener(this);
            loginTask.execute();


        } catch (Exception e) {
            e.printStackTrace();
        }
        //}
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
        //try {
        //login.setLoginPassword(mPassword.getText().toString());
        userPreferences.setToken(login.getLoginAccessToken());
        userPreferences.setLoginData(login);

        //  GetUserInfoTask userInfoTask = new GetUserInfoTask(this, new JSONObject().put(USER_ID, login.getLoginId()));
//            userInfoTask.setUserInfoListener(this);
//            userInfoTask.execute();

        //} catch (JSONException e) {
//            e.printStackTrace();
        //}
    }

    @Override
    public void userInfoErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void userInfoSuccessResponse(User user) {
        UserPreferences userPreferences = new UserPreferences(this);
        userPreferences.setUser(user);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
