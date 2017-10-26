package com.zgas.tesselar.myzuite.View.Fragment.UserOperator;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Controller.GetOrdersTask;
import com.zgas.tesselar.myzuite.Controller.UserPreferences;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.View.Adapter.OrdersAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentOperator extends Fragment implements GetOrdersTask.OrderTaskListener {

    private static final String DEBUG_TAG = "MainFragmentOperator";
    private static final String USER_ID = "Id";
    private static final String ADMIN_TOKEN = "access_token";

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
        mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());

        callAsyncTask();

        initUi(mRootView);
        return mRootView;
    }

    private void initUi(View pRootView) {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerOrders = pRootView.findViewById(R.id.fragment_main_operator_recycler_view);
    }

    private void asyncTask() {
        try {
            JSONObject params = new JSONObject();
            params.put(USER_ID, mUserPreferences.getUserObject().getUserId());
            params.put(ADMIN_TOKEN, mUserPreferences.getAdminToken());
            Log.d(DEBUG_TAG, "Parámetros: " + params.getString(USER_ID) + " " + params.getString(ADMIN_TOKEN));
            GetOrdersTask getOrdersTask = new GetOrdersTask(getContext(), params);
            getOrdersTask.setOrderTaskListener(this);
            getOrdersTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callAsyncTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            asyncTask();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 600000); //execute in every 600000 ms = 10 min
        //timer.schedule(doAsynchronousTask, 0, 5000); //executes in every 5000ms = 5 seconds
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
