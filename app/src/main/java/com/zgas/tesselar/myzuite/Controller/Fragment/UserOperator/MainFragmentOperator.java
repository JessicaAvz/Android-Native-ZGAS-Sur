package com.zgas.tesselar.myzuite.Controller.Fragment.UserOperator;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zgas.tesselar.myzuite.Controller.Adapter.OrdersOperatorAdapter;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;

import java.sql.Time;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentOperator extends Fragment {

    private static final String DEBUG_TAG = "MainFragmentOperator";

    private final ArrayList<Case> mCaseList = new ArrayList();
    private RecyclerView mRecyclerOrders;
    private OrdersOperatorAdapter mOrderAdapter;
    private View mRootView;
    private Case mCase;
    private SharedPreferences sharedPreferences;

    public MainFragmentOperator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_main_operator, container, false);
        Log.d(DEBUG_TAG, "OnCreate");
        initUi(mRootView);
        return mRootView;
    }

    private void initUi(View pRootView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mOrderAdapter = new OrdersOperatorAdapter(getActivity(), mCaseList);

        mRecyclerOrders = pRootView.findViewById(R.id.fragment_main_operator_recycler_view);
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
            mCase.setCaseTimeIn(new Time(4, 40, 0));
            mCase.setCaseTimeSeen(new Time(System.currentTimeMillis()));
            mCase.setCaseTimeArrival(new Time(7, 30, 0));
            mCase.setCaseTimeProgrammed(new Time(6, 30, 0));
            mCase.setCasePriority(Case.casePriority.HIGH);
            mCase.setCaseClientName("Oscar");
            mCase.setCaseUserId(1234);
            mCase.setCaseClientLastname("Arvizu");
            mCaseList.add(mCase);
        }
    }
}