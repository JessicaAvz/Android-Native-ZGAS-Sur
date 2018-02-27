package com.zgas.tesselar.myzuite.View.Fragment.UserOperator;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutNewOrderTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;


/**
 * This class is for requesting a new order when the userType is of 'operator'
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see Order
 * @see UserPreferences
 * @see android.os.AsyncTask
 * @see PutNewOrderTask
 */
public class ExtraOrderFragmentOperator extends Fragment implements View.OnClickListener,
        PutNewOrderTask.NewOrderTaskListener {

    private static final String DEBUG_TAG = "ExtraOrderFragmentOperator";
    private View mRootView;
    private EditText mUserName;
    private EditText mPhoneNumber;
    private String userName;
    private String userPhone;
    private Button mMakeOrder;
    private UserPreferences mUserPreferences;
    private User mUser;
    private JSONObject params;
    private Dialog dialog;

    public ExtraOrderFragmentOperator() {
        // Required empty public constructor
    }


    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_order_operator, container, false);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getContext());
        mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());
        initUi(mRootView);
        return mRootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_order_operator_btn_make_order:
                makeOrder();
                break;
        }
    }

    private void initUi(View rootview) {
        mUserName = rootview.findViewById(R.id.fragment_order_operator_et_username);
        mPhoneNumber = rootview.findViewById(R.id.fragment_order_operator_et_phone);
        mMakeOrder = rootview.findViewById(R.id.fragment_order_operator_btn_make_order);
        mMakeOrder.setOnClickListener(this);
    }

    @SuppressLint("LongLogTag")
    private void callAsyncTask() {
        params = new JSONObject();
        try {
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, mUserPreferences.getUserObject().getUserId());
            params.put(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_NAME, userName);
            params.put(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_PHONE, userPhone);

            Log.d(DEBUG_TAG, "Id: " + params.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID)
                    + " Razón: " + params.getString(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_NAME)
                    + " Fecha: " + params.getString(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_PHONE));

            PutNewOrderTask putNewOrderTask = new PutNewOrderTask(getContext(), params);
            putNewOrderTask.setNewOrderTaskListener(this);
            putNewOrderTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    public void makeOrder() {
        if (isEmpty(mUserName) || isEmpty(mPhoneNumber)) {
            Toast.makeText(getContext(), getResources().getString(R.string.order_new_incorrect), Toast.LENGTH_LONG).show();
        } else {
            userName = mUserName.getText().toString();
            userPhone = mPhoneNumber.getText().toString();
            callAsyncTask();
            Log.d(DEBUG_TAG, "Nombre de usuario: " + userName);
            Log.d(DEBUG_TAG, "Teléfono del usuario: " + userPhone);
            mUserName.getText().clear();
            mPhoneNumber.getText().clear();
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void newOrderErrorResponse(String error) {
        Log.d(DEBUG_TAG, error);
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void newOrderSuccessResponse(Order order) {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_new_order);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;
        Log.d(DEBUG_TAG, "Dialog " + getResources().getString(R.string.on_create));
        dialog.setCancelable(false);

        final Button mBtnAccept = dialog.findViewById(R.id.dialog_new_order_btn_accept);
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
