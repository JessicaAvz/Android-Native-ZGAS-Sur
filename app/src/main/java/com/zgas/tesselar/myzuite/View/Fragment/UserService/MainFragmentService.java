package com.zgas.tesselar.myzuite.View.Fragment.UserService;


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
import com.zgas.tesselar.myzuite.Service.GetServiceTask;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This class shows the list of all the orders of type 'custom service'
 *
 * @author jarvizu
 * @version 2018.0.9
 * @see JSONObject
 * @see User
 * @see Order
 * @see UserPreferences
 * @see android.os.AsyncTask
 * @see GetServiceTask
 * @see RecyclerRefreshLayout
 * @see OrdersAdapter
 */
public class MainFragmentService extends Fragment implements GetServiceTask.ServiceTaskListener {

    private static final String DEBUG_TAG = "MainFragmentService";
    private static final String USER_ID = "Id";
    private static final String ADMIN_TOKEN = "access_token";
    private static final int REFRESH_DELAY = 1000;

    @BindView(R.id.fragment_main_service_recycler_view)
    RecyclerView mRecyclerServices;
    @BindView(R.id.fragment_main_service_refresh_layout)
    RecyclerRefreshLayout mRecyclerRefreshLayout;
    private LinearLayoutManager layoutManager;
    private OrdersAdapter mServiceAdapter;
    private View mRootView;
    private UserPreferences mUserPreferences;
    private User mUser;
    private Unbinder unbinder;

    public MainFragmentService() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_main_service, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
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

    private void asyncTask() {
        try {
            JSONObject params = new JSONObject();
            params.put(USER_ID, mUserPreferences.getUserObject().getUserId());
            params.put(ADMIN_TOKEN, mUserPreferences.getAdminToken());
            Log.d(DEBUG_TAG, "Parámetros: " + "Id de usuario: " + params.getString(USER_ID) + " Token de admin: " + params.getString(ADMIN_TOKEN));
            GetServiceTask getServiceTask = new GetServiceTask(getContext(), params);
            getServiceTask.setServiceTaskListener(this);
            getServiceTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUi(View rootview) {
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

    @Override
    public void getServicesError(String error) {
        Log.d(DEBUG_TAG, error);
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getServicesSuccessResponse(List<Order> serviceList) {
        mServiceAdapter = new OrdersAdapter(getContext(), (ArrayList<Order>) serviceList);
        mRecyclerServices.setHasFixedSize(true);
        mRecyclerServices.setItemViewCacheSize(20);
        mRecyclerServices.setDrawingCacheEnabled(true);
        mRecyclerServices.setLayoutManager(layoutManager);
        mRecyclerServices.setAdapter(mServiceAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
