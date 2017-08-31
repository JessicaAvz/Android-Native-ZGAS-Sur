package com.zgas.tesselar.myzuite.Controller.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "DetailActivity";
    private Bundle mBundle;
    private int mIntCaseId;
    private int mIntCaseUserId;
    private String mStrCaseUserName;
    private String mStrCaseUserLastname;
    private String mStrCaseAddress;
    private String mStrCaseStatus;
    private String mStrCaseType;
    private String mStrCasePriority;
    private Calendar mCldCaseTimeIn;
    private Calendar mCldCaseTimeSeen;
    private Calendar mCldCaseTimeArrived;
    private Calendar mCldCaseTimeProgrammed;

    private TextView mUserId;
    private TextView mUserName;
    private TextView mCaseAddress;
    private TextView mCaseStatus;
    private TextView mCaseType;
    private TextView mCaseTimeIn;
    private TextView mCaseTimeSeen;
    private TextView mCaseTimeArrived;
    private TextView mCaseTimeProgrammed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(DEBUG_TAG, "OnCreate");
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);

        initUi();
    }

    private void initUi() {
        mBundle = getIntent().getExtras();
        mIntCaseId = mBundle.getInt(MainActivity.EXTRA_CASE_ID);
        mIntCaseUserId = mBundle.getInt(MainActivity.EXTRA_CASE_USER_ID);
        mStrCaseUserName = mBundle.getString(MainActivity.EXTRA_CASE_USER_NAME);
        mStrCaseUserLastname = mBundle.getString(MainActivity.EXTRA_CASE_USER_LASTNAME);
        mStrCaseAddress = mBundle.getString(MainActivity.EXTRA_CASE_ADDRESS);
        mStrCaseStatus = mBundle.getString(MainActivity.EXTRA_CASE_STATUS);
        mStrCaseType = mBundle.getString(MainActivity.EXTRA_CASE_TYPE);
        mStrCasePriority = mBundle.getString(MainActivity.EXTRA_CASE_PRIORITY);
        mCldCaseTimeIn = (Calendar) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_IN);
        mCldCaseTimeSeen = (Calendar) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_SEEN);
        mCldCaseTimeArrived = (Calendar) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_ARRIVAL);
        mCldCaseTimeProgrammed = (Calendar) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_PROGRAMMED);

        Log.d(DEBUG_TAG, "Id del pedido: " + String.valueOf(mIntCaseId));
        Log.d(DEBUG_TAG, "Id del cliente: " + String.valueOf(mIntCaseUserId));
        Log.d(DEBUG_TAG, "Nombre del cliente: " + mStrCaseUserName);
        Log.d(DEBUG_TAG, "Apellido del cliente: " + mStrCaseUserLastname);
        Log.d(DEBUG_TAG, "Dirección de la entrega: " + mStrCaseAddress);
        Log.d(DEBUG_TAG, "Estatus del pedido: " + mStrCaseStatus.toString());
        Log.d(DEBUG_TAG, "Tipo de pedido: " + mStrCaseType.toString());
        Log.d(DEBUG_TAG, "Prioridad del pedido: " + mStrCasePriority.toString());
        Log.d(DEBUG_TAG, "Hora de pedido: " + String.valueOf(mCldCaseTimeIn));
        Log.d(DEBUG_TAG, "Hora de visualización del pedido: " + String.valueOf(mCldCaseTimeSeen));
        Log.d(DEBUG_TAG, "Hora de llegada del pedido: " + String.valueOf(mCldCaseTimeArrived));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detalle del pedido " + mIntCaseId);

        mUserId = (TextView) findViewById(R.id.activity_detail_tv_client_id);
        mUserId.setText(String.valueOf(mIntCaseUserId));
        mUserName = (TextView) findViewById(R.id.activity_detail_tv_client_name);
        mUserName.setText(mStrCaseUserName + " " + mStrCaseUserLastname);
        mCaseAddress = (TextView) findViewById(R.id.activity_detail_tv_case_address);
        mCaseAddress.setText(mStrCaseAddress);
        mCaseStatus = (TextView) findViewById(R.id.activity_detail_tv_status);
        mCaseStatus.setText(mStrCaseStatus);
        mCaseTimeIn = (TextView) findViewById(R.id.activity_detail_tv_time_in);
        mCaseTimeIn.setText(String.valueOf(mCldCaseTimeIn));
        mCaseTimeSeen = (TextView) findViewById(R.id.activity_detail_tv_time_seen);
        mCaseTimeSeen.setText(String.valueOf(mCldCaseTimeSeen));
        mCaseTimeArrived = (TextView) findViewById(R.id.activity_detail_tv_arrived);
        mCaseTimeArrived.setText(String.valueOf(mCldCaseTimeArrived));
        mCaseTimeProgrammed = (TextView) findViewById(R.id.activity_detail_tv_time_programmed);
        mCaseTimeProgrammed.setText(String.valueOf(mCldCaseTimeProgrammed));

        if (mStrCaseStatus.equals(Case.caseStatus.INPROGRESS.toString())) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.amber));
        } else if (mStrCaseStatus.equals(Case.caseStatus.FINISHED.toString())) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.light_green));
        } else if (mStrCaseStatus.equals(Case.caseStatus.CANCELLED.toString())) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.red));
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
