package com.zgas.tesselar.myzuite.Controller.Fragment.UserService;


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
public class MainFragmentService extends Fragment {

    private static final String DEBUG_TAG = "MainFragmentService";

    private final ArrayList<Case> mCaseList = new ArrayList();
    private RecyclerView mRecyclerOrders;
    private OrdersAdapter mOrderAdapter;
    private View mRootView;
    private Case mCase;
    private UserPreferences mUserPreferences;
    private User mUser;

    public MainFragmentService() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_main_service, container, false);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getContext());
        //mUser = mUserPreferences.getUserData();
        //Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getUserData().getUserEmail());
        initUi(mRootView);
        return mRootView;
    }

    private void initUi(View pRootview) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mOrderAdapter = new OrdersAdapter(getActivity(), mCaseList);

        mRecyclerOrders = pRootview.findViewById(R.id.fragment_main_service_recycler_view);
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
            mCase.setCaseType(Case.caseTypes.CUSTOM_SERVICE);
            mCase.setCaseTimeIn("04:40");
            mCase.setCaseTimeSeen(new Time(System.currentTimeMillis()).toString());
            mCase.setCaseTimeArrival("07:30");
            mCase.setCaseTimeProgrammed("06:30");
            mCase.setCasePriority(Case.casePriority.HIGH);
            mCase.setCaseClientName("Oscar");
            mCase.setCaseUserId(1234);
            mCase.setCaseClientLastname("Arvizu");
            mCaseList.add(mCase);
        }
    }

}
