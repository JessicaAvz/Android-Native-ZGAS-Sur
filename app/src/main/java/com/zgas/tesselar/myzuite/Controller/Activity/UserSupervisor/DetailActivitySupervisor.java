package com.zgas.tesselar.myzuite.Controller.Activity.UserSupervisor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Controller.Activity.MainActivity;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

public class DetailActivitySupervisor extends AppCompatActivity {

    private static final String DEBUG_TAG = "Det.Act.Supervisor";

    private Bundle mBundle;
    private int mIntUserId;
    private String mStrUserName;
    private String mStrUserLastname;
    private String mStrUserEmail;
    private String mStrUserRoute;
    private String mStrUserZone;
    private String mStrUserStatus;
    private String mStrUserType;

    private TextView mUserId;
    private TextView mUserName;
    private TextView mUserEmail;
    private TextView mUserRoute;
    private TextView mUserZone;
    private TextView mUserStatus;
    private TextView mUserType;

    private UserPreferences mUserPreferences;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_supervisor);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, "OnCreate");
        mUserPreferences = new UserPreferences(getApplicationContext());
        mUser = mUserPreferences.getUser();
        Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getUser().getUserEmail());
        initUi();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.no_change, R.anim.push_out_right);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_change, R.anim.push_out_right);
    }

    private void initUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBundle = getIntent().getExtras();
        mIntUserId = mBundle.getInt(MainActivity.EXTRA_CASE_ID);
        mStrUserName = mBundle.getString(MainActivity.EXTRA_USER_NAME);
        mStrUserLastname = mBundle.getString(MainActivity.EXTRA_USER_LASTNAME);
        mStrUserEmail = mBundle.getString(MainActivity.EXTRA_USER_EMAIL);
        mStrUserRoute = mBundle.getString(MainActivity.EXTRA_USER_ROUTE);
        mStrUserZone = mBundle.getString(MainActivity.EXTRA_USER_ZONE);
        mStrUserStatus = mBundle.getString(MainActivity.EXTRA_USER_STATUS);
        mStrUserType = mBundle.getString(MainActivity.EXTRA_USER_TYPE);

        getSupportActionBar().setTitle("Detalles de " + mStrUserName + " " + mStrUserLastname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Log.d(DEBUG_TAG, "Id del usuario: " + String.valueOf(mIntUserId));
        Log.d(DEBUG_TAG, "Nombre del usuario: " + mStrUserName + " " + mStrUserLastname);
        Log.d(DEBUG_TAG, "Correo del usuario: " + mStrUserEmail);
        Log.d(DEBUG_TAG, "Ruta del usuario: " + mStrUserRoute);
        Log.d(DEBUG_TAG, "Zona del usuario: " + mStrUserZone);
        Log.d(DEBUG_TAG, "Estatus del usuario: " + mStrUserStatus);
        Log.d(DEBUG_TAG, "Tipo del usuario: " + mStrUserType);

        mUserId = (TextView) findViewById(R.id.activity_detail_supervisor_tv_user_id);
        mUserId.setText(String.valueOf(mIntUserId));
        mUserName = (TextView) findViewById(R.id.activity_detail_supervisor_tv_user_name);
        mUserName.setText(mStrUserName);
        mUserEmail = (TextView) findViewById(R.id.activity_detail_supervisor_tv_user_email);
        mUserEmail.setText(mStrUserEmail);
        mUserRoute = (TextView) findViewById(R.id.activity_detail_supervisor_tv_user_route);
        mUserRoute.setText(mStrUserRoute);
        mUserZone = (TextView) findViewById(R.id.activity_detail_supervisor_tv_user_zone);
        mUserZone.setText(mStrUserZone);
        mUserStatus = (TextView) findViewById(R.id.activity_detail_supervisor_tv_user_status);
        mUserStatus.setText(String.valueOf(mStrUserStatus));
        if (mStrUserStatus.equals(User.userStatus.ACTIVE.toString())) {
            mUserStatus.setTextColor(getResources().getColor(R.color.light_green));
        } else if (mStrUserStatus.equals(User.userStatus.NOTACTIVE.toString())) {
            mUserStatus.setTextColor(getResources().getColor(R.color.red));
        }

        mUserType = (TextView) findViewById(R.id.activity_detail_supervisor_tv_user_type);
        mUserType.setText(String.valueOf(mStrUserType));

    }
}
