package com.zgas.tesselar.myzuite.View.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.View.Activity.UserOperator.DetailActivityOperator;

import java.util.ArrayList;

/**
 * Created by jarvizu on 29/08/2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private static final String DEBUG_TAG = "OrdersAdapter";

    private Context context;
    private ArrayList<Order> mOrderList;
    private Intent intent;

    public OrdersAdapter(Context context, ArrayList<Order> mOrderList) {
        this.context = context;
        this.mOrderList = mOrderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_main_fragment_operator_my_orders, parent, false);
        return new OrdersAdapter.OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.OrderViewHolder holder, int position) {
        Order mOrder = mOrderList.get(position);
        String caseId = mOrder.getCaseId();
        String caseAddress = mOrder.getCaseAddress();
        Order.caseStatus caseStatus = mOrder.getCaseStatus();
        Order.caseTypes caseType = mOrder.getCaseType();
        String orderHourIn = mOrder.getCaseTimeAssignment();
        String serviceType = mOrder.getCaseServiceType();

        TextView id = holder.mOrderId;
        id.setText(caseId);
        TextView address = holder.mOrderAddress;
        address.setText(caseAddress);
        TextView hourIn = holder.mOrderTimeIn;
        TextView type = holder.mOrderType;

        Log.d(DEBUG_TAG, String.valueOf(Order.caseTypes.ORDER));
        id.setText("Pedido número: " + String.valueOf(caseId));
        address.setText("Dirección: " + caseAddress);
        type.setText("Tipo: " + caseType + " - " + serviceType);

        if (orderHourIn == null || orderHourIn.equals("")) {
            hourIn.setText("Hora del pedido: " + context.getResources().getString(R.string.no_data));
        } else {
            hourIn.setText("Hora del pedido: " + orderHourIn);
        }

        TextView status = holder.mOrderStatus;
        if (caseStatus == Order.caseStatus.CANCELLED) {
            Log.d(DEBUG_TAG, String.valueOf(Order.caseStatus.CANCELLED));
            status.setTextColor(context.getResources().getColor(R.color.red));
        } else if (caseStatus == Order.caseStatus.FINISHED) {
            Log.d(DEBUG_TAG, String.valueOf(Order.caseStatus.FINISHED));
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (caseStatus == Order.caseStatus.INPROGRESS) {
            Log.d(DEBUG_TAG, String.valueOf(Order.caseStatus.INPROGRESS));
            status.setTextColor(context.getResources().getColor(R.color.amber));
        } else {
            status.setTextColor(context.getResources().getColor(R.color.orange));
        }
        status.setText(caseStatus.toString());
        holder.itemView.setTag(mOrderList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mOrderList.isEmpty()) {
            return 0;
        } else {
            return mOrderList.size();
        }
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView mOrderId;
        private TextView mOrderStatus;
        private TextView mOrderAddress;
        private TextView mOrderTimeIn;
        private TextView mOrderType;

        public OrderViewHolder(final View itemView) {
            super(itemView);

            mOrderId = itemView.findViewById(R.id.row_main_fragment_tv_order_id);
            mOrderStatus = itemView.findViewById(R.id.row_main_fragment_tv_order_status);
            mOrderAddress = itemView.findViewById(R.id.row_main_fragment_tv_order_address);
            mOrderTimeIn = itemView.findViewById(R.id.row_main_fragment_tv_order_in);
            mOrderType = itemView.findViewById(R.id.row_main_fragment_tv_order_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                    Order mOrder = mOrderList.get(requestCode);

                    String id = mOrder.getCaseId();
                    String address = mOrder.getCaseAddress();
                    String status = mOrder.getCaseStatus().toString();
                    String type = mOrder.getCaseType().toString();
                    String timeAssignment = mOrder.getCaseTimeAssignment();
                    String timeSeen = mOrder.getCaseTimeSeen();
                    String timeArrival = mOrder.getCaseTimeArrival();
                    String timeScheduled = mOrder.getCaseTimeScheduled();
                    String priority = mOrder.getCasePriority().toString();
                    String userName = mOrder.getCaseAccountName();

                    Log.d(DEBUG_TAG, "OrdersAdapter itemView listener for adapter position: " + requestCode);
                    Log.d(DEBUG_TAG, "Id del caso: " + id);
                    Log.d(DEBUG_TAG, "Dirección del caso: " + address);
                    Log.d(DEBUG_TAG, "Status de caso: " + status);
                    Log.d(DEBUG_TAG, "Tipo de caso: " + type.toString());
                    Log.d(DEBUG_TAG, "Hora de asignación: " + timeAssignment);
                    //Log.d(DEBUG_TAG, "Visto : " + timeSeen);
                    //Log.d(DEBUG_TAG, "Hora de llegada: " + timeArrival);
                    Log.d(DEBUG_TAG, "Hora programada: " + timeScheduled);
                    Log.d(DEBUG_TAG, "Prioridad del caso: " + priority);
                    Log.d(DEBUG_TAG, "Nombre del cliente: " + userName);

                    Bundle bundle = new Bundle();
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ID, id);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS, address);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, status);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_TYPE, type);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT, timeAssignment);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN, timeSeen);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL, timeArrival);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED, timeScheduled);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY, priority);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME, userName);

                    Log.d(DEBUG_TAG, "Bundle - Id del caso: " + bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ID));
                    Log.d(DEBUG_TAG, "Bundle - Dirección del caso: " + bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Bundle - Estatus del caso: " + bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS));
                    Log.d(DEBUG_TAG, "Bundle - Tipo de caso: " + bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TYPE));
                    Log.d(DEBUG_TAG, "Bundle - Hora de caso: " + bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    Log.d(DEBUG_TAG, "Bundle - Hora de visualización de caso: " + String.valueOf(timeSeen));
                    Log.d(DEBUG_TAG, "Bundle - Hora de llegada del caso: " + String.valueOf(timeArrival));
                    Log.d(DEBUG_TAG, "Bundle - Hora programada del caso: " + bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED));
                    Log.d(DEBUG_TAG, "Bundle - Prioridad del caso: " + bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY));
                    Log.d(DEBUG_TAG, "Bundle - Nombre del cliente: " + bundle.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME));

                    intent = new Intent();
                    if (type.equals(Order.caseTypes.ORDER.toString())) {
                        intent = new Intent(context, DetailActivityOperator.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else {

                    }
                }
            });
        }
    }
}

