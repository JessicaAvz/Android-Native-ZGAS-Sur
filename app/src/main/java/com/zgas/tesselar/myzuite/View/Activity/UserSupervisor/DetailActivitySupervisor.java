package com.zgas.tesselar.myzuite.View.Activity.UserSupervisor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.GetUserInfoTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

/**
 * Class that shows the details of all the users that a supervisor has under its care. This will show
 * if the user is of type 'supervisor'
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see UserPreferences
 * @see Bundle
 * @see android.os.AsyncTask
 * @see GetUserInfoTask
 */
public class DetailActivitySupervisor extends AppCompatActivity {

    private static final String DEBUG_TAG = "DetailActSuperv";

    private Bundle mBundle;
    private String mStrUserId;
    private String mStrUserName;
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
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(this);
        mUser = mUserPreferences.getUserObject();
        initUi();
    }

    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBundle = getIntent().getExtras();
        mStrUserId = mBundle.getString(ExtrasHelper.EXTRA_USER_ID);
        mStrUserName = mBundle.getString(ExtrasHelper.EXTRA_USER_NAME);
        mStrUserEmail = mBundle.getString(ExtrasHelper.EXTRA_USER_EMAIL);
        mStrUserRoute = mBundle.getString(ExtrasHelper.EXTRA_USER_ROUTE);
        mStrUserZone = mBundle.getString(ExtrasHelper.EXTRA_USER_ZONE);
        mStrUserStatus = mBundle.getString(ExtrasHelper.EXTRA_USER_STATUS);
        mStrUserType = mBundle.getString(ExtrasHelper.EXTRA_USER_TYPE);

        getSupportActionBar().setTitle("Detalles de " + mStrUserName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mUserId = findViewById(R.id.activity_detail_supervisor_tv_user_id);
        if (mStrUserId != null) {
            mUserId.setText(String.valueOf(mStrUserId));
        } else {
            mUserId.setText(getResources().getString(R.string.no_data));
        }
        mUserName = findViewById(R.id.activity_detail_supervisor_tv_user_name);
        if (mStrUserName != null) {
            mUserName.setText(mStrUserName);
        } else {
            mUserName.setText(getResources().getString(R.string.no_data));
        }
        mUserEmail = findViewById(R.id.activity_detail_supervisor_tv_user_email);
        if (mStrUserEmail != null) {
            mUserEmail.setText(mStrUserEmail);
        } else {
            mUserEmail.setText(getResources().getString(R.string.no_data));
        }
        mUserRoute = findViewById(R.id.activity_detail_supervisor_tv_user_route);
        if (mStrUserRoute != null) {
            mUserRoute.setText(mStrUserRoute);
        } else {
            mUserRoute.setText(getResources().getString(R.string.no_data));
        }
        mUserZone = findViewById(R.id.activity_detail_supervisor_tv_user_zone);
        if (mStrUserZone != null) {
            mUserZone.setText(mStrUserZone);
        } else {
            mUserZone.setText(getResources().getString(R.string.no_data));
        }
        mUserStatus = findViewById(R.id.activity_detail_supervisor_tv_user_status);
        if (mStrUserStatus != null) {
            if (mStrUserStatus.equals(User.userStatus.ACTIVE.toString())) {
                mUserStatus.setTextColor(getResources().getColor(R.color.light_green));
                mUserStatus.setText(String.valueOf(mStrUserStatus));
            } else if (mStrUserStatus.equals(User.userStatus.NOTACTIVE.toString())) {
                mUserStatus.setTextColor(getResources().getColor(R.color.red));
                mUserStatus.setText(String.valueOf(mStrUserStatus));
            }
        } else {
            mUserStatus.setText(getResources().getString(R.string.no_data));
        }

        mUserType = findViewById(R.id.activity_detail_supervisor_tv_user_type);
        if (mStrUserType != null) {
            mUserType.setText(String.valueOf(mStrUserType));
        } else {
            mUserType.setText(getResources().getString(R.string.no_data));
        }
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
}
