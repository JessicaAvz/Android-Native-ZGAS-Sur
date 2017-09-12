package com.zgas.tesselar.myzuite.Controller.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "LoginActivity";

    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mLogin;
    private User userToJson;
    private Intent mainIntent;

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
        if (isEmpty(mEmail) || isEmpty(mPassword)) {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese todos los datos.", Toast.LENGTH_SHORT).show();
        } else {
            userToJson = new User();
            userToJson.setUserId(1001);
            userToJson.setUserType(User.userType.OPERATOR);
            userToJson.setUserName("Mario");
            userToJson.setUserLastname("López");
            userToJson.setUserEmail("j.arvizutr@gmail.com");
            userToJson.setUserPassword("j789");
            userToJson.setUserRoute("Ruta 1");
            userToJson.setUserZone("Zona 1");
            userToJson.setUserstatus(User.userStatus.ACTIVE);
            String json = userToJson.toJson();
            Log.d(DEBUG_TAG, json);

            User userFromJson = User.fromJson(json);
            Log.d(DEBUG_TAG, "userid: " + userFromJson.getUserId() + " - userType: " + userFromJson.getUserType()
                    + " - userName: " + userFromJson.getUserName() + " - userLastname: " + userFromJson.getUserLastname()
                    + " - userEmail: " + userFromJson.getUserEmail() + " - userPassword: " + userFromJson.getUserPassword()
                    + " - userRoute: " + userFromJson.getUserRoute() + " - userZone:" + userFromJson.getUserZone()
                    + " - userStatus: " + userFromJson.getUserstatus());

            mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            this.finish();

            UserPreference prefs = new UserPreference(this);
            if (prefs.getUser() != null) {
                Log.d(DEBUG_TAG, "El usuario no fue nulo. ");
            } else {
                Log.d(DEBUG_TAG, "El usuario fue nulo. ");
            }

            prefs.saveUser(userFromJson);
            if (prefs.getUser() != null) {
                Log.d(DEBUG_TAG, "El usuario no fue nulo. ");
            } else {
                Log.d(DEBUG_TAG, "El usuario fue nulo. ");
            }
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
