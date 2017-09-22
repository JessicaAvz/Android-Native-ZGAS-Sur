package com.zgas.tesselar.myzuite.Controller.Fragment.UserOperator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Controller.Adapter.OrdersAdapter;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.GetOrdersTask;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentOperator extends Fragment implements GetOrdersTask.OrderTaskListener {

    private static final String DEBUG_TAG = "MainFragmentOperator";

    private RecyclerView mRecyclerOrders;
    private OrdersAdapter mOrderAdapter;
    private View mRootView;
    private UserPreferences mUserPreferences;
    LinearLayoutManager layoutManager;
    private User mUser;

    public MainFragmentOperator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_main_operator, container, false);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getContext());
        mUser = mUserPreferences.getUser();
        Log.d(DEBUG_TAG, "Usuario logeado: " + mUserPreferences.getUser().getUserEmail());
        initUi(mRootView);
        try {
            GetOrdersTask getOrdersTask = new GetOrdersTask(getContext(), null);
            getOrdersTask.setOrderTaskListener(this);
            getOrdersTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mRootView;
    }

    private void initUi(View pRootView) {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerOrders = pRootView.findViewById(R.id.fragment_main_operator_recycler_view);
    }


    @Override
    public void getCasesErrorResponse(String error) {
        Log.d(DEBUG_TAG, error);
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getCasesSuccessResponse(List<Case> caseList) {
        mOrderAdapter = new OrdersAdapter(getContext(), caseList);
        mRecyclerOrders.setHasFixedSize(true);
        mRecyclerOrders.setItemViewCacheSize(20);
        mRecyclerOrders.setDrawingCacheEnabled(true);
        mRecyclerOrders.setLayoutManager(layoutManager);
        mRecyclerOrders.setAdapter(mOrderAdapter);
    }
}
