package com.zgas.tesselar.myzuite.Controller.Fragment.UserOperator;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragmentOperator extends Fragment implements View.OnClickListener {

    private static final String DEBUG_TAG = "OrderFragmentOperator";
    private View mRootView;
    private EditText mUserName;
    private EditText mPhoneNumber;
    private Button mMakeOrder;
    private UserPreferences mUserPreferences;
    private User mUser;

    public OrderFragmentOperator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_order_operator, container, false);
        Log.d(DEBUG_TAG, "OnCreate");
        mUserPreferences = new UserPreferences(getContext());
        mUser = mUserPreferences.getUser();
        Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getUser().getUserEmail());
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

    private void initUi(View pRootView) {
        mUserName = pRootView.findViewById(R.id.fragment_order_operator_et_username);
        mPhoneNumber = pRootView.findViewById(R.id.fragment_order_operator_et_phone);
        mMakeOrder = pRootView.findViewById(R.id.fragment_order_operator_btn_make_order);
        mMakeOrder.setOnClickListener(this);
    }

    public void makeOrder() {
        if (isEmpty(mUserName) || isEmpty(mPhoneNumber)) {
            Toast.makeText(getContext(), "Por favor, llene todos los campos para hacer un pedido.", Toast.LENGTH_LONG).show();
        } else {
            Log.d(DEBUG_TAG, "Nombre de usuario: " + mUserName.getText().toString());
            Log.d(DEBUG_TAG, "Teléfono del usuario: " + mPhoneNumber.getText().toString());
            mUserName.getText().clear();
            mPhoneNumber.getText().clear();

            new AlertDialog.Builder(getContext())
                    .setTitle(getResources().getString(R.string.dialog_extra_title))
                    .setMessage(getResources().getString(R.string.dialog_extra_body))
                    .setIcon(R.drawable.icon_dialog_finish)
                    .setPositiveButton(getResources().getString(R.string.dialog_extra_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }

                    })
                    .setCancelable(false)
                    .show();

        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
