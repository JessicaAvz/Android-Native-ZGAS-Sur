package com.zgas.tesselar.myzuite.Controller.Activity.UserLeakage;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Controller.Activity.MainActivity;
import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

import java.sql.Time;

public class DetailActivityLeakage extends AppCompatActivity implements View.OnClickListener {

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

    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_leakage);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getApplicationContext());
        mUser = mUserPreferences.getUser();
        Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getUser().getUserEmail());
        initUi();
    }

    private void initUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBundle = getIntent().getExtras();
        mIntCaseId = mBundle.getInt(MainActivity.EXTRA_CASE_ID);
        mIntCaseUserId = mBundle.getInt(MainActivity.EXTRA_CASE_USER_ID);
        mStrCaseUserName = mBundle.getString(MainActivity.EXTRA_CASE_USER_NAME);
        mStrCaseUserLastname = mBundle.getString(MainActivity.EXTRA_CASE_USER_LASTNAME);
        mStrCaseAddress = mBundle.getString(MainActivity.EXTRA_CASE_ADDRESS);
        mStrCaseStatus = mBundle.getString(MainActivity.EXTRA_CASE_STATUS);
        mStrCaseType = mBundle.getString(MainActivity.EXTRA_CASE_TYPE);
        mStrCasePriority = mBundle.getString(MainActivity.EXTRA_CASE_PRIORITY);
        mCldCaseTimeIn = (Time) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_IN);
        mCldCaseTimeSeen = (Time) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_SEEN);
        mCldCaseTimeArrived = (Time) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_ARRIVAL);
        mCldCaseTimeProgrammed = (Time) mBundle.getSerializable(MainActivity.EXTRA_CASE_TIME_PROGRAMMED);

        Log.d(DEBUG_TAG, "Id de la fuga: " + String.valueOf(mIntCaseId));
        Log.d(DEBUG_TAG, "Id del cliente: " + String.valueOf(mIntCaseUserId));
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
        getSupportActionBar().setTitle("Detalle de la fuga " + mIntCaseId);

        mUserId = (TextView) findViewById(R.id.activity_detai_leakage_tv_client_id);
        mUserId.setText(String.valueOf(mIntCaseUserId));
        mUserName = (TextView) findViewById(R.id.activity_detail_leakage_tv_client_name);
        mUserName.setText(mStrCaseUserName + " " + mStrCaseUserLastname);
        mCaseAddress = (TextView) findViewById(R.id.activity_detail_leakage_tv_case_address);
        mCaseAddress.setText(mStrCaseAddress);
        mCaseStatus = (TextView) findViewById(R.id.activity_detail_leakage_tv_status);
        mCaseStatus.setText(mStrCaseStatus);
        mCaseTimeIn = (TextView) findViewById(R.id.activity_detail_leakage_tv_time_in);
        mCaseTimeIn.setText(String.valueOf(mCldCaseTimeIn));
        mCaseTimeSeen = (TextView) findViewById(R.id.activity_detail_leakage_tv_time_seen);
        mCaseTimeSeen.setText(String.valueOf(mCldCaseTimeSeen));
        mCaseTimeArrived = (TextView) findViewById(R.id.activity_detail_leakage_tv_arrived);
        mCaseTimeArrived.setText(String.valueOf(mCldCaseTimeArrived));
        mCaseTimeProgrammed = (TextView) findViewById(R.id.activity_detail_leakage_tv_time_programmed);
        mCaseTimeProgrammed.setText(String.valueOf(mCldCaseTimeProgrammed));
        mFabInProgress = (FloatingActionButton) findViewById(R.id.activity_detail_leakage_fab_in_progress);
        mFabInProgress.setOnClickListener(this);
        mFabFinished = (FloatingActionButton) findViewById(R.id.activity_detail_leakage_fab_finished);
        mFabFinished.setOnClickListener(this);
        mFabCanceled = (FloatingActionButton) findViewById(R.id.activity_detail_leakage_fab_cancel);
        mFabCanceled.setOnClickListener(this);
        mFabWaze = (FloatingActionButton) findViewById(R.id.activity_detail_leakage_fab_waze);
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
            case R.id.activity_detail_leakage_fab_finished:
                finishDialog();
                break;
            case R.id.activity_detail_leakage_fab_in_progress:
                inProgressDialog();
                break;
            case R.id.activity_detail_leakage_fab_cancel:
                cancelDialog();
                break;
            case R.id.activity_detail_leakage_fab_waze:
                wazeIntent(mStrCaseAddress);
                break;
        }
    }

    private void wazeIntent(String address) {
        final String url = "waze://?q=" + address;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
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

    private void finishDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_finish_case_leakage);
        Log.d(DEBUG_TAG, "Finish dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final Spinner mSpinnerOptions = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_option);
        final Spinner mSpinnerSecondOption = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_second_option);
        mSpinnerSecondOption.setVisibility(View.GONE);
        final Spinner mSpinnerThirdOption = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_third_option);
        mSpinnerThirdOption.setVisibility(View.GONE);

        adapter = ArrayAdapter.createFromResource(this, R.array.leakage_prompts,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));
        mSpinnerOptions.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1 || i == 2 || i == 3 || i == 4) {
                    mSpinnerSecondOption.setVisibility(View.VISIBLE);
                    adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.leakage_state, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinnerSecondOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                            R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));

                    mSpinnerSecondOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (adapterView.getSelectedItemPosition() == 1 && i == 1) {
                                //Fuga cilindro con gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                        R.array.leakage_prompts_cilynder_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));
                            } else if (adapterView.getSelectedItemPosition() == 1 && i == 2) {
                                //Fuga cilindro sin gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                        R.array.leakage_prompts_cilynder_no_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));
                            } else if (adapterView.getSelectedItemPosition() == 2 && i == 1) {
                                //Fuga estacionario con gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                        R.array.leakage_prompts_stationary_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));
                            } else if (adapterView.getSelectedItemPosition() == 2 && i == 2) {
                                //Fuga estacionario sin gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                        R.array.leakage_prompts_stationary_no_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));
                            } else if (adapterView.getSelectedItemPosition() == 3 && i == 1) {
                                //Fuga servicio medido con gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                        R.array.leakage_prompts_service_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));
                            } else if (adapterView.getSelectedItemPosition() == 3 && i == 2) {
                                //Fuga servicio medido sin gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                        R.array.leakage_prompts_service_no_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));
                            } else if (adapterView.getSelectedItemPosition() == 4 && i == 1) {
                                //Fuga cliente con gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                        R.array.leakage_prompts_client_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));
                            } else if (adapterView.getSelectedItemPosition() == 4 && i == 2) {
                                //Fuga cliente sin gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                                        R.array.leakage_prompts_client_no_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, getApplicationContext()));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }));


        Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_leakage_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSpinnerOptions.getSelectedItem() == null || mSpinnerSecondOption.getSelectedItem() == null || mSpinnerThirdOption.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leak_finish_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    Log.d(DEBUG_TAG, "Spinner 1: " + mSpinnerOptions.getSelectedItem().toString());
                    Log.d(DEBUG_TAG, "Spinner 2: " + mSpinnerSecondOption.getSelectedItem().toString());
                    Log.d(DEBUG_TAG, "Spinner 3: " + mSpinnerThirdOption.getSelectedItem().toString());
                    mSpinnerOptions.setSelection(0);
                    mSpinnerSecondOption.setSelection(0);
                    mSpinnerThirdOption.setSelection(0);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leak_finish_correct), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });

        Button mBtnCancel = dialog.findViewById(R.id.dialog_finish_case_leakage_btn_cancel);
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
        dialog.setContentView(R.layout.dialog_cancel_case_leakage);
        Log.d(DEBUG_TAG, "Cancel dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final Spinner mSpinnerOptions = dialog.findViewById(R.id.dialog_cancel_case_leakage_sp_option);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cancelation_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));

        Button mBtnAccept = dialog.findViewById(R.id.dialog_cancel_case_leakage_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSpinnerOptions.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leak_cancel_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    Log.d(DEBUG_TAG, "Spinner 1 " + mSpinnerOptions.getSelectedItem().toString());
                    mSpinnerOptions.setSelection(0);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leak_cancel_correct), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });

        Button mBtnCancel = dialog.findViewById(R.id.dialog_cancel_case_leakage_btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
