package com.zgas.tesselar.myzuite.View.Fragment.UserOperator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;

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

    public void makeOrder() {
        if (isEmpty(mUserName) || isEmpty(mPhoneNumber)) {
            Toast.makeText(getContext(), getResources().getString(R.string.order_new_incorrect), Toast.LENGTH_LONG).show();
        } else {
            Log.d(DEBUG_TAG, "Nombre de usuario: " + mUserName.getText().toString());
            Log.d(DEBUG_TAG, "Teléfono del usuario: " + mPhoneNumber.getText().toString());
            mUserName.getText().clear();
            mPhoneNumber.getText().clear();

            new FancyAlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.order_finish_report))
                    .setBackgroundColor(getResources().getColor(R.color.light_green))
                    .setMessage(getResources().getString(R.string.order_finish_report_correct))
                    .setNegativeBtnText(getResources().getString(R.string.cancel))
                    .setPositiveBtnBackground(getResources().getColor(R.color.light_green))
                    .setPositiveBtnText(getResources().getString(R.string.dialog_in_progress_accept))
                    .setNegativeBtnBackground(getResources().getColor(R.color.grey_300))
                    .setAnimation(Animation.SIDE)
                    .isCancellable(false)
                    .setIcon(R.drawable.icon_check_circle, Icon.Visible)
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
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
