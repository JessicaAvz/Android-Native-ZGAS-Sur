package com.zgas.tesselar.myzuite.Controller.Fragment.UserLeakage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragmentLeak extends Fragment implements View.OnClickListener {

    private static final String DEBUG_TAG = "HelpFragmentLeak";
    private Spinner mOptions;
    private Button mSendProblem;
    private View mRootView;

    public HelpFragmentLeak() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_help_leak, container, false);
        Log.d(DEBUG_TAG, "OnCreate");
        initUi(mRootView);
        return mRootView;
    }

    private void initUi(View pRootView) {
        mOptions = pRootView.findViewById(R.id.fragment_help_leakage_sp_options);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.help_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, getContext()));

        mSendProblem = pRootView.findViewById(R.id.fragment_help_leakage_btn_send_problem);
        mSendProblem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_help_leakage_btn_send_problem:
                break;
        }
    }

}