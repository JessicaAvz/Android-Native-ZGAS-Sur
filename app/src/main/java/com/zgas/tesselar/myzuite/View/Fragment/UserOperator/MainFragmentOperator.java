package com.zgas.tesselar.myzuite.View.Fragment.UserOperator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.zgas.tesselar.myzuite.Service.GetOrdersTask;
import com.zgas.tesselar.myzuite.Service.UserPreferences;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Controller.Adapter.OrdersAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Jessica Arvizu
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentOperator extends Fragment implements GetOrdersTask.OrderTaskListener {

    private static final String DEBUG_TAG = "MainFragmentOperator";
    private static final String USER_ID = "Id";
    private static final String ADMIN_TOKEN = "access_token";
    private static final int REFRESH_DELAY = 1000;

    private RecyclerView mRecyclerOrders;
    private RecyclerRefreshLayout mRecyclerRefreshLayout;
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
        mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());

        initUi(mRootView);
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(DEBUG_TAG, "onStart");
        Log.d(DEBUG_TAG, "Llamado de asyncTask onStart");
        asyncTask();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.d(DEBUG_TAG, "onResume");
        Log.d(DEBUG_TAG, "Llamado de asyncTask onResume");
        asyncTask();
    }

    private void initUi(View rootview) {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerOrders = rootview.findViewById(R.id.fragment_main_operator_recycler_view);
        mRecyclerRefreshLayout = rootview.findViewById(R.id.fragment_main_operator_refresh_layout);

        mRecyclerRefreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerRefreshLayout.setRefreshing(false);
                        asyncTask();
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    private void asyncTask() {
        try {
            JSONObject params = new JSONObject();
            params.put(USER_ID, mUserPreferences.getUserObject().getUserId());
            params.put(ADMIN_TOKEN, mUserPreferences.getAdminToken());
            Log.d(DEBUG_TAG, "Parámetros: " + "Id de usuario: " + params.getString(USER_ID) + " Token de admin: " + params.getString(ADMIN_TOKEN));
            GetOrdersTask getOrdersTask = new GetOrdersTask(getContext(), params);
            getOrdersTask.setOrderTaskListener(this);
            getOrdersTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getCasesErrorResponse(String error) {
        Log.d(DEBUG_TAG, error);
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getCasesSuccessResponse(List<Order> orderList) {
        mOrderAdapter = new OrdersAdapter(getContext(), (ArrayList<Order>) orderList);
        mRecyclerOrders.setHasFixedSize(true);
        mRecyclerOrders.setItemViewCacheSize(20);
        mRecyclerOrders.setDrawingCacheEnabled(true);
        mRecyclerOrders.setLayoutManager(layoutManager);
        mRecyclerOrders.setAdapter(mOrderAdapter);
    }
}
