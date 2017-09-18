package com.zgas.tesselar.myzuite.Controller.Fragment.UserLeakage;


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
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

import java.sql.Time;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentLeak extends Fragment {

    private static final String DEBUG_TAG = "MainFragmentLeak";

    private final ArrayList<Case> mCaseList = new ArrayList();
    private RecyclerView mRecyclerOrders;
    private OrdersAdapter mOrderAdapter;
    private View mRootView;
    private Case mCase;
    private UserPreferences mUserPreferences;
    private User mUser;

    public MainFragmentLeak() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_main_leak, container, false);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getContext());
        mUser = mUserPreferences.getUser();
        Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getUser().getUserEmail());
        initUi(mRootView);
        return mRootView;
    }

    private void initUi(View pRootView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mOrderAdapter = new OrdersAdapter(getActivity(), mCaseList);

        mRecyclerOrders = pRootView.findViewById(R.id.fragment_main_leak_recycler_view);
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
            mCase.setCaseType(Case.caseTypes.LEAKAGE);
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
