package com.zgas.tesselar.myzuite.Controller.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zgas.tesselar.myzuite.Controller.Adapter.OrdersAdapter;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String DEBUG_TAG = "MainFragment";

    private final ArrayList<Case> mCaseList = new ArrayList();

    private UserPreferences userPreferences = new UserPreferences();
    private RecyclerView mRecyclerOrders;
    private OrdersAdapter mOrderAdapter;
    private View mRootView;
    private Case mCase;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        Log.d(DEBUG_TAG, "OnCreate");
        userPreferences.getUserID();
        initUi(mRootView);
        return mRootView;
    }

    private void initUi(View pRootView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mOrderAdapter = new OrdersAdapter(getActivity(), mCaseList);

        mRecyclerOrders = pRootView.findViewById(R.id.fragment_main_recycler_view);
        mRecyclerOrders.setHasFixedSize(true);
        mRecyclerOrders.setItemViewCacheSize(20);
        mRecyclerOrders.setDrawingCacheEnabled(true);
        mRecyclerOrders.setLayoutManager(layoutManager);
        mRecyclerOrders.setAdapter(mOrderAdapter);

        for (int x = 0; x < 15; x++) {
            mCase = new Case();
            mCase.setCaseId(x);
            mCase.setCaseAddress("Av. Patria #123");
            mCase.setCaseStatus(Case.caseStatus.INPROGRESS);
            mCase.setCaseType(Case.caseTypes.ORDER);
            mCase.setCaseTimeIn(null);
            mCase.setCaseTimeSeen(null);
            mCase.setCaseTimeArrival(null);
            mCase.setCaseTimeProgrammed(null);
            mCase.setCasePriority(Case.casePriority.HIGH);
            mCase.setCaseUserName("Jessiquita");
            mCase.setCaseUserId(1234);
            mCase.setCaseUserLastName("Arvizitu");
            mCaseList.add(mCase);
        }
    }
}
