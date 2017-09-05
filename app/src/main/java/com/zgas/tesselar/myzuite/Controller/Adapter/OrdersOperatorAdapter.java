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

import com.zgas.tesselar.myzuite.Controller.Activity.UserOperator.DetailActivityOperator;
import com.zgas.tesselar.myzuite.Controller.Activity.MainActivity;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by jarvizu on 29/08/2017.
 */

public class OrdersOperatorAdapter extends RecyclerView.Adapter<OrdersOperatorAdapter.OrderViewHolder> {

    private static final String DEBUG_TAG = "OrdersOperatorAdapter";

    private Context context;
    private ArrayList<Case> mCaseList;

    public OrdersOperatorAdapter(Context context, ArrayList<Case> mCaseList) {
        this.context = context;
        this.mCaseList = mCaseList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_main_fragment_operator_my_orders, parent, false);
        return new OrdersOperatorAdapter.OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrdersOperatorAdapter.OrderViewHolder holder, int position) {
        Case mCase = mCaseList.get(position);
        int orderId = mCase.getCaseId();
        String orderAddress = mCase.getCaseAddress();
        Case.caseStatus orderStatus = mCase.getCaseStatus();

        Time orderHourIn = mCase.getCaseTimeIn();

        TextView id = holder.mOrderId;
        id.setText("Pedido número: " + String.valueOf(orderId));

        TextView address = holder.mOrderAddress;
        address.setText("Dirección: " + orderAddress);

        TextView hourIn = holder.mOrderTimeIn;
        hourIn.setText("Hora del pedido: " + orderHourIn.toString());

        TextView status = holder.mOrderStatus;
        if (orderStatus == Case.caseStatus.CANCELLED) {
            status.setTextColor(context.getResources().getColor(R.color.red));
        } else if (orderStatus == Case.caseStatus.FINISHED) {
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
                    Case mCase = mCaseList.get(requestCode);

                    int id = mCase.getCaseId();
                    String address = mCase.getCaseAddress();
                    Case.caseStatus status = mCase.getCaseStatus();
                    Case.caseTypes type = mCase.getCaseType();
                    Time timeIn = mCase.getCaseTimeIn();
                    Time timeSeen = mCase.getCaseTimeSeen();
                    Time timeArrival = mCase.getCaseTimeArrival();
                    Time timeProgrammed = mCase.getCaseTimeProgrammed();
                    Case.casePriority priority = mCase.getCasePriority();
                    String userName = mCase.getCaseClientName();
                    String userLastname = mCase.getCaseClientLastname();
                    int userId = mCase.getCaseUserId();

                    Log.d(DEBUG_TAG, "OrdersOperatorAdapter itemView listener for adapter position: " + requestCode);

                    Bundle bundle = new Bundle();
                    bundle.putInt(MainActivity.EXTRA_CASE_ID, id);
                    bundle.putString(MainActivity.EXTRA_CASE_ADDRESS, address);
                    bundle.putString(MainActivity.EXTRA_CASE_STATUS, status.toString());
                    bundle.putString(MainActivity.EXTRA_CASE_TYPE, type.toString());
                    bundle.putSerializable(MainActivity.EXTRA_CASE_TIME_IN, timeIn);
                    bundle.putSerializable(MainActivity.EXTRA_CASE_TIME_SEEN, timeSeen);
                    bundle.putSerializable(MainActivity.EXTRA_CASE_TIME_ARRIVAL, timeArrival);
                    bundle.putSerializable(MainActivity.EXTRA_CASE_TIME_PROGRAMMED, timeProgrammed);
                    bundle.putString(MainActivity.EXTRA_CASE_PRIORITY, priority.toString());
                    bundle.putInt(MainActivity.EXTRA_CASE_USER_ID, userId);
                    bundle.putString(MainActivity.EXTRA_CASE_USER_NAME, userName);
                    bundle.putString(MainActivity.EXTRA_CASE_USER_LASTNAME, userLastname);

                    Log.d(DEBUG_TAG, "Id del pedido: " + String.valueOf(id));
                    Log.d(DEBUG_TAG, "Dirección del pedido: " + address);
                    Log.d(DEBUG_TAG, "Estatus del pedido: " + String.valueOf(status));
                    Log.d(DEBUG_TAG, "Tipo de pedido: " + String.valueOf(type));
                    Log.d(DEBUG_TAG, "Hora de pedido: " + String.valueOf(timeIn));
                    Log.d(DEBUG_TAG, "Hora de visualización de pedido: " + String.valueOf(timeSeen));
                    Log.d(DEBUG_TAG, "Hora de llegada del pedido: " + String.valueOf(timeArrival));
                    Log.d(DEBUG_TAG, "Hora programada del pedido: " + String.valueOf(timeProgrammed));
                    Log.d(DEBUG_TAG, "Prioridad del pedido: " + String.valueOf(priority));
                    Log.d(DEBUG_TAG, "Id del cliente: " + String.valueOf(userId));
                    Log.d(DEBUG_TAG, "Nombre del cliente: " + userName);
                    Log.d(DEBUG_TAG, "Apellido del cliente:" + userLastname);

                    Intent intent = new Intent(context, DetailActivityOperator.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
