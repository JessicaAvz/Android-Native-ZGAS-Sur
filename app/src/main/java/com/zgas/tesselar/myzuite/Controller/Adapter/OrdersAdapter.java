package com.zgas.tesselar.myzuite.Controller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Controller.Activity.DetailActivity;
import com.zgas.tesselar.myzuite.Controller.Activity.MainActivity;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jarvizu on 29/08/2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private static final String DEBUG_TAG = "OrdersAdapter";

    private Context context;
    private ArrayList<Case> mCaseList;

    public OrdersAdapter(Context context, ArrayList<Case> mCaseList) {
        this.context = context;
        this.mCaseList = mCaseList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_main_fragment_my_orders, parent, false);
        return new OrdersAdapter.OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.OrderViewHolder holder, int position) {
        Case mCase = mCaseList.get(position);
        int orderId = mCase.getCaseId();
        String orderAddress = mCase.getCaseAddress();
        Case.caseStatus orderStatus = mCase.getCaseStatus();

        Calendar orderHourIn = mCase.getCaseTimeIn();

        TextView id = holder.mOrderId;
        id.setText("Pedido número: " + String.valueOf(orderId));

        TextView address = holder.mOrderAddress;
        address.setText("Dirección: " + orderAddress);

        TextView hourIn = holder.mOrderTimeIn;
        //hourIn.setText(orderType.toString());

        TextView status = holder.mOrderStatus;
        if (orderStatus == Case.caseStatus.CANCELLED) {
            status.setTextColor(context.getResources().getColor(R.color.red));
        } else if (orderStatus == Case.caseStatus.FINALIZED) {
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (orderStatus == Case.caseStatus.INPROGRESS) {
            status.setTextColor(context.getResources().getColor(R.color.amber));
        }
        status.setText(orderStatus.toString());

        holder.itemView.setTag(mCaseList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mCaseList.isEmpty()) {
            return 0;
        } else {
            return mCaseList.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return mCaseList.get(position).getCaseId();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView mOrderId;
        private TextView mOrderStatus;
        private TextView mOrderAddress;
        private TextView mOrderTimeIn;

        public OrderViewHolder(final View itemView) {
            super(itemView);

            mOrderId = itemView.findViewById(R.id.row_main_fragment_tv_order_id);
            mOrderStatus = itemView.findViewById(R.id.row_main_fragment_tv_order_status);
            mOrderAddress = itemView.findViewById(R.id.row_main_fragment_tv_order_address);
            mOrderTimeIn = itemView.findViewById(R.id.row_main_fragment_tv_order_in);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                    int id = mCaseList.get(requestCode).getCaseId();
                    String address = mCaseList.get(requestCode).getCaseAddress();
                    Case.caseStatus status = mCaseList.get(requestCode).getCaseStatus();
                    Case.caseTypes type = mCaseList.get(requestCode).getCaseType();
                    Calendar timeIn = mCaseList.get(requestCode).getCaseTimeIn();
                    Calendar timeSeen = mCaseList.get(requestCode).getCaseTimeSeen();
                    Calendar timeArrival = mCaseList.get(requestCode).getCaseTimeArrival();
                    String priority = mCaseList.get(requestCode).getCasePriority();
                    String userName = mCaseList.get(requestCode).getCaseUserName();
                    String userLastname = mCaseList.get(requestCode).getCaseUserLastName();
                    int userId = mCaseList.get(requestCode).getCaseUserId();

                    Log.d(DEBUG_TAG, "OrdersAdapter itemView listener for adapter position: " + requestCode);

                    Bundle bundle = new Bundle();
                    bundle.putInt(MainActivity.EXTRA_CASE_ID, id);
                    bundle.putString(MainActivity.EXTRA_CASE_ADDRESS, address);
                    bundle.putString(MainActivity.EXTRA_CASE_STATUS, status.toString());
                    bundle.putString(MainActivity.EXTRA_CASE_TYPE, type.toString());
                    bundle.putSerializable(MainActivity.EXTRA_CASE_TIME_IN, timeIn);
                    bundle.putSerializable(MainActivity.EXTRA_CASE_TIME_SEEN, timeSeen);
                    bundle.putSerializable(MainActivity.EXTRA_CASE_TIME_ARRIVAL, timeArrival);
                    bundle.putString(MainActivity.EXTRA_CASE_PRIORITY, priority);
                    bundle.putInt(MainActivity.EXTRA_CASE_USER_ID, userId);
                    bundle.putString(MainActivity.EXTRA_CASE_USER_NAME, userName);
                    bundle.putString(MainActivity.EXTRA_CASE_USER_LASTNAME, userLastname);

                    Log.d(DEBUG_TAG, String.valueOf(id));
                    Log.d(DEBUG_TAG, address);
                    Log.d(DEBUG_TAG, String.valueOf(status));
                    Log.d(DEBUG_TAG, String.valueOf(type));
                    Log.d(DEBUG_TAG, String.valueOf(timeIn));
                    Log.d(DEBUG_TAG, String.valueOf(timeSeen));
                    Log.d(DEBUG_TAG, String.valueOf(timeArrival));
                    Log.d(DEBUG_TAG, priority);
                    Log.d(DEBUG_TAG, String.valueOf(userId));
                    Log.d(DEBUG_TAG, userName);
                    Log.d(DEBUG_TAG, userLastname);

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
