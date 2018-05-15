package com.zgas.tesselar.myzuite.View.Activity.UserLeakage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutStatusLeakTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Class that shows the details of the leakages; it's used when the operator is of type 'Leak
 * technician'. In this class we can also modify the leak status - in progress, cancelled, finished -
 * and open the Waze app with the leak address.
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see UserPreferences
 * @see Bundle
 * @see android.os.AsyncTask
 * @see PutStatusLeakTask
 */
public class DetailActivityLeakage extends AppCompatActivity implements
        PutStatusLeakTask.StatusLeakTaskListener {

    private static final String DEBUG_TAG = "DetailActivityLeakage";
    private Bundle mBundle;
    private String mStrLeakId;
    private String mStrLeakClientName;
    private String mStrLeakStatus;
    private String mStrLeakType;
    private String mStrLeakPriority;
    private String mStrLeakOperator;
    private String mStrLeakSubject;
    private String mStrLeakFolioSalesNote;
    private String mStrLeakTimeSeen;
    private String mStrLeakTimeDeparture;
    private String mStrLeakTimeArrived;
    private String mStrLeakTimeScheduled;
    private String mStrLeakTimeTechnician;
    private String mStrCylinderCapacity;
    private String mStrCylinderColor;
    private String mStrChannel;
    private String mStrLeakAddress;
    private String strCancellationReason;

    private String resolution;
    private String channel;

    @BindView(R.id.activity_detail_leakage_tv_client_name)
    TextView mUserName;
    @BindView(R.id.activity_detail_leakage_tv_case_address)
    TextView mLeakAddress;
    @BindView(R.id.activity_detail_leakage_tv_status)
    TextView mLeakStatus;
    @BindView(R.id.activity_detail_leakage_tv_time_in)
    TextView mLeakTimeIn;
    @BindView(R.id.activity_detail_leakage_tv_time_seen)
    TextView mLeakTimeSeen;
    @BindView(R.id.activity_detail_leakage_tv_time_arrived)
    TextView mLeakTimeArrived;
    @BindView(R.id.activity_detail_leakage_tv_time_programmed)
    TextView mLeakTimeScheduled;
    @BindView(R.id.activity_detail_leakage_tv_capacity)
    TextView mLeakCylinderCapacity;
    @BindView(R.id.activity_detail_leakage_tv_color)
    TextView mLeakCylinderColor;
    @BindView(R.id.activity_detail_leakage_tv_channel)
    TextView mLeakChannel;
    @Nullable
    @BindView(R.id.activity_detail_leakage_fab_in_progress)
    FloatingActionButton mFabInProgress;
    @Nullable
    @BindView(R.id.activity_detail_leakage_fab_finished)
    FloatingActionButton mFabFinished;
    @Nullable
    @BindView(R.id.activity_detail_leakage_fab_cancel)
    FloatingActionButton mFabCanceled;
    @Nullable
    @BindView(R.id.activity_detail_leakage_fab_waze)
    FloatingActionButton mFabWaze;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.blue)
    int blue;
    @BindColor(R.color.amber)
    int amber;
    @BindColor(R.color.red)
    int red;
    @BindColor(R.color.light_green)
    int light_green;
    @BindColor(R.color.grey_300)
    int grey_300;

    private UserPreferences mUserPreferences;
    private User mUser;
    private boolean isClicked = false;
    private Context context;
    private JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_leakage);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(this);
        mUser = mUserPreferences.getUserObject();
        context = this;
        initUi();
        checkButtons();
    }

    private void checkButtons() {
        if (isClicked == true || mStrLeakStatus.equals(this.getResources().getString(R.string.order_status_in_progress))) {
            mFabFinished.show();
            mFabCanceled.show();
            mFabWaze.show();
            mFabInProgress.hide();
        }
    }

    private void initUi() {
        setSupportActionBar(toolbar);

        mBundle = getIntent().getExtras();
        mStrLeakId = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID);
        mStrLeakClientName = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_WHO_REPORTS);
        mStrLeakSubject = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SUBJECT);
        mStrLeakAddress = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS);
        mStrLeakStatus = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS);
        mStrLeakType = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SERVICE_TYPE);
        mStrLeakPriority = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_PRIORITY);
        mStrLeakTimeSeen = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SEEN);
        mStrLeakTimeArrived = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_END);
        mStrLeakTimeScheduled = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_SCHEDULED);
        mStrLeakTimeDeparture = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_DEPARTURE);
        mStrLeakOperator = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_OPERATOR);
        mStrLeakFolioSalesNote = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SALES_NOTE);
        mStrLeakTimeTechnician = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_TECHNICIAN);
        mStrCylinderCapacity = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY);
        mStrCylinderColor = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR);
        mStrChannel = mBundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL);
        mLeakChannel = findViewById(R.id.activity_detail_leakage_tv_channel);
        mLeakChannel.setText(mStrChannel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detalle de la fuga " + mStrLeakId);

        mUserName.setText(mStrLeakClientName);
        mLeakAddress.setText(mStrLeakAddress);
        mLeakStatus.setText(mStrLeakStatus);
        mLeakCylinderColor.setText(mStrCylinderColor);
        mLeakCylinderCapacity.setText(mStrCylinderCapacity);

        if (mStrLeakTimeTechnician == null || mStrLeakTimeTechnician.equals("")) {
            mLeakTimeIn.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeIn.setText(mStrLeakTimeTechnician);
        }

        if (mStrLeakTimeSeen == null || mStrLeakTimeSeen.equals("")) {
            mLeakTimeSeen.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeSeen.setText(mStrLeakTimeSeen);
        }

        if (mStrLeakTimeArrived == null || mStrLeakTimeArrived.equals("")) {
            mLeakTimeArrived.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeArrived.setText(mStrLeakTimeArrived);
        }

        if (mStrLeakTimeScheduled == null || mStrLeakTimeScheduled.equals("")) {
            mLeakTimeScheduled.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeScheduled.setText(mStrLeakTimeScheduled);
        }
        mLeakStatus.setTextColor(blue);
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

    @OnClick(R.id.activity_detail_leakage_fab_finished)
    public void onClickFinished() {
        finishDialog();
    }

    @OnClick(R.id.activity_detail_leakage_fab_in_progress)
    public void onClickProgress() {
        inProgressDialog();
    }

    @OnClick(R.id.activity_detail_leakage_fab_cancel)
    public void onClickCanceled() {
        cancelDialog();
    }

    @OnClick(R.id.activity_detail_leakage_fab_waze)
    public void onClickWaze() {
        wazeIntent(mStrLeakAddress);
    }

    private void callAsyncTaskInProgress() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_ID, mStrLeakId);
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, String.valueOf(R.string.order_status_in_progress));

            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS)
                    + " ID: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID));

            PutStatusLeakTask putStatusLeakTask = new PutStatusLeakTask(this, params);
            putStatusLeakTask.setStatusLeakTaskListener(this);
            putStatusLeakTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAsyncTaskFinished() {
        JSONObject params = new JSONObject();
        try {
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_ID, mStrLeakId);
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, String.valueOf(R.string.order_status_finished));
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_RESOLUTION_STATUS, resolution);
            params.put(ExtrasHelper.LEAJ_JSON_OBJECT_CHANNEL_STATUS, channel);
            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS) +
                    "Id: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID) +
                    "Cylinder 10: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_RESOLUTION_STATUS) +
                    "Cylinder 20: " + params.getString(ExtrasHelper.LEAJ_JSON_OBJECT_CHANNEL_STATUS));

            PutStatusLeakTask putStatusLeakTask = new PutStatusLeakTask(this, params);
            putStatusLeakTask.setStatusLeakTaskListener(this);
            putStatusLeakTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAsyncTaskCancelled() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_ID, mStrLeakId);
            params.put(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, String.valueOf(R.string.order_status_canceled));
            //params.put(ExtrasHelper.ORDER_JSON_OBJECT_CANCELATION_REASON, strCancellationReason);
            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS)
                    + " ID: " + params.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID));

            PutStatusLeakTask putStatusLeakTask = new PutStatusLeakTask(this, params);
            putStatusLeakTask.setStatusLeakTaskListener(this);
            putStatusLeakTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wazeIntent(String address) {
        final String url = "waze://?q=" + address;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void inProgressDialog() {
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));

        new FancyAlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_in_progress_title))
                .setBackgroundColor(amber)
                .setMessage(getResources().getString(R.string.dialog_in_progress_body))
                .setNegativeBtnText(getResources().getString(R.string.cancel))
                .setPositiveBtnBackground(amber)
                .setPositiveBtnText(getResources().getString(R.string.dialog_in_progress_accept))
                .setNegativeBtnBackground(grey_300)
                .setAnimation(Animation.SIDE)
                .isCancellable(false)
                .setIcon(R.drawable.icon_progress, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        callAsyncTaskInProgress();
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        mFabCanceled.hide();
                        mFabFinished.hide();
                        mFabInProgress.show();
                    }
                })
                .build();
    }

    private void finishDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_finish_case_leakage);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
        Log.d(DEBUG_TAG, "Finish dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final Spinner mSpinnerLeakChannel = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_option);
        final Spinner mSpinnerLeakResolution = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_third_option);
        mSpinnerLeakResolution.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.leakage_prompts,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerLeakChannel.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected,
                this));
        mSpinnerLeakChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<CharSequence> adapter;
                switch (i) {
                    case 1: //Cilindro con gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        adapter = ArrayAdapter.createFromResource(context, R.array.leakage_prompts_cilynder_gas,
                                android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(adapter);
                        break;
                    case 2: //Cilindro sin gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        adapter = ArrayAdapter.createFromResource(context, R.array.leakage_prompts_cilynder_no_gas,
                                android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(adapter);
                        break;
                    case 3: //Estacionario con gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        adapter = ArrayAdapter.createFromResource(context, R.array.leakage_prompts_stationary_gas,
                                android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(adapter);
                        break;
                    case 4: //Estacionario sin gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        adapter = ArrayAdapter.createFromResource(context, R.array.leakage_prompts_stationary_no_gas,
                                android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(adapter);
                        break;
                    case 5: //Servicio medido con gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        adapter = ArrayAdapter.createFromResource(context, R.array.leakage_prompts_service_gas,
                                android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(adapter);
                        break;
                    case 6: //Servicio medido sin gas
                        mSpinnerLeakResolution.setVisibility(View.VISIBLE);
                        adapter = ArrayAdapter.createFromResource(context, R.array.leakage_prompts_service_no_gas,
                                android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinnerLeakResolution.setAdapter(adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_leakage_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSpinnerLeakChannel.getSelectedItem() == null || mSpinnerLeakResolution.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leak_finish_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    channel = mSpinnerLeakChannel.getSelectedItem().toString();
                    resolution = mSpinnerLeakResolution.getSelectedItem().toString();
                    Log.d(DEBUG_TAG, resolution);
                    Log.d(DEBUG_TAG, channel);
                    callAsyncTaskFinished();
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
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
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
                    strCancellationReason = mSpinnerOptions.getSelectedItem().toString();
                    mSpinnerOptions.setSelection(0);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.leak_cancel_correct), Toast.LENGTH_LONG).show();
                    callAsyncTaskCancelled();
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

    @Override
    public void statusErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void statusSuccessResponse(Leak leak) {
        Log.d(DEBUG_TAG, "Si jala");
        isClicked = true;
        checkButtons();

        String status = leak.getLeakStatus().toString();

        if (status.equals(this.getResources().getString(R.string.order_status_in_progress))) {
            mLeakStatus.setTextColor(amber);
        } else if (status.equals(this.getResources().getString(R.string.order_status_finished))) {
            mLeakStatus.setTextColor(light_green);
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        } else if (status.equals(this.getResources().getString(R.string.order_status_canceled))) {
            mLeakStatus.setTextColor(red);
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        }

        mLeakStatus.setText(leak.getLeakStatus().toString());
    }
}
