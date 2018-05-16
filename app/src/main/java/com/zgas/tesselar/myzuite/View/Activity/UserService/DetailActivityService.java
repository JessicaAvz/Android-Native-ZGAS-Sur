package com.zgas.tesselar.myzuite.View.Activity.UserService;

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

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Class that shows the details of the orders; it's used when the operator is of type
 * 'Custom service'. In this class we can also modify the service status - in progress, cancelled,
 * finished - and open the Waze app with the service address.
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
public class DetailActivityService extends AppCompatActivity implements
        PutStatusOrderTask.StatusOrderTaskListener {

    private final String DEBUG_TAG = getClass().getSimpleName();

    private Bundle bundle;
    private String mStrCaseId;
    private String mStrCaseUserName;
    private String mStrCaseAddress;
    private String mStrCaseStatus;
    private String mStrCaseType;
    private String mStrCasePriority;
    private String mStrCaseTimeIn;
    private String mStrCaseTimeSeen;
    private String mStrCaseTimeArrived;
    private String mStrCaseTimeProgrammed;
    private String mStrCasePaymentMethod;
    private String strTicket;
    private String strQuantity;
    private String strCancellationReason;

    @BindView(R.id.activity_detail_service_tv_client_name)
    TextView mUserName;
    @BindView(R.id.activity_detail_service_tv_case_address)
    TextView mCaseAddress;
    @BindView(R.id.activity_detail_service_tv_status)
    TextView mCaseStatus;
    @BindView(R.id.activity_detail_service_tv_time_in)
    TextView mCaseTimeIn;
    @BindView(R.id.activity_detail_service_tv_time_seen)
    TextView mCaseTimeSeen;
    @BindView(R.id.activity_detail_service_tv_arrived)
    TextView mCaseTimeArrived;
    @BindView(R.id.activity_detail_service_tv_time_programmed)
    TextView mCaseTimeProgrammed;
    @BindView(R.id.activity_detail_service_tv_payment)
    TextView mCasePaymentMethod;
    @Nullable
    @BindView(R.id.activity_detail_service_fab_in_progress)
    FloatingActionButton mFabInProgress;
    @Nullable
    @BindView(R.id.activity_detail_service_fab_finished)
    FloatingActionButton mFabFinished;
    @Nullable
    @BindView(R.id.activity_detail_service_fab_cancel)
    FloatingActionButton mFabCanceled;
    @Nullable
    @BindView(R.id.activity_detail_service_fab_waze)
    FloatingActionButton mFabWaze;

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
    private JSONObject params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(this);
        mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());
        initUi();
        checkButtons();
    }

    /**
     *
     */
    private void initUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bundle = getIntent().getExtras();
        mStrCaseId = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID);
        mStrCaseUserName = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME);
        mStrCaseAddress = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS);
        mStrCaseStatus = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS);
        mStrCaseType = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TYPE);
        mStrCasePriority = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY);
        mStrCaseTimeIn = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT);
        mStrCaseTimeSeen = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN);
        mStrCaseTimeArrived = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL);
        mStrCaseTimeProgrammed = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED);
        mStrCasePaymentMethod = bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detalle del servicio " + mStrCaseId);

        mUserName.setText(mStrCaseUserName);
        mCaseAddress.setText(mStrCaseAddress);
        mCaseStatus.setText(mStrCaseStatus);
        mCaseTimeIn.setText(String.valueOf(mStrCaseTimeIn));
        mCaseTimeSeen.setText(String.valueOf(mStrCaseTimeSeen));
        mCaseTimeArrived.setText(String.valueOf(mStrCaseTimeArrived));
        mCaseTimeProgrammed.setText(String.valueOf(mStrCaseTimeProgrammed));
        mCasePaymentMethod.setText(mStrCasePaymentMethod);
        mFabFinished.setVisibility(View.GONE);
        mFabCanceled.setVisibility(View.GONE);
        mFabWaze.setVisibility(View.GONE);
        mCaseStatus.setTextColor(blue);
    }

    /**
     * @param menuItem
     * @return
     */
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

    private void checkButtons() {
        if (isClicked == true || mStrCaseStatus.equals(this.getResources().getString(R.string.order_status_in_progress))) {
            mFabFinished.show();
            mFabCanceled.show();
            mFabWaze.show();
            mFabInProgress.hide();
        }
    }

    @OnClick(R.id.activity_detail_service_fab_in_progress)
    public void onClickProgress() {
        inProgressDialog();
    }

    @OnClick(R.id.activity_detail_service_fab_finished)
    public void onClickFinished() {
        finishDialog();
    }

    @OnClick(R.id.activity_detail_service_fab_cancel)
    public void onClickCanceled() {
        cancelDialog();
    }

    @OnClick(R.id.activity_detail_service_fab_waze)
    public void onClickWaze() {
        wazeIntent(mStrCaseAddress);
    }

    private void callAsyncTaskInProgress() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, mStrCaseId);
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, String.valueOf(R.string.order_status_in_progress));
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
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_TICKET, strTicket);
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_QUANTITY, strQuantity);
            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) +
                    "Id: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID) +
                    "Ticket: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_TICKET) +
                    "Quantity: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_QUANTITY));

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
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_CANCELATION_REASON, strCancellationReason);
            Log.d(DEBUG_TAG, "Status: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS) + " ID: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID));

            PutStatusOrderTask putStatusOrderTask = new PutStatusOrderTask(this, params);
            putStatusOrderTask.setStatusOrderTaskListener(this);
            putStatusOrderTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wazeIntent(String address) {
        final String url = "waze://?q=" + address;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void finishDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_finish_case_operator_stationary);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
        Log.d(DEBUG_TAG, "Finish dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final EditText etQuantity = dialog.findViewById(R.id.dialog_finish_case_stationary_tv_quantity);
        final EditText etTicket = dialog.findViewById(R.id.dialog_finish_case_stationary_tv_ticket_number);

        Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_stationary_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(etQuantity) || isEmpty(etTicket)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_finish_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    strQuantity = etQuantity.getText().toString();
                    strTicket = etTicket.getText().toString();
                    Log.d(DEBUG_TAG, "Cantidad surtida " + strQuantity);
                    Log.d(DEBUG_TAG, "Folio del ticket " + strTicket);
                    etQuantity.getText().clear();
                    etTicket.getText().clear();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_finish_correct), Toast.LENGTH_LONG).show();
                    callAsyncTaskFinished();
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
        dialog.show();
    }

    private void cancelDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_cancel_case_operator);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;

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
                    strCancellationReason = mSpinnerOptions.getSelectedItem().toString();
                    mSpinnerOptions.setSelection(0);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_cancel_correct), Toast.LENGTH_LONG).show();
                    callAsyncTaskCancelled();
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
