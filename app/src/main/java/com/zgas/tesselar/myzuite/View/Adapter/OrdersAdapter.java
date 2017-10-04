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

import com.zgas.tesselar.myzuite.View.Activity.MainActivity;
import com.zgas.tesselar.myzuite.View.Activity.UserLeakage.DetailActivityLeakage;
import com.zgas.tesselar.myzuite.View.Activity.UserOperator.DetailActivityOperator;
import com.zgas.tesselar.myzuite.View.Activity.UserService.DetailActivityService;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;

import java.util.List;

/**
 * Created by jarvizu on 29/08/2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private static final String DEBUG_TAG = "OrdersAdapter";

    private Context context;
    private List<Case> mCaseList;
    private Intent intent;

    public OrdersAdapter(Context context, List<Case> mCaseList) {
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
        int caseId = mCase.getCaseId();
        String caseAddress = mCase.getCaseAddress();
        Case.caseStatus caseStatus = mCase.getCaseStatus();
        Case.caseTypes caseType = mCase.getCaseType();
        String orderHourIn = mCase.getCaseTimeIn();

        TextView id = holder.mOrderId;
        TextView address = holder.mOrderAddress;
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
        } else if (caseType.equals(Case.caseTypes.CANCELLATION)) {
            Log.d(DEBUG_TAG, String.valueOf(Case.caseTypes.CANCELLATION));
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

    @Override
    public long getItemId(int position) {
        return mCaseList.get(position).getCaseId();
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

                    int id = mCase.getCaseId();
                    String address = mCase.getCaseAddress();
                    Case.caseStatus status = mCase.getCaseStatus();
                    Case.caseTypes type = mCase.getCaseType();
                    String timeIn = mCase.getCaseTimeIn();
                    String timeSeen = mCase.getCaseTimeSeen();
                    String timeArrival = mCase.getCaseTimeArrival();
                    String timeProgrammed = mCase.getCaseTimeProgrammed();
                    Case.casePriority priority = mCase.getCasePriority();
                    String userName = mCase.getCaseClientName();
                    String userLastname = mCase.getCaseClientLastname();
                    int userId = mCase.getCaseUserId();

                    Log.d(DEBUG_TAG, "OrdersAdapter itemView listener for adapter position: " + requestCode);

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

                    intent = new Intent();
                    if (type.equals(Case.caseTypes.ORDER)) {
                        intent = new Intent(context, DetailActivityOperator.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (type.equals(Case.caseTypes.LEAKAGE)) {
                        intent = new Intent(context, DetailActivityLeakage.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (type.equals(Case.caseTypes.CUSTOM_SERVICE)) {
                        intent = new Intent(context, DetailActivityService.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (type.equals(Case.caseTypes.CANCELLATION)) {
                    } else if (type.equals(Case.caseTypes.CUT)) {
                    } else if (type.equals(Case.caseTypes.RECONNECTION)) {
                    }
                }
            });
        }
    }
}
