package com.zgas.tesselar.myzuite.View.Fragment.UserLeakage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zgas.tesselar.myzuite.Controller.GetLeakagesTask;
import com.zgas.tesselar.myzuite.Controller.UserPreferences;
import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.View.Adapter.LeaksAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentLeak extends Fragment implements GetLeakagesTask.LeakagesTaskListener {

    private static final String DEBUG_TAG = "MainFragmentLeak";

    private RecyclerView mRecyclerOrders;
    private LeaksAdapter leaksAdapter;
    private View mRootView;
    private UserPreferences mUserPreferences;
    private User mUser;
    private static final String USER_ID = "Id";
    private static final String ADMIN_TOKEN = "access_token";
    private LinearLayoutManager linearLayoutManager;

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
        mUser = mUserPreferences.getUserObject();
        Log.d(DEBUG_TAG, "Usuario logeado id: " + mUser.getUserId());
        Log.d(DEBUG_TAG, "Usuario logeado nombre: " + mUser.getUserName());
        Log.d(DEBUG_TAG, "Usuario logeado tipo: " + mUser.getUserType());
        try {
            JSONObject params = new JSONObject();
            params.put(USER_ID, mUserPreferences.getUserObject().getUserId());
            params.put(ADMIN_TOKEN, mUserPreferences.getAdminToken());
            Log.d(DEBUG_TAG, "Parámetros: " + params.getString(USER_ID) + " " + params.getString(ADMIN_TOKEN));
            GetLeakagesTask getLeakagesTask = new GetLeakagesTask(getContext(), params);
            getLeakagesTask.setLeakagesTaskListener(this);
            getLeakagesTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initUi(mRootView);
        return mRootView;
    }

    private void initUi(View pRootView) {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerOrders = pRootView.findViewById(R.id.fragment_main_leak_recycler_view);
    }

    @Override
    public void getLeakagesErrorResponse(String error) {
        Log.d(DEBUG_TAG, error);
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void getLeakagesSuccessResponse(List<Leak> leakList) {
        leaksAdapter = new LeaksAdapter(getActivity(), (ArrayList<Leak>) leakList);
        mRecyclerOrders.setHasFixedSize(true);
        mRecyclerOrders.setItemViewCacheSize(20);
        mRecyclerOrders.setDrawingCacheEnabled(true);
        mRecyclerOrders.setLayoutManager(linearLayoutManager);
        mRecyclerOrders.setAdapter(leaksAdapter);
    }
}
