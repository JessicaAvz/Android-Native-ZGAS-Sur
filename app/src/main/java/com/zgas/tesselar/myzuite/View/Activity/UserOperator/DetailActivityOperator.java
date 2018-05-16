package com.zgas.tesselar.myzuite.View.Activity.UserOperator;

import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutStatusOrderTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Class that shows the details of the orders; it's used when the operator is of type 'Operator'.
 * In this class we can also modify the order status - in progress, cancelled, finished -
 * and open the Waze app with the order address.
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see Order
 * @see User
 * @see UserPreferences
 * @see Bundle
 * @see android.os.AsyncTask
 * @see PutStatusOrderTask
 */
public class DetailActivityOperator extends AppCompatActivity implements
        PutStatusOrderTask.StatusOrderTaskListener {

    private final String DEBUG_TAG = getClass().getSimpleName();

    private String mStrCaseId;
    private String mStrCaseAddress;
    private String mStrCaseStatus;
    private String mStrCaseTimeSeen;
    private String mStrCaseServiceType;

    private String mStrTicket;
    private String mStrQuantity;
    private String mStrCylinder10;
    private String mStrCylinder20;
    private String mStrCylinder30;
    private String mStrCylinder45;
    private String mStrCancellationReason;

    @BindView(R.id.activity_detail_operator_tv_client_name)
    TextView mUserName;
    @BindView(R.id.activity_detail_operator_tv_case_address)
    TextView mCaseAddress;
    @BindView(R.id.activity_detail_operator_tv_status)
    TextView mCaseStatus;
    @BindView(R.id.activity_detail_operator_tv_time_in)
    TextView mCaseTimeIn;
    @BindView(R.id.activity_detail_operator_tv_time_seen)
    TextView mCaseTimeSeen;
    @BindView(R.id.activity_detail_operator_tv_arrived)
    TextView mCaseTimeArrived;
    @BindView(R.id.activity_detail_operator_tv_time_programmed)
    TextView mCaseTimeProgrammed;
    @BindView(R.id.activity_detal_operator_tv_payment)
    TextView mCasePaymentMethod;
    @Nullable
    @BindView(R.id.dialog_finish_case_stationary_tv_quantity)
    EditText etQuantity;
    @Nullable
    @BindView(R.id.dialog_finish_case_stationary_tv_ticket_number)
    EditText etTicket;
    @Nullable
    @BindView(R.id.activity_detail_operator_fab_in_progress)
    FloatingActionButton mFabInProgress;
    @Nullable
    @BindView(R.id.activity_detail_operator_fab_finished)
    FloatingActionButton mFabFinished;
    @Nullable
    @BindView(R.id.activity_detail_operator_fab_cancel)
    FloatingActionButton mFabCanceled;
    @Nullable
    @BindView(R.id.activity_detail_operator_fab_waze)
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

    private boolean isClicked = false;
    private JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_operator);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        UserPreferences userPreferences = new UserPreferences(this);
        User user = userPreferences.getUserObject();
        initUi();
        checkButtons();
    }

    private void checkButtons() {
        if (isClicked || mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_in_progress))) {
            mFabFinished.show();
            mFabCanceled.show();
            mFabWaze.show();
            mFabInProgress.hide();
        }
    }

    @OnClick(R.id.activity_detail_operator_fab_in_progress)
    public void onClickProgress() {
        inProgressDialog();
    }

    @OnClick(R.id.activity_detail_operator_fab_finished)
    public void onClickFinished() {
        finishDialog();
    }

    @OnClick(R.id.activity_detail_operator_fab_cancel)
    public void onClickCanceled() {
        cancelDialog();
    }

    @OnClick(R.id.activity_detail_operator_fab_waze)
    public void onClickWaze() {
        wazeIntent(mStrCaseAddress);
    }

    private void initUi() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        String date = dateFormat.format(calendar.getTime());

        Bundle bundle = getIntent().getExtras();
        mStrCaseId = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID);
        String mStrCaseUserName = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME);
        mStrCaseAddress = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS);
        mStrCaseStatus = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS);
        String mStrCaseType = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TYPE);
        String mStrCasePriority = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY);
        String mStrCaseTimeIn = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT);
        String mStrCaseTimeArrived = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL);
        String mStrCaseTimeProgrammed = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED);
        String mStrCasePaymentMethod = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD);
        String mStrCaseRecordType = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_RECORD_TYPE);
        mStrCaseServiceType = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getApplicationContext().getResources().getString(R.string.prompt_detail_operator) + " " + mStrCaseId);

        if (mStrCaseUserName == null || mStrCaseUserName.equals("")) {
            mUserName.setText(getResources().getString(R.string.no_data));
        } else {
            mUserName.setText(mStrCaseUserName);
        }

        if (mStrCaseAddress == null || mStrCaseAddress.equals("")) {
            mCaseAddress.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseAddress.setText(mStrCaseAddress);
        }

        if (mStrCaseStatus == null || mStrCaseStatus.equals("")) {
            mCaseStatus.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseStatus.setText(mStrCaseStatus);
        }

        if (mStrCaseTimeIn == null || mStrCaseTimeIn.equals("")) {
            mCaseTimeIn.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeIn.setText(mStrCaseTimeIn);
        }

        if (mStrCaseTimeSeen == null || mStrCaseTimeSeen.equals("")) {
            mCaseTimeSeen.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeSeen.setText(mStrCaseTimeSeen);
        }

        if (mStrCaseTimeArrived == null || mStrCaseTimeArrived.equals("")) {
            mCaseTimeArrived.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeArrived.setText(mStrCaseTimeArrived);
        }

        if (mStrCaseTimeProgrammed == null || mStrCaseTimeProgrammed.equals("")) {
            mCaseTimeProgrammed.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeProgrammed.setText(mStrCaseTimeProgrammed);
        }

        if (mStrCasePaymentMethod == null || mStrCasePaymentMethod.equals("")) {
            mCasePaymentMethod.setText(getResources().getString(R.string.no_data));
        } else {
            mCasePaymentMethod.setText(mStrCasePaymentMethod);
        }

        mFabFinished.setVisibility(View.GONE);
        mFabCanceled.setVisibility(View.GONE);
        mFabWaze.setVisibility(View.GONE);
        mCaseStatus.setTextColor(blue);
    }

    private void callAsyncTaskInProgress() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, mStrCaseId);
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, this.getResources().getString(R.string.order_status_in_progress));
            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) + " ID: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID));

            PutStatusOrderTask putStatusOrderTask = new PutStatusOrderTask(this, params);
            putStatusOrderTask.setStatusOrderTaskListener(this);
            putStatusOrderTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAsyncTaskFinished() {
        JSONObject params = new JSONObject();
        try {
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, mStrCaseId);
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, String.valueOf(R.string.order_status_finished));
            if (mStrCaseServiceType.equals(this.getResources().getString(R.string.service_static))) {
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_10, mStrCylinder10);
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_20, mStrCylinder20);
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_30, mStrCylinder30);
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_45, mStrCylinder45);
                Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) +
                        "Id: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID) +
                        "Cylinder 10: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_10) +
                        "Cylinder 20: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_20) +
                        "Cylinder 30: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_30) +
                        "Cylinder 45: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_CYLINDER_45));
            } else if (mStrCaseServiceType.equals(this.getResources().getString(R.string.service_cylinder))) {
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_TICKET, mStrTicket);
                params.put(ExtrasHelper.ORDER_JSON_OBJECT_QUANTITY, mStrQuantity);
                Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) +
                        "Id: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID) +
                        "Ticket: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_TICKET) +
                        "Quantity: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_QUANTITY));
            }
            PutStatusOrderTask putStatusOrderTask = new PutStatusOrderTask(this, params);
            putStatusOrderTask.setStatusOrderTaskListener(this);
            putStatusOrderTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAsyncTaskCancelled() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, mStrCaseId);
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, String.valueOf(R.string.order_status_canceled));
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_CANCELATION_REASON, mStrCancellationReason);
            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) + " ID: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID));

            PutStatusOrderTask putStatusOrderTask = new PutStatusOrderTask(this, params);
            putStatusOrderTask.setStatusOrderTaskListener(this);
            putStatusOrderTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
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

    private void wazeIntent(String address) {
        final String url = "waze://?q=" + address;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void finishDialog() {
        final Dialog dialog = new Dialog(this);
        if (mStrCaseServiceType.equals("Cilindro")) {
            dialog.setContentView(R.layout.dialog_finish_case_operator_cylinder);
            dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
            Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
            dialog.setCancelable(false);

            final EditText et10Kilograms = dialog.findViewById(R.id.dialog_finish_case_cylinder_10_edit_text);
            final EditText et20Kilograms = dialog.findViewById(R.id.dialog_finish_case_cylinder_20_edit_text);
            final EditText et30Kilograms = dialog.findViewById(R.id.dialog_finish_case_cylinder_30_edit_text);
            final EditText et45Kilograms = dialog.findViewById(R.id.dialog_finish_case_cylinder_45_edit_text);

            Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_cylinder_btn_accept);
            mBtnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mStrCylinder10 = et10Kilograms.getText().toString();
                    mStrCylinder20 = et20Kilograms.getText().toString();
                    mStrCylinder30 = et30Kilograms.getText().toString();
                    mStrCylinder45 = et45Kilograms.getText().toString();
                    callAsyncTaskFinished();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_finish_correct), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });

            Button mBtnCancel = dialog.findViewById(R.id.dialog_finish_case_cylinder_btn_cancel);
            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } else if (mStrCaseServiceType.equals("Estacionario")) {
            dialog.setContentView(R.layout.dialog_finish_case_operator_stationary);
            dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
            Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
            dialog.setCancelable(false);

            etQuantity = dialog.findViewById(R.id.dialog_finish_case_stationary_tv_quantity);
            etTicket = dialog.findViewById(R.id.dialog_finish_case_stationary_tv_ticket_number);

            Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_stationary_btn_accept);
            mBtnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEmpty(etQuantity) || isEmpty(etTicket)) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_finish_incorrect), Toast.LENGTH_LONG).show();
                    } else {
                        mStrQuantity = etQuantity.getText().toString();
                        mStrTicket = etTicket.getText().toString();
                        Log.d(DEBUG_TAG, "Cantidad surtida " + mStrQuantity);
                        Log.d(DEBUG_TAG, "Folio del ticket " + mStrTicket);
                        etQuantity.getText().clear();
                        etTicket.getText().clear();
                        callAsyncTaskFinished();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_finish_correct), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }
            });

            Button mBtnCancel = dialog.findViewById(R.id.dialog_finish_case_stationary_btn_cancel);
            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    private void cancelDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_cancel_case_operator);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;

        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
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
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_cancel_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    mStrCancellationReason = mSpinnerOptions.getSelectedItem().toString();
                    mSpinnerOptions.setSelection(0);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_cancel_correct), Toast.LENGTH_LONG).show();
                    callAsyncTaskCancelled();
                    dialog.dismiss();
                }
            }
        });

        Button mBtnCancel = dialog.findViewById(R.id.dialog_cancel_case_btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void statusErrorResponse(String error) {
        Log.d(DEBUG_TAG, "Error response: " + error);
        Toast.makeText(this, "Error " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void statusSuccessResponse(Order order) {
        Log.d(DEBUG_TAG, "Si jala");
        isClicked = true;
        checkButtons();

        String status = order.getOrderStatus().toString();

        if (status.equals(this.getResources().getString(R.string.order_status_in_progress))) {
            mCaseStatus.setTextColor(amber);
        } else if (status.equals(this.getResources().getString(R.string.order_status_finished))) {
            mCaseStatus.setTextColor(light_green);
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        } else if (status.equals(this.getResources().getString(R.string.order_status_canceled))) {
            mCaseStatus.setTextColor(red);
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        }

        mCaseStatus.setText(order.getOrderStatus().toString());
    }
}

