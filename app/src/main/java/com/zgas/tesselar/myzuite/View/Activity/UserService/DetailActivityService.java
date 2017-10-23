package com.zgas.tesselar.myzuite.View.Activity.UserService;

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
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.View.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Controller.UserPreferences;

import java.sql.Time;

public class DetailActivityService extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "DetailActivityLeakage";

    private Bundle mBundle;
    private int mIntCaseId;
    private int mIntCaseUserId;
    private String mStrCaseUserName;
    private String mStrCaseUserLastname;
    private String mStrCaseAddress;
    private String mStrCaseStatus;
    private String mStrCaseType;
    private String mStrCasePriority;
    private Time mCldCaseTimeIn;
    private Time mCldCaseTimeSeen;
    private Time mCldCaseTimeArrived;
    private Time mCldCaseTimeProgrammed;

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

    private UserPreferences mUserPreferences;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(this);
        mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());
        initUi();
    }

    private void initUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBundle = getIntent().getExtras();
        mIntCaseId = mBundle.getInt(ExtrasHelper.ORDER_JSON_OBJECT_ID);
        //mIntCaseUserId = mBundle.getInt(ExtrasHelper.EXTRA_JSON_OBJECT_USER_ID);
        mStrCaseUserName = mBundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_USER_NAME);
        mStrCaseAddress = mBundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS);
        mStrCaseStatus = mBundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS);
        mStrCaseType = mBundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TYPE);
        mStrCasePriority = mBundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY);
        mCldCaseTimeIn = (Time) mBundle.getSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT);
        mCldCaseTimeSeen = (Time) mBundle.getSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN);
        mCldCaseTimeArrived = (Time) mBundle.getSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL);
        mCldCaseTimeProgrammed = (Time) mBundle.getSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED);

        Log.d(DEBUG_TAG, "Id de la fuga: " + String.valueOf(mIntCaseId));
        //Log.d(DEBUG_TAG, "Id del cliente: " + String.valueOf(mIntCaseUserId));
        Log.d(DEBUG_TAG, "Nombre del cliente: " + mStrCaseUserName);
        Log.d(DEBUG_TAG, "Apellido del cliente: " + mStrCaseUserLastname);
        Log.d(DEBUG_TAG, "Dirección de la entrega: " + mStrCaseAddress);
        Log.d(DEBUG_TAG, "Estatus de la fuga: " + mStrCaseStatus.toString());
        Log.d(DEBUG_TAG, "Tipo de pedido: " + mStrCaseType.toString());
        Log.d(DEBUG_TAG, "Prioridad de la fuga: " + mStrCasePriority.toString());
        Log.d(DEBUG_TAG, "Hora de reporte: " + String.valueOf(mCldCaseTimeIn));
        Log.d(DEBUG_TAG, "Hora de visualización del reporte: " + String.valueOf(mCldCaseTimeSeen));
        Log.d(DEBUG_TAG, "Hora de llegada del reporte: " + String.valueOf(mCldCaseTimeArrived));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detalle del servicio " + mIntCaseId);

        mUserName = (TextView) findViewById(R.id.activity_detail_service_tv_client_name);
        mUserName.setText(mStrCaseUserName + " " + mStrCaseUserLastname);
        mCaseAddress = (TextView) findViewById(R.id.activity_detail_service_tv_case_address);
        mCaseAddress.setText(mStrCaseAddress);
        mCaseStatus = (TextView) findViewById(R.id.activity_detail_service_tv_status);
        mCaseStatus.setText(mStrCaseStatus);
        mCaseTimeIn = (TextView) findViewById(R.id.activity_detail_service_tv_time_in);
        mCaseTimeIn.setText(String.valueOf(mCldCaseTimeIn));
        mCaseTimeSeen = (TextView) findViewById(R.id.activity_detail_service_tv_time_seen);
        mCaseTimeSeen.setText(String.valueOf(mCldCaseTimeSeen));
        mCaseTimeArrived = (TextView) findViewById(R.id.activity_detail_service_tv_arrived);
        mCaseTimeArrived.setText(String.valueOf(mCldCaseTimeArrived));
        mCaseTimeProgrammed = (TextView) findViewById(R.id.activity_detail_service_tv_time_programmed);
        mCaseTimeProgrammed.setText(String.valueOf(mCldCaseTimeProgrammed));
        mFabInProgress = (FloatingActionButton) findViewById(R.id.activity_detail_service_fab_in_progress);
        mFabInProgress.setOnClickListener(this);
        mFabFinished = (FloatingActionButton) findViewById(R.id.activity_detail_service_fab_finished);
        mFabFinished.setOnClickListener(this);
        mFabCanceled = (FloatingActionButton) findViewById(R.id.activity_detail_service_fab_cancel);
        mFabCanceled.setOnClickListener(this);
        mFabWaze = (FloatingActionButton) findViewById(R.id.activity_detail_service_fab_waze);
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
            case R.id.activity_detail_service_fab_in_progress:
                inProgressDialog();
                break;
            case R.id.activity_detail_service_fab_finished:
                finishDialog();
                break;
            case R.id.activity_detail_service_fab_cancel:
                cancelDialog();
                break;
            case R.id.activity_detail_service_fab_waze:
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
        Log.d(DEBUG_TAG, "Finish dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final EditText etQuantity = dialog.findViewById(R.id.dialog_finish_case_tv_quantity);
        final EditText etTicket = dialog.findViewById(R.id.dialog_finish_case_tv_ticket_number);
        final EditText etTotal = dialog.findViewById(R.id.dialog_finish_case_tv_total);

        Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(etQuantity) || isEmpty(etTicket) || isEmpty(etTotal)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_finish_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    final String quantity = etQuantity.getText().toString();
                    final String ticket = etTicket.getText().toString();
                    final String total = etTotal.getText().toString();
                    Log.d(DEBUG_TAG, "Cantidad surtida " + quantity);
                    Log.d(DEBUG_TAG, "Folio del ticket " + ticket);
                    Log.d(DEBUG_TAG, "Total " + total);
                    etQuantity.getText().clear();
                    etTicket.getText().clear();
                    etTotal.getText().clear();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_finish_correct), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
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
        Log.d(DEBUG_TAG, "Cancel dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final Spinner mSpinnerOptions = dialog.findViewById(R.id.dialog_cancel_case_sp_option);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cancelation_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));

        Button mBtnAccept = dialog.findViewById(R.id.dialog_cancel_case_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSpinnerOptions.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_cancel_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    Log.d(DEBUG_TAG, mSpinnerOptions.getSelectedItem().toString());
                    mSpinnerOptions.setSelection(0);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_cancel_correct), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
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
        Log.d(DEBUG_TAG, "In progress dialog " + getResources().getString(R.string.on_create));
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

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
