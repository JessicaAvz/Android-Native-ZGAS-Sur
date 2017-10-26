package com.zgas.tesselar.myzuite.View.Fragment.UserService;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.View.Adapter.OrdersAdapter;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Controller.UserPreferences;

import java.sql.Time;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentService extends Fragment {

    private static final String DEBUG_TAG = "MainFragmentService";

    private final ArrayList<Order> mOrderList = new ArrayList();
    private RecyclerView mRecyclerOrders;
    private OrdersAdapter mOrderAdapter;
    private View mRootView;
    private Order mOrder;
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
        mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());
        initUi(mRootView);
        return mRootView;
    }

    private void initUi(View pRootview) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mOrderAdapter = new OrdersAdapter(getActivity(), mOrderList);

        mRecyclerOrders = pRootview.findViewById(R.id.fragment_main_service_recycler_view);
        mRecyclerOrders.setHasFixedSize(true);
        mRecyclerOrders.setItemViewCacheSize(20);
        mRecyclerOrders.setDrawingCacheEnabled(true);
        mRecyclerOrders.setLayoutManager(layoutManager);
        mRecyclerOrders.setAdapter(mOrderAdapter);

        for (int x = 0; x < 15; x++) {
            mOrder = new Order();
            mOrder.setCaseId(String.valueOf(x));
            mOrder.setCaseAddress("Av. Patria #123");
            mOrder.setCaseStatus(Order.caseStatus.INPROGRESS);
            mOrder.setCaseType(Order.caseTypes.CUSTOM_SERVICE);
            mOrder.setCaseTimeAssignment("04:40");
            mOrder.setCaseTimeSeen(new Time(System.currentTimeMillis()).toString());
            mOrder.setCaseTimeArrival("07:30");
            mOrder.setCaseTimeScheduled("06:30");
            mOrder.setCasePriority(Order.casePriority.HIGH);
            mOrder.setCaseAccountName("Oscar");
            mOrder.setCaseUserId(String.valueOf(1234));
            mOrderList.add(mOrder);
        }
    }

}
