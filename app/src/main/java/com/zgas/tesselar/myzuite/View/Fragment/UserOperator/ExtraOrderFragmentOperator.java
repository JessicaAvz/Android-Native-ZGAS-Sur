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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


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
 * @see ButterKnife
 */
public class ExtraOrderFragmentOperator extends Fragment implements
        PutNewOrderTask.NewOrderTaskListener {

    private final String DEBUG_TAG = getClass().getSimpleName();
    @BindView(R.id.fragment_order_operator_et_username)
    EditText mUserName;
    @BindView(R.id.fragment_order_operator_et_phone)
    EditText mPhoneNumber;
    private String userName;
    private String userPhone;
    private UserPreferences mUserPreferences;
    private Dialog dialog;
    private Unbinder unbinder;

    public ExtraOrderFragmentOperator() {
        // Required empty public constructor
    }


    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_order_operator, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getContext());
        User mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());
        return mRootView;
    }

    @SuppressLint("LongLogTag")
    @OnClick(R.id.fragment_order_operator_btn_make_order)
    public void onClick() {
        Log.d(DEBUG_TAG, "Butterknife onclick");
        makeOrder();
    }

    @SuppressLint("LongLogTag")
    private void callAsyncTask() {
        JSONObject params = new JSONObject();
        try {
            params.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, mUserPreferences.getUserObject().getUserId());
            params.put(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_NAME, userName);
            params.put(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_PHONE, userPhone);
            params.put(ExtrasHelper.MY_ZUITE, true);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
