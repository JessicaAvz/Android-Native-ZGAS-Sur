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
import com.zgas.tesselar.myzuite.Service.GetCasesTask;
import com.zgas.tesselar.myzuite.Service.UserPreferences;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentOperator extends Fragment implements GetCasesTask.GetCasesTaskListener {

    private static final String DEBUG_TAG = "MainFragmentOperator";

    private final ArrayList<Case> mCaseList = new ArrayList();
    private RecyclerView mRecyclerOrders;
    private OrdersAdapter mOrderAdapter;
    private View mRootView;
    private UserPreferences mUserPreferences;
    private User mUser;
    private Case aCase;

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
        return mRootView;
    }

    private void initUi(View pRootView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mOrderAdapter = new OrdersAdapter(getActivity(), mCaseList);

        mRecyclerOrders = pRootView.findViewById(R.id.fragment_main_operator_recycler_view);
        mRecyclerOrders.setHasFixedSize(true);
        mRecyclerOrders.setItemViewCacheSize(20);
        mRecyclerOrders.setDrawingCacheEnabled(true);
        mRecyclerOrders.setLayoutManager(layoutManager);
        mRecyclerOrders.setAdapter(mOrderAdapter);

        Case case1 = new Case(1, 2499028, "16:40", "16:45", null, null, Case.caseStatus.INPROGRESS, Case.casePriority.HIGH, "Jessica", "Arvizu", "Leon Tolstoi 4964", Case.caseTypes.ORDER);
        Case case2 = new Case(2, 6671059, "10:20", "11:20", "12:30", null, Case.caseStatus.FINISHED, Case.casePriority.LOW, "Grecia", "Rosas", "Yurdard Kipling 22", Case.caseTypes.ORDER);
        Case case3 = new Case(3, 1815678, "08:00", "8:03", null, null, Case.caseStatus.CANCELLED, Case.casePriority.MEDIUM, "Aejandro", "Monteagudo", "Robles Sur 239", Case.caseTypes.ORDER);
        Case case4 = new Case(4, 3844756, "13:10", "13:11", null, null, Case.caseStatus.CANCELLED, Case.casePriority.HIGH, "Jonathan", "Rodríguez", "Guadalupe 110", Case.caseTypes.ORDER);
        Case case5 = new Case(5, 2499028, "05:23", "06:01", null, null, Case.caseStatus.INPROGRESS, Case.casePriority.HIGH, "Ricardo", "Páramo", "Lopez Cotilla 1000", Case.caseTypes.ORDER);
        Case case6 = new Case(6, 2499028, "06:00", "06:04", null, null, Case.caseStatus.INPROGRESS, Case.casePriority.LOW, "Rosa", "Aguilar", "Hidalgo 403", Case.caseTypes.ORDER);
        Case case7 = new Case(7, 2499028, "15:10", "15:17", null, null, Case.caseStatus.INPROGRESS, Case.casePriority.HIGH, "Dayra", "Bobadilla", "Rafael Sanzio 394", Case.caseTypes.ORDER);
        Case case8 = new Case(8, 2499028, "18:34", "18:37", null, "20:00", Case.caseStatus.FINISHED, Case.casePriority.MEDIUM, "Héctor", "Carrillo", "Mariano Otero 1105", Case.caseTypes.ORDER);
        Case case9 = new Case(9, 2499028, "21:05", "21:22", null, "07:00", Case.caseStatus.INPROGRESS, Case.casePriority.HIGH, "Oscar", "Torres", "Lázaro Cárdenas 2508", Case.caseTypes.ORDER);
        Case case10 = new Case(10, 2499028, "14:20", "14:22", null, "15:00", Case.caseStatus.FINISHED, Case.casePriority.HIGH, "Nora", "Salcido", "Patria 1201", Case.caseTypes.ORDER);
/*
        mCaseList.add(case1);
        mCaseList.add(case2);
        mCaseList.add(case3);
        mCaseList.add(case4);
        mCaseList.add(case5);
        mCaseList.add(case6);
        mCaseList.add(case7);
        mCaseList.add(case8);
        mCaseList.add(case9);
        mCaseList.add(case10);
*/

        try {
            GetCasesTask getCasesTask = new GetCasesTask(getContext(), null);
            getCasesTask.setCasesTaskListener(this);
            getCasesTask.execute();
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
    public void getCasesSuccessResponse(Case cases) {

    }
}
