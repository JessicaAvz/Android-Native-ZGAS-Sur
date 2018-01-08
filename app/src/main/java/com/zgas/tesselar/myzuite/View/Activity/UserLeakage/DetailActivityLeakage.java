package com.zgas.tesselar.myzuite.View.Activity.UserLeakage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.zgas.tesselar.myzuite.Controller.UserPreferences;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.View.Adapter.NothingSelectedSpinnerAdapter;

public class DetailActivityLeakage extends AppCompatActivity implements View.OnClickListener {

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

    private TextView mUserId;
    private TextView mUserName;
    private TextView mLeakAddress;
    private TextView mLeakStatus;
    private TextView mLeakTimeIn;
    private TextView mLeakTimeSeen;
    private TextView mLeakTimeArrived;
    private TextView mLeakTimeScheduled;
    private TextView mLeakCylinderCapacity;
    private TextView mLeakCylinderColor;
    private TextView mLeakChannel;
    private FloatingActionButton mFabInProgress;
    private FloatingActionButton mFabFinished;
    private FloatingActionButton mFabCanceled;
    private FloatingActionButton mFabWaze;
    private UserPreferences mUserPreferences;
    private User mUser;
    private boolean isClicked = false;

    private ArrayAdapter<CharSequence> adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_leakage);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(this);
        mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());
        context = this;
        initUi();
    }

    /**
     *
     */
    private void checkButtons() {
        if (isClicked == true) {
            mFabFinished.setVisibility(View.VISIBLE);
            mFabCanceled.setVisibility(View.VISIBLE);
            mFabInProgress.setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        mLeakChannel = (TextView) findViewById(R.id.activity_detail_leakage_tv_channel);
        mLeakChannel.setText(mStrChannel);
        Log.d(DEBUG_TAG, "Bundle - Id de la fuga: " + mStrLeakId);
        Log.d(DEBUG_TAG, "Bundle - WhoReports: " + mStrLeakClientName);
        Log.d(DEBUG_TAG, "Bundle - Subject: " + mStrLeakSubject);
        Log.d(DEBUG_TAG, "Bundle - Status: " + mStrLeakStatus);
        Log.d(DEBUG_TAG, "Bundle - RecordTypeName: " + mStrLeakType);
        Log.d(DEBUG_TAG, "Bundle - Priority: " + mStrLeakPriority);
        Log.d(DEBUG_TAG, "Bundle - FolioSalesNote: " + mStrLeakFolioSalesNote);
        Log.d(DEBUG_TAG, "Bundle - DateTimeTechnician: " + mStrLeakTimeTechnician);
        Log.d(DEBUG_TAG, "Bundle - DateTimeScheduled: " + mStrLeakTimeScheduled);
        Log.d(DEBUG_TAG, "Bundle - DateTimeEnd: " + mStrLeakTimeArrived);
        Log.d(DEBUG_TAG, "Bundle - DateTimeDeparture: " + mStrLeakTimeDeparture);
        Log.d(DEBUG_TAG, "Bundle - CylinderCapacity: " + mStrCylinderCapacity);
        Log.d(DEBUG_TAG, "Bundle - CylinderColor: " + mStrCylinderColor);
        Log.d(DEBUG_TAG, "Bundle - Channel: " + mStrChannel);
        Log.d(DEBUG_TAG, "Bundle - Address: " + mStrLeakAddress);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detalle de la fuga " + mStrLeakId);

        mUserName = findViewById(R.id.activity_detail_leakage_tv_client_name);
        mUserName.setText(mStrLeakClientName);
        mLeakAddress = findViewById(R.id.activity_detail_leakage_tv_case_address);
        mLeakAddress.setText(mStrLeakAddress);
        mLeakStatus = findViewById(R.id.activity_detail_leakage_tv_status);
        mLeakStatus.setText(mStrLeakStatus);
        mLeakCylinderColor = findViewById(R.id.activity_detail_leakage_tv_color);
        mLeakCylinderColor.setText(mStrCylinderColor);
        mLeakCylinderCapacity = findViewById(R.id.activity_detail_leakage_tv_capacity);
        mLeakCylinderCapacity.setText(mStrCylinderCapacity);

        mFabInProgress = findViewById(R.id.activity_detail_leakage_fab_in_progress);
        mFabInProgress.setOnClickListener(this);
        mFabFinished = findViewById(R.id.activity_detail_leakage_fab_finished);
        mFabFinished.setOnClickListener(this);
        mFabCanceled = findViewById(R.id.activity_detail_leakage_fab_cancel);
        mFabCanceled.setOnClickListener(this);
        mFabWaze = findViewById(R.id.activity_detail_leakage_fab_waze);
        mFabWaze.setOnClickListener(this);

        mLeakTimeIn = findViewById(R.id.activity_detail_leakage_tv_time_in);
        if (mStrLeakTimeTechnician == null || mStrLeakTimeTechnician.equals("")) {
            mLeakTimeIn.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeIn.setText(mStrLeakTimeTechnician);
        }
        mLeakTimeSeen = findViewById(R.id.activity_detail_leakage_tv_time_seen);
        if (mStrLeakTimeSeen == null || mStrLeakTimeSeen.equals("")) {
            mLeakTimeSeen.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeSeen.setText(mStrLeakTimeSeen);
        }
        mLeakTimeArrived = findViewById(R.id.activity_detail_leakage_tv_arrived);
        if (mStrLeakTimeArrived == null || mStrLeakTimeArrived.equals("")) {
            mLeakTimeArrived.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeArrived.setText(mStrLeakTimeArrived);
        }
        mLeakTimeScheduled = findViewById(R.id.activity_detail_leakage_tv_time_programmed);
        if (mStrLeakTimeScheduled == null || mStrLeakTimeScheduled.equals("")) {
            mLeakTimeScheduled.setText(getResources().getString(R.string.no_data));
        } else {
            mLeakTimeScheduled.setText(mStrLeakTimeScheduled);
        }

        if (mStrLeakStatus.equals(Order.caseStatus.INPROGRESS.toString())) {
            mLeakStatus.setTextColor(getResources().getColor(R.color.amber));
        } else if (mStrLeakStatus.equals(Order.caseStatus.FINISHED.toString())) {
            mLeakStatus.setTextColor(getResources().getColor(R.color.light_green));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        } else if (mStrLeakStatus.equals(Order.caseStatus.CANCELLED.toString())) {
            mLeakStatus.setTextColor(getResources().getColor(R.color.red));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        } else {
            mLeakStatus.setTextColor(getResources().getColor(R.color.orange));
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
                wazeIntent(mStrLeakAddress);
                break;
        }
    }

    /**
     * @param address
     */
    private void wazeIntent(String address) {
        final String url = "waze://?q=" + address;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /**
     *
     */
    private void inProgressDialog() {
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        isClicked = true;

        new FancyAlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_in_progress_title))
                .setBackgroundColor(getResources().getColor(R.color.amber))
                .setMessage(getResources().getString(R.string.dialog_in_progress_body))
                .setNegativeBtnText(getResources().getString(R.string.cancel))
                .setPositiveBtnBackground(getResources().getColor(R.color.amber))
                .setPositiveBtnText(getResources().getString(R.string.dialog_in_progress_accept))
                .setNegativeBtnBackground(getResources().getColor(R.color.grey_300))
                .setAnimation(Animation.SIDE)
                .isCancellable(false)
                .setIcon(R.drawable.icon_progress, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        //change order status
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                    }
                })
                .build();

        checkButtons();
    }

    /**
     *
     */
    private void finishDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_finish_case_leakage);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
        Log.d(DEBUG_TAG, "Finish dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final Spinner mSpinnerOptions = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_option);
        final Spinner mSpinnerSecondOption = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_second_option);
        mSpinnerSecondOption.setVisibility(View.GONE);
        final Spinner mSpinnerThirdOption = dialog.findViewById(R.id.dialog_finish_case_leakage_sp_third_option);
        mSpinnerThirdOption.setVisibility(View.GONE);

        adapter = ArrayAdapter.createFromResource(this, R.array.leakage_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, this));
        mSpinnerOptions.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1 || i == 2 || i == 3 || i == 4) {
                    mSpinnerSecondOption.setVisibility(View.VISIBLE);
                    adapter = ArrayAdapter.createFromResource(context, R.array.leakage_state, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mSpinnerSecondOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, context));

                    mSpinnerSecondOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (adapterView.getSelectedItemPosition() == 1 && i == 1) {
                                //Fuga cilindro con gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(context,
                                        R.array.leakage_prompts_cilynder_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, context));
                            } else if (adapterView.getSelectedItemPosition() == 1 && i == 2) {
                                //Fuga cilindro sin gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(context,
                                        R.array.leakage_prompts_cilynder_no_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, context));
                            } else if (adapterView.getSelectedItemPosition() == 2 && i == 1) {
                                //Fuga estacionario con gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(context,
                                        R.array.leakage_prompts_stationary_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, context));
                            } else if (adapterView.getSelectedItemPosition() == 2 && i == 2) {
                                //Fuga estacionario sin gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(context,
                                        R.array.leakage_prompts_stationary_no_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, context));
                            } else if (adapterView.getSelectedItemPosition() == 3 && i == 1) {
                                //Fuga servicio medido con gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(context,
                                        R.array.leakage_prompts_service_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, context));
                            } else if (adapterView.getSelectedItemPosition() == 3 && i == 2) {
                                //Fuga servicio medido sin gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(context,
                                        R.array.leakage_prompts_service_no_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, context));
                            } else if (adapterView.getSelectedItemPosition() == 4 && i == 1) {
                                //Fuga cliente con gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(context,
                                        R.array.leakage_prompts_client_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, context));
                            } else if (adapterView.getSelectedItemPosition() == 4 && i == 2) {
                                //Fuga cliente sin gas
                                mSpinnerThirdOption.setVisibility(View.VISIBLE);
                                adapter = ArrayAdapter.createFromResource(context,
                                        R.array.leakage_prompts_client_no_gas, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mSpinnerThirdOption.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                                        R.layout.contact_spinner_row_nothing_selected, context));
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

    /**
     *
     */
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
