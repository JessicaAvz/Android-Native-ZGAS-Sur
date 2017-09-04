package com.zgas.tesselar.myzuite.Controller.Fragment.UserOperator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragmentOperator extends Fragment implements View.OnClickListener {

    private static final String DEBUG_TAG = "OrderFragmentOperator";
    private View mRootView;
    private EditText mEmail;
    private EditText mPhoneNumber;
    private Button mMakeOrder;

    public OrderFragmentOperator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_order_operator, container, false);
        Log.d(DEBUG_TAG, "OnCreate");
        initUi(mRootView);
        return mRootView;
    }

    private void initUi(View pRootView) {
        mEmail = pRootView.findViewById(R.id.fragment_order_operator_et_username);
        mPhoneNumber = pRootView.findViewById(R.id.fragment_order_operator_et_phone);
        mMakeOrder = pRootView.findViewById(R.id.fragment_order_operator_btn_make_order);
        mMakeOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_order_operator_btn_make_order:
                Toast.makeText(getContext(), "Si funciona el onclick", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
