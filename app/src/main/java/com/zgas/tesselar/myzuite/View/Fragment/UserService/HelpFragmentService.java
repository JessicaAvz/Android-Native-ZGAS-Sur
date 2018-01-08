package com.zgas.tesselar.myzuite.View.Fragment.UserService;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.zgas.tesselar.myzuite.Controller.UserPreferences;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.View.Adapter.NothingSelectedSpinnerAdapter;

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
            case R.id.fragment_help_service_btn_send_problem:
                selectOption();
                break;
        }
    }

    private void initUi(View rootview) {
        mSpinnerOptions = rootview.findViewById(R.id.fragment_help_service_sp_options);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.help_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, getContext()));

        mSendProblem = rootview.findViewById(R.id.fragment_help_service_btn_send_problem);
        mSendProblem.setOnClickListener(this);
    }

    private void selectOption() {
        if (mSpinnerOptions.getSelectedItem() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.order_cancel_incorrect), Toast.LENGTH_LONG).show();
        } else {
            Log.d(DEBUG_TAG, mSpinnerOptions.getSelectedItem().toString());
            mSpinnerOptions.setSelection(0);
            new FancyAlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.dialog_help_order_title))
                    .setBackgroundColor(getResources().getColor(R.color.light_green))
                    .setMessage(getResources().getString(R.string.dialog_help_order_body))
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
}
