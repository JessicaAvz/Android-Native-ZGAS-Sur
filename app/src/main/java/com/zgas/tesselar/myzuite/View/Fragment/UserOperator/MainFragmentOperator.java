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
import com.zgas.tesselar.myzuite.Controller.Adapter.OrdersAdapter;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.GetOrdersTask;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This class shows the list of all the orders of type 'order'
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see Order
 * @see UserPreferences
 * @see android.os.AsyncTask
 * @see GetOrdersTask
 * @see RecyclerRefreshLayout
 * @see OrdersAdapter
 * @see ButterKnife
 */
public class MainFragmentOperator extends Fragment implements GetOrdersTask.OrderTaskListener {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String USER_ID = "Id";
    private static final String ADMIN_TOKEN = "access_token";
    private static final int REFRESH_DELAY = 1000;

    @BindView(R.id.fragment_main_operator_recycler_view)
    RecyclerView mRecyclerOrders;
    @BindView(R.id.fragment_main_operator_refresh_layout)
    RecyclerRefreshLayout mRecyclerRefreshLayout;
    private UserPreferences mUserPreferences;
    LinearLayoutManager layoutManager;
    private Unbinder unbinder;

    public MainFragmentOperator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_main_operator, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        Log.d(DEBUG_TAG, getResources().getString(R.string.on_create));
        mUserPreferences = new UserPreferences(getContext());
        initUi();
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        asyncTask();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        asyncTask();
    }

    private void initUi() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
        OrdersAdapter mOrderAdapter = new OrdersAdapter(getContext(), (ArrayList<Order>) orderList);
        mRecyclerOrders.setHasFixedSize(true);
        mRecyclerOrders.setItemViewCacheSize(20);
        mRecyclerOrders.setDrawingCacheEnabled(true);
        mRecyclerOrders.setLayoutManager(layoutManager);
        mRecyclerOrders.setAdapter(mOrderAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
