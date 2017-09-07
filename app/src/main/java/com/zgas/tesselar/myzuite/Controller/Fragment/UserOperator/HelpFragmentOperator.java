package com.zgas.tesselar.myzuite.Controller.Fragment.UserOperator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Controller.Adapter.NothingSelectedSpinnerAdapter;
import com.zgas.tesselar.myzuite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragmentOperator extends Fragment implements OnClickListener {

    private static final String DEBUG_TAG = "HelpFragmentOperator";

    private Spinner mOptions;
    private Button mSendProblem;
    private View mRootView;

    public HelpFragmentOperator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_help_operator, container, false);

        Log.d(DEBUG_TAG, "OnCreate");
        initUi(mRootView);
        return mRootView;
    }

    public void initUi(View pRootView) {
        mOptions = pRootView.findViewById(R.id.fragment_help_operator_sp_options);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.help_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, getContext()));

        mSendProblem = pRootView.findViewById(R.id.fragment_help_operator_btn_send_problem);
        mSendProblem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_help_operator_btn_send_problem:
                Toast.makeText(getContext(), "Prueba de click", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
