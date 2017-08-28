package com.zgas.tesselar.myzuite.Controller.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.zgas.tesselar.myzuite.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment implements OnClickListener {

    private static final String DEBUG_TAG = "HelpFragment";

    private Spinner mOptions;
    private View pRootView;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pRootView = inflater.inflate(R.layout.fragment_help, container, false);
        initUi(pRootView);
        return pRootView;
    }

    public void initUi(View pRootView) {
        mOptions = pRootView.findViewById(R.id.fragment_help_sp_options);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.help_prompts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOptions.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

    }
}
