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

import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.View.Activity.UserLeakage.DetailActivityLeakage;
import com.zgas.tesselar.myzuite.View.Activity.UserOperator.DetailActivityOperator;
import com.zgas.tesselar.myzuite.View.Activity.UserService.DetailActivityService;

import java.util.ArrayList;

/**
 * Created by jarvizu on 29/08/2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private static final String DEBUG_TAG = "OrdersAdapter";

    private Context context;
    private ArrayList<Case> mCaseList;
    private Intent intent;

    public OrdersAdapter(Context context, ArrayList<Case> mCaseList) {
        this.context = context;
        this.mCaseList = mCaseList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_main_fragment_operator_my_orders, parent, false);
        return new OrdersAdapter.OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.OrderViewHolder holder, int position) {
        Case mCase = mCaseList.get(position);
        String caseId = mCase.getCaseId();
        String caseAddress = mCase.getCaseAddress();
        Case.caseStatus caseStatus = mCase.getCaseStatus();
        Log.d(DEBUG_TAG, "OnBindViewHolder " + caseStatus);
        Case.caseTypes caseType = mCase.getCaseType();
        String orderHourIn = mCase.getCaseTimeAssignment();

        TextView id = holder.mOrderId;
        id.setText(caseId);
        TextView address = holder.mOrderAddress;
        address.setText(caseAddress);
        TextView hourIn = holder.mOrderTimeIn;
        TextView type = holder.mOrderType;

        if (caseType.equals(Case.caseTypes.LEAKAGE)) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseTypes.LEAKAGE));
            id.setText("Reporte número: " + String.valueOf(caseId));
            address.setText("Dirección: " + caseAddress);
            type.setText("Tipo: " + caseType);
            if (orderHourIn != null && !orderHourIn.equals("")) {
                hourIn.setText("Hora del reporte: " + context.getResources().getString(R.string.no_data));
            } else {
                hourIn.setText("Hora del reporte: " + orderHourIn);
            }
        } else if (caseType.equals(Case.caseTypes.ORDER)) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseTypes.ORDER));
            id.setText("Pedido número: " + String.valueOf(caseId));
            address.setText("Dirección: " + caseAddress);
            type.setText("Tipo: " + caseType);
            if (orderHourIn != null && !orderHourIn.equals("")) {
                hourIn.setText("Hora del pedido: " + context.getResources().getString(R.string.no_data));
            } else {
                hourIn.setText("Hora del pedido: " + orderHourIn);
            }
        } else if (caseType.equals(Case.caseTypes.CUSTOM_SERVICE)) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseTypes.ORDER));
            id.setText("Servicio número: " + String.valueOf(caseId));
            address.setText("Dirección: " + caseAddress);
            type.setText("Tipo: " + caseType);
            if (orderHourIn != null && !orderHourIn.equals("")) {
                hourIn.setText("Hora del servicio: " + context.getResources().getString(R.string.no_data));
            } else {
                hourIn.setText("Hora del servicio: " + orderHourIn);
            }
        } else if (caseType.equals(Case.caseTypes.CUT)) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseTypes.CUT));
        } else if (caseType.equals(Case.caseTypes.RECONNECTION)) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseTypes.RECONNECTION));
        }

        TextView status = holder.mOrderStatus;
        if (caseStatus == Case.caseStatus.CANCELLED) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseStatus.CANCELLED));
            status.setTextColor(context.getResources().getColor(R.color.red));
        } else if (caseStatus == Case.caseStatus.FINISHED) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseStatus.FINISHED));
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (caseStatus == Case.caseStatus.INPROGRESS) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseStatus.INPROGRESS));
            status.setTextColor(context.getResources().getColor(R.color.amber));
        } else if (caseStatus == Case.caseStatus.NEW) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseStatus.NEW));
            status.setTextColor(context.getResources().getColor(R.color.sky_blue));
        }
        status.setText(caseStatus.toString());
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
                    Case mCase = mCaseList.get(requestCode);

                    String id = mCase.getCaseId();
                    String address = mCase.getCaseAddress();
                    String status = mCase.getCaseStatus().toString();
                    String type = mCase.getCaseType().toString();
                    String timeAssignment = mCase.getCaseTimeAssignment();
                    String timeSeen = mCase.getCaseTimeSeen();
                    String timeArrival = mCase.getCaseTimeArrival();
                    String timeScheduled = mCase.getCaseTimeScheduled();
                    String priority = mCase.getCasePriority().toString();
                    String userName = mCase.getCaseClientName();

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
                    bundle.putString(ExtrasHelper.EXTRA_JSON_OBJECT_ID, id);
                    bundle.putString(ExtrasHelper.EXTRA_JSON_OBJECT_ADDRESS, address);
                    bundle.putString(ExtrasHelper.EXTRA_JSON_OBJECT_STATUS, status);
                    bundle.putString(ExtrasHelper.EXTRA_JSON_OBJECT_TYPE, type);
                    bundle.putSerializable(ExtrasHelper.EXTRA_JSON_OBJECT_TIME_ASSIGNMENT, timeAssignment);
                    bundle.putSerializable(ExtrasHelper.EXTRA_JSON_OBJECT_TIME_SEEN, timeSeen);
                    bundle.putSerializable(ExtrasHelper.EXTRA_JSON_OBJECT_TIME_ARRIVAL, timeArrival);
                    bundle.putSerializable(ExtrasHelper.EXTRA_JSON_OBJECT_TIME_SCHEDULED, timeScheduled);
                    bundle.putString(ExtrasHelper.EXTRA_JSON_OBJECT_PRIORITY, priority);
                    bundle.putString(ExtrasHelper.EXTRA_JSON_OBJECT_USER_NAME, userName);

                    Log.d(DEBUG_TAG, "Bundle - Id del caso: " + bundle.getString(ExtrasHelper.EXTRA_JSON_OBJECT_ID));
                    Log.d(DEBUG_TAG, "Bundle - Dirección del caso: " + bundle.getString(ExtrasHelper.EXTRA_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Bundle - Estatus del caso: " + bundle.getString(ExtrasHelper.EXTRA_JSON_OBJECT_STATUS));
                    Log.d(DEBUG_TAG, "Bundle - Tipo de caso: " + bundle.getString(ExtrasHelper.EXTRA_JSON_OBJECT_TYPE));
                    Log.d(DEBUG_TAG, "Bundle - Hora de caso: " + bundle.getString(ExtrasHelper.EXTRA_JSON_OBJECT_TIME_ASSIGNMENT));
                    //Log.d(DEBUG_TAG, "Bundle - Hora de visualización de caso: " + String.valueOf(timeSeen));
                    //Log.d(DEBUG_TAG, "Bundle - Hora de llegada del caso: " + String.valueOf(timeArrival));
                    Log.d(DEBUG_TAG, "Bundle - Hora programada del caso: " + bundle.getString(ExtrasHelper.EXTRA_JSON_OBJECT_TIME_SCHEDULED));
                    Log.d(DEBUG_TAG, "Bundle - Prioridad del caso: " + bundle.getString(ExtrasHelper.EXTRA_JSON_OBJECT_PRIORITY));
                    Log.d(DEBUG_TAG, "Bundle - Nombre del cliente: " + bundle.getString(ExtrasHelper.EXTRA_JSON_OBJECT_CLIENT_NAME));

                    intent = new Intent();
                    if (type.equals(Case.caseTypes.ORDER.toString())) {
                        intent = new Intent(context, DetailActivityOperator.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (type.equals(Case.caseTypes.LEAKAGE.toString())) {
                        intent = new Intent(context, DetailActivityLeakage.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (type.equals(Case.caseTypes.CUSTOM_SERVICE.toString())) {
                        intent = new Intent(context, DetailActivityService.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (type.equals(Case.caseTypes.CUT.toString())) {
                    } else if (type.equals(Case.caseTypes.RECONNECTION.toString())) {
                    }
                }
            });
        }
    }
}
