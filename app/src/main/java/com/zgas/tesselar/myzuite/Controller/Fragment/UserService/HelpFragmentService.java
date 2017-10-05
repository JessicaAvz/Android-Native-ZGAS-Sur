package com.zgas.tesselar.myzuite.Controller.Fragment.UserService;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragmentService extends Fragment implements View.OnClickListener {

    private static final String DEBUG_TAG = "HelpFragmentService";
    private Spinner mSpinnerOptions;
    private Button mSendProblem;
    private View mRootView;
    private UserPreferences mUserPreferences;
    private User mUser;

    public HelpFragmentService() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_help_service, container, false);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getContext());
        mUser = mUserPreferences.getUserData();
        Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getUserData().getUserEmail());
        initUi(mRootView);
        return mRootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_help_service_btn_send_problem:
                selectOption();
                break;
        }
    }

    private void initUi(View pRootView) {
        mSpinnerOptions = pRootView.findViewById(R.id.fragment_help_service_sp_options);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.help_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, getContext()));

        mSendProblem = pRootView.findViewById(R.id.fragment_help_service_btn_send_problem);
        mSendProblem.setOnClickListener(this);
    }

    private void selectOption() {
        if (mSpinnerOptions.getSelectedItem() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.order_cancel_incorrect), Toast.LENGTH_LONG).show();
        } else {
            Log.d(DEBUG_TAG, mSpinnerOptions.getSelectedItem().toString());
            mSpinnerOptions.setSelection(0);
            new AlertDialog.Builder(getContext())
                    .setTitle(getResources().getString(R.string.dialog_help_order_title))
                    .setMessage(getResources().getString(R.string.dialog_help_order_body))
                    .setIcon(R.drawable.icon_dialog_finish)
                    .setPositiveButton(getResources().getString(R.string.dialog_help_order_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }

                    })
                    .setCancelable(false)
                    .show();
        }
    }
}
