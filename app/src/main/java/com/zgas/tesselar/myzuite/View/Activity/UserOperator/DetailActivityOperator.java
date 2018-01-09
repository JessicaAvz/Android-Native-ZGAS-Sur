package com.zgas.tesselar.myzuite.View.Activity.UserOperator;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.zgas.tesselar.myzuite.Service.UserPreferences;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;

/**
 * @author Jessica Arvizu
 *         Clase que muestra los detalles de un pedido, cuando el Operador es tipo Operador...
 */
public class DetailActivityOperator extends AppCompatActivity implements View.OnClickListener {

    private static final String DEBUG_TAG = "DetailActivityOperator";

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

    private TextView mUserName;
    private TextView mCaseAddress;
    private TextView mCaseStatus;
    private TextView mCaseTimeIn;
    private TextView mCaseTimeSeen;
    private TextView mCaseTimeArrived;
    private TextView mCaseTimeProgrammed;
    private TextView mCasePaymentMethod;
    private FloatingActionButton mFabInProgress;
    private FloatingActionButton mFabFinished;
    private FloatingActionButton mFabCanceled;
    private FloatingActionButton mFabWaze;

    private UserPreferences userPreferences;
    private User user;
    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_operator);
        overridePendingTransition(R.anim.pull_in_right, R.anim.no_change);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        userPreferences = new UserPreferences(this);
        user = userPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + user.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + user.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + user.getUserType());
        initUi();
    }

    /**
     * Método que muestra los botones de aceptar y cancelar, y esconde el botón de en progreso,
     * una vez que la bandera = true.
     */
    private void checkButtons() {
        if (isClicked == true) {
            mFabFinished.show();
            mFabCanceled.show();
            mFabInProgress.hide();
        }
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

    /**
     * Método que inicializa la interfaz de usuario, y obtiene los datos del bundle.
     */
    private void initUi() {
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

        Log.d(DEBUG_TAG, "Id del pedido: " + String.valueOf(mStrCaseId));
        //Log.d(DEBUG_TAG, "Id del cliente: " + String.valueOf(mIntCaseUserId));
        Log.d(DEBUG_TAG, "Nombre del cliente: " + mStrCaseUserName);
        Log.d(DEBUG_TAG, "Dirección de la entrega: " + mStrCaseAddress);
        Log.d(DEBUG_TAG, "Estatus del pedido: " + mStrCaseStatus);
        Log.d(DEBUG_TAG, "Tipo de pedido: " + mStrCaseType);
        Log.d(DEBUG_TAG, "Prioridad del pedido: " + mStrCasePriority);
        Log.d(DEBUG_TAG, "Hora de pedido: " + mStrCaseTimeIn);
        Log.d(DEBUG_TAG, "Hora de visualización del pedido: " + mStrCaseTimeSeen);
        Log.d(DEBUG_TAG, "Hora de llegada del pedido: " + mStrCaseTimeArrived);
        Log.d(DEBUG_TAG, "Hora programada del pedido: " + mStrCaseTimeProgrammed);
        Log.d(DEBUG_TAG, "Tipo de pago del pedido: " + mStrCasePaymentMethod);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Detalle del pedido " + mStrCaseId);

        mUserName = findViewById(R.id.activity_detail_operator_tv_client_name);
        if (mStrCaseUserName == null || mStrCaseUserName.equals("")) {
            mUserName.setText(getResources().getString(R.string.no_data));
        } else {
            mUserName.setText(mStrCaseUserName);
        }
        mCaseAddress = findViewById(R.id.activity_detail_operator_tv_case_address);
        if (mStrCaseAddress == null || mStrCaseAddress.equals("")) {
            mCaseAddress.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseAddress.setText(mStrCaseAddress);
        }
        mCaseStatus = findViewById(R.id.activity_detail_operator_tv_status);
        if (mStrCaseStatus == null || mStrCaseStatus.equals("")) {
            mCaseStatus.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseStatus.setText(mStrCaseStatus);
        }
        mCaseTimeIn = findViewById(R.id.activity_detail_operator_tv_time_in);
        if (mStrCaseTimeIn == null || mStrCaseTimeIn.equals("")) {
            mCaseTimeIn.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeIn.setText(mStrCaseTimeIn);
        }
        mCaseTimeSeen = findViewById(R.id.activity_detail_operator_tv_time_seen);
        if (mStrCaseTimeSeen == null || mStrCaseTimeSeen.equals("")) {
            mCaseTimeSeen.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeSeen.setText(mStrCaseTimeSeen);
        }
        mCaseTimeArrived = findViewById(R.id.activity_detail_operator_tv_arrived);
        if (mStrCaseTimeArrived == null || mStrCaseTimeArrived.equals("")) {
            mCaseTimeArrived.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeArrived.setText(mStrCaseTimeArrived);
        }
        mCaseTimeProgrammed = findViewById(R.id.activity_detail_operator_tv_time_programmed);
        if (mStrCaseTimeProgrammed == null || mStrCaseTimeProgrammed.equals("")) {
            mCaseTimeProgrammed.setText(getResources().getString(R.string.no_data));
        } else {
            mCaseTimeProgrammed.setText(mStrCaseTimeProgrammed);
        }
        mCasePaymentMethod = findViewById(R.id.activity_detal_operator_tv_payment);
        if (mStrCasePaymentMethod == null || mStrCasePaymentMethod.equals("")) {
            mCasePaymentMethod.setText(getResources().getString(R.string.no_data));
        } else {
            mCasePaymentMethod.setText(mStrCasePaymentMethod);
        }
        mFabInProgress = findViewById(R.id.activity_detail_operator_fab_in_progress);
        mFabInProgress.setOnClickListener(this);
        mFabFinished = findViewById(R.id.activity_detail_operator_fab_finished);
        mFabFinished.setOnClickListener(this);
        mFabFinished.setVisibility(View.GONE);
        mFabCanceled = findViewById(R.id.activity_detail_operator_fab_cancel);
        mFabCanceled.setOnClickListener(this);
        mFabCanceled.setVisibility(View.GONE);
        mFabWaze = findViewById(R.id.activity_detail_operator_fab_waze);
        mFabWaze.setOnClickListener(this);

        if (mStrCaseStatus.equals(Order.caseStatus.INPROGRESS.toString())) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.amber));
        } else if (mStrCaseStatus.equals(Order.caseStatus.FINISHED.toString())) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.light_green));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        } else if (mStrCaseStatus.equals(Order.caseStatus.CANCELLED.toString())) {
            mCaseStatus.setTextColor(getResources().getColor(R.color.red));
            mFabInProgress.setVisibility(View.GONE);
            mFabFinished.setVisibility(View.GONE);
            mFabCanceled.setVisibility(View.GONE);
            mFabWaze.setVisibility(View.GONE);
        } else {
            mCaseStatus.setTextColor(getResources().getColor(R.color.orange));
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

    /**
     * Método que abre la aplicación de Waze, recibe la dirección de la fuga y después
     * la pinta en Waze.
     *
     * @param address - Dirección de la fuga.
     */
    private void wazeIntent(String address) {
        final String url = "waze://?q=" + address;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /**
     *
     */
    private void finishDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_finish_case_operator);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final EditText etQuantity = dialog.findViewById(R.id.dialog_finish_case_tv_quantity);
        final EditText etTicket = dialog.findViewById(R.id.dialog_finish_case_tv_ticket_number);

        Button mBtnAccept = dialog.findViewById(R.id.dialog_finish_case_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(etQuantity) || isEmpty(etTicket)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_finish_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    final String quantity = etQuantity.getText().toString();
                    final String ticket = etTicket.getText().toString();
                    Log.d(DEBUG_TAG, "Cantidad surtida " + quantity);
                    Log.d(DEBUG_TAG, "Folio del ticket " + ticket);
                    etQuantity.getText().clear();
                    etTicket.getText().clear();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_finish_correct), Toast.LENGTH_LONG).show();
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

    /**
     *
     */
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
                    Log.d(DEBUG_TAG, mSpinnerOptions.getSelectedItem().toString());
                    mSpinnerOptions.setSelection(0);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.order_cancel_correct), Toast.LENGTH_LONG).show();
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

    /**
     *
     */
    private void inProgressDialog() {
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));

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
                        isClicked = true;
                        checkButtons();
                        //change order status
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

    /**
     * @param etText
     * @return
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}

