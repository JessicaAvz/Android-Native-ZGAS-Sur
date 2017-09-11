package com.zgas.tesselar.myzuite.Controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "LoginActivity";
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String USER_ID = "userId";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_PASS = "userPassword";
    public static final String USER_NAME = "userName";
    public static final String USER_LASTNAME = "userLastname";
    public static final String USER_ROUTE = "userRoute";
    public static final String USER_ZONE = "userZone";
    public static final String USER_STATUS = "userStatus";
    public static final String USER_TYPE = "userType";

    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mLogin;
    private UserPreference usersPreferences;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(DEBUG_TAG, "OnCreate");
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
                login();
                break;
        }
    }

    private void login() {
        /*if (isEmpty(mEmail) || isEmpty(mPassword)) {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese todos los datos.", Toast.LENGTH_SHORT).show();
        } else {*/
        user = new User();
        user.setUserEmail(mEmail.toString());
        usersPreferences = new UserPreference(this);
        usersPreferences.setUserData(user);
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        this.finish();
        //}
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
