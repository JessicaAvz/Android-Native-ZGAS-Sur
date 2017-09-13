package com.zgas.tesselar.myzuite.Controller.Activity.UserOperator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Controller.Activity.MainActivity;
import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

import java.sql.Time;

public class DetailActivityOperator extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "DetailActivityOperator";
    private Bundle mBundle;
    private int mIntCaseId;
    private int mIntCaseUserId;
    private String mStrCaseUserName;
    private String mStrCaseUserLastname;
    private String mStrCaseAddress;
    private String mStrCaseStatus;
    private String mStrCaseType;
    private String mStrCasePriority;
    private Time mTmCaseTimeIn;
    private Time mTmCaseTimeSeen;
    private Time mTmCaseTimeArrived;
    private Time mTmCaseTimeProgrammed;

    private TextView mUserId;
    private TextView mUserName;
    private TextView mCaseAddress;
    private TextView mCaseStatus;
    private TextView mCaseTimeIn;
    private TextView mCaseTimeSeen;
    private TextView mCaseTimeArrived;
    private TextView mCaseTimeProgrammed;
    private FloatingActionButton mFabInProgress;
    private FloatingActionButton mFabFinished;
    private FloatingActionButton mFabCanceled;
    private FloatingActionButton mFabWaze;

    private Case mCase;
    private UserPreferences mUserPreferences;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_operator);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, "OnCreate");
        mUserPreferences = new UserPreferences(getApplicationContext());
        mUser = mUserPreferences.getUser();
        Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getUser().getUserEmail());
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
        mTmCaseTimeIn = (Time) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_IN);
        mTmCaseTimeSeen = (Time) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_SEEN);
        mTmCaseTimeArrived = (Time) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_ARRIVAL);
        mTmCaseTimeProgrammed = (Time) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_PROGRAMMED);

        Log.d(DEBUG_TAG, "Id del pedido: " + String.valueOf(mIntCaseId));
        Log.d(DEBUG_TAG, "Id del cliente: " + String.valueOf(mIntCaseUserId));
        Log.d(DEBUG_TAG, "Nombre del cliente: " + mStrCaseUserName);
        Log.d(DEBUG_TAG, "Apellido del cliente: " + mStrCaseUserLastname);
        Log.d(DEBUG_TAG, "Dirección de la entrega: " + mStrCaseAddress);
        Log.d(DEBUG_TAG, "Estatus del pedido: " + mStrCaseStatus.toString());
        Log.d(DEBUG_TAG, "Tipo de pedido: " + mStrCaseType.toString());
        Log.d(DEBUG_TAG, "Prioridad del pedido: " + mStrCasePriority.toString());
        Log.d(DEBUG_TAG, "Hora de pedido: " + String.valueOf(mTmCaseTimeIn));
        Log.d(DEBUG_TAG, "Hora de visualización del pedido: " + String.valueOf(mTmCaseTimeSeen));
        Log.d(DEBUG_TAG, "Hora de llegada del pedido: " + String.valueOf(mTmCaseTimeArrived));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detalle del pedido " + mIntCaseId);

        mUserId = (TextView) findViewById(R.id.activity_detai_operator_tv_client_id);
        mUserId.setText(String.valueOf(mIntCaseUserId));
        mUserName = (TextView) findViewById(R.id.activity_detail_operator_tv_client_name);
        mUserName.setText(mStrCaseUserName + " " + mStrCaseUserLastname);
        mCaseAddress = (TextView) findViewById(R.id.activity_detail_operator_tv_case_address);
        mCaseAddress.setText(mStrCaseAddress);
        mCaseStatus = (TextView) findViewById(R.id.activity_detail_operator_tv_status);
        mCaseStatus.setText(mStrCaseStatus);
        mCaseTimeIn = (TextView) findViewById(R.id.activity_detail_operator_tv_time_in);
        if (mTmCaseTimeIn == null) {
            mCaseTimeIn.setText("Sin datos.");
        } else {
            mCaseTimeIn.setText(String.valueOf(mTmCaseTimeIn));
        }
        mCaseTimeSeen = (TextView) findViewById(R.id.activity_detail_operator_tv_time_seen);
        if (mTmCaseTimeSeen == null) {
            mCaseTimeSeen.setText("Sin datos.");
        } else {
            mCaseTimeSeen.setText(String.valueOf(mTmCaseTimeSeen));
        }
        mCaseTimeArrived = (TextView) findViewById(R.id.activity_detail_operator_tv_arrived);
        if (mTmCaseTimeArrived == null) {
            mCaseTimeArrived.setText("Sin datos.");
        } else {
            mCaseTimeArrived.setText(String.valueOf(mTmCaseTimeArrived));
        }
        mCaseTimeProgrammed = (TextView) findViewById(R.id.activity_detail_operator_tv_time_programmed);
        if (mTmCaseTimeProgrammed == null) {
            mCaseTimeProgrammed.setText("Sin datos.");
        } else {
            mCaseTimeProgrammed.setText(String.valueOf(mTmCaseTimeProgrammed));
        }
        mFabInProgress = (FloatingActionButton) findViewById(R.id.activity_detail_operator_fab_in_progress);
        mFabInProgress.setOnClickListener(this);
        mFabFinished = (FloatingActionButton) findViewById(R.id.activity_detail_operator_fab_finished);
        mFabFinished.setOnClickListener(this);
        mFabCanceled = (FloatingActionButton) findViewById(R.id.activity_detail_operator_fab_cancel);
        mFabCanceled.setOnClickListener(this);
        mFabWaze = (FloatingActionButton) findViewById(R.id.activity_detail_operator_fab_waze);
        mFabWaze.setOnClickListener(this);

        if (mStrCaseStatus.equals(Case.caseStatus.INPROGRESS.toString())) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.amber));
        } else if (mStrCaseStatus.equals(Case.caseStatus.FINISHED.toString())) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.light_green));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        } else if (mStrCaseStatus.equals(Case.caseStatus.CANCELLED.toString())) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.red));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_detail_operator_fab_in_progress:
                inProgressDialog();
                break;
            case R.id.activity_detail_operator_fab_finished:
                finishDialog();
                break;
            case R.id.activity_detail_operator_fab_cancel:
                cancelDialog();
                break;
            case R.id.activity_detail_operator_fab_waze:
                wazeIntent(mStrCaseAddress);
                break;
        }
    }

    private void wazeIntent(String address) {
        final String url = "waze://?q=" + address;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void finishDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_finish_case_operator);
        Log.d(DEBUG_TAG, "OnCreate");
        dialog.setCancelable(false);

        Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String quantity = ((EditText) dialog.findViewById(R.id.dialog_finish_case_tv_quantity)).getText().toString();
                final String ticket = ((EditText) dialog.findViewById(R.id.dialog_finish_case_tv_ticket_number)).getText().toString();
                final String total = ((EditText) dialog.findViewById(R.id.dialog_finish_case_tv_total)).getText().toString();

                Log.d(DEBUG_TAG, "Cantidad surtida " + quantity);
                Log.d(DEBUG_TAG, "Folio del ticket " + ticket);
                Log.d(DEBUG_TAG, "Total " + total);

                dialog.dismiss();
            }
        });

        Button mBtnCancel = dialog.findViewById(R.id.dialog_finish_case_btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void cancelDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_cancel_case_operator);
        Log.d(DEBUG_TAG, "OnCreate");
        dialog.setCancelable(false);

        Spinner mSpinnerOption = dialog.findViewById(R.id.dialog_cancel_case_sp_option);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cancelation_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));

        Button mBtnAccept = dialog.findViewById(R.id.dialog_cancel_case_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button mBtnCancel = dialog.findViewById(R.id.dialog_cancel_case_btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void inProgressDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_in_progress_title))
                .setMessage(getResources().getString(R.string.dialog_in_progress_body))
                .setIcon(R.drawable.icon_dialog_progress)
                .setPositiveButton(getResources().getString(R.string.dialog_in_progress_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }

                })
                .setCancelable(false)
                .show();
    }
}
