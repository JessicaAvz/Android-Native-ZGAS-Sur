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

import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.View.Activity.UserLeakage.DetailActivityLeakage;
import com.zgas.tesselar.myzuite.View.Activity.UserOperator.DetailActivityOperator;
import com.zgas.tesselar.myzuite.View.Activity.UserService.DetailActivityService;

import java.util.ArrayList;

/**
 * Created by jarvizu on 24/10/2017.
 */

public class LeaksAdapter extends RecyclerView.Adapter<LeaksAdapter.LeaksViewHolder> {

    private static final String DEBUG_TAG = "LeaksAdapter";

    private Context context;
    private ArrayList<Leak> mLeaksList;
    private Intent intent;

    public LeaksAdapter(Context context, ArrayList<Leak> mLeaksList) {
        this.context = context;
        this.mLeaksList = mLeaksList;
    }

    @Override
    public LeaksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_main_fragment_operator_my_orders, parent, false);
        return new LeaksAdapter.LeaksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LeaksAdapter.LeaksViewHolder holder, int position) {
        Leak mLeak = mLeaksList.get(position);
        String leakId = mLeak.getLeakId();
        String leakAddress = mLeak.getLeakAddress();
        Leak.leakStatus leakStatus = mLeak.getLeakStatus();
        Leak.leakType leakType = mLeak.getLeakType();
        String leakHourIn = mLeak.getLeakTimeAssignment();
        String serviceType = mLeak.getLeakServiceType();

        TextView id = holder.mLeakId;
        id.setText(leakId);
        TextView address = holder.mLeakAddress;
        address.setText(leakAddress);
        TextView hourIn = holder.mLeakTimeIn;
        TextView type = holder.mLeakType;

        if (leakType.equals(Leak.leakType.LEAKAGE)) {
            Log.d(DEBUG_TAG, String.valueOf(Leak.leakType.LEAKAGE));
            id.setText("Reporte número: " + String.valueOf(leakId));
            address.setText("Dirección: " + leakAddress);
            type.setText("Tipo: " + leakType);
            if (leakHourIn == null || leakHourIn.equals("")) {
                hourIn.setText("Hora del reporte: " + context.getResources().getString(R.string.no_data));
            } else {
                hourIn.setText("Hora del reporte: " + leakHourIn);
            }
        } else if (leakType.equals(Leak.leakType.ORDER)) {
            Log.d(DEBUG_TAG, String.valueOf(Leak.leakType.ORDER));
            id.setText("Pedido número: " + String.valueOf(leakId));
            address.setText("Dirección: " + leakAddress);
            type.setText("Tipo: " + leakType + " - " + serviceType);
            if (leakHourIn == null || leakHourIn.equals("")) {
                hourIn.setText("Hora del pedido: " + context.getResources().getString(R.string.no_data));
            } else {
                hourIn.setText("Hora del pedido: " + leakHourIn);
            }
        } else if (leakType.equals(Leak.leakType.CUSTOM_SERVICE)) {
            Log.d(DEBUG_TAG, String.valueOf(Leak.leakType.CUSTOM_SERVICE));
            id.setText("Servicio número: " + String.valueOf(leakId));
            address.setText("Dirección: " + leakAddress);
            type.setText("Tipo: " + leakType);
            if (leakHourIn == null || leakHourIn.equals("")) {
                hourIn.setText("Hora del servicio: " + context.getResources().getString(R.string.no_data));
            } else {
                hourIn.setText("Hora del servicio: " + leakHourIn);
            }
        } else if (leakType.equals(Leak.leakType.CUT)) {
            Log.d(DEBUG_TAG, String.valueOf(Leak.leakType.CUT));
        } else if (leakType.equals(Leak.leakType.RECONNECTION)) {
            Log.d(DEBUG_TAG, String.valueOf(Leak.leakType.RECONNECTION));
        }

        TextView status = holder.mLeakStatus;
        if (leakStatus == Leak.leakStatus.CANCELLED) {
            Log.d(DEBUG_TAG, String.valueOf(Leak.leakStatus.CANCELLED));
            status.setTextColor(context.getResources().getColor(R.color.red));
        } else if (leakStatus == Leak.leakStatus.FINISHED) {
            Log.d(DEBUG_TAG, String.valueOf(Leak.leakStatus.FINISHED));
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (leakStatus == Leak.leakStatus.INPROGRESS) {
            Log.d(DEBUG_TAG, String.valueOf(Leak.leakStatus.INPROGRESS));
            status.setTextColor(context.getResources().getColor(R.color.amber));
        } else {
            status.setTextColor(context.getResources().getColor(R.color.orange));
        }

        status.setText(leakStatus.toString());
        holder.itemView.setTag(mLeaksList.get(position));


    }

    @Override
    public int getItemCount() {
        if (mLeaksList.isEmpty()) {
            return 0;
        } else {
            return mLeaksList.size();
        }
    }

    public class LeaksViewHolder extends RecyclerView.ViewHolder {

        private TextView mLeakId;
        private TextView mLeakStatus;
        private TextView mLeakAddress;
        private TextView mLeakTimeIn;
        private TextView mLeakType;

        public LeaksViewHolder(View itemView) {
            super(itemView);

            mLeakId = itemView.findViewById(R.id.row_main_fragment_tv_order_id);
            mLeakStatus = itemView.findViewById(R.id.row_main_fragment_tv_order_status);
            mLeakAddress = itemView.findViewById(R.id.row_main_fragment_tv_order_address);
            mLeakTimeIn = itemView.findViewById(R.id.row_main_fragment_tv_order_in);
            mLeakType = itemView.findViewById(R.id.row_main_fragment_tv_order_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                    Leak mLeak = mLeaksList.get(requestCode);

                    String id = mLeak.getLeakId();
                    String timeAssignment = mLeak.getLeakTimeAssignment();
                    String timeDeparture = mLeak.getLeakTimeDeparture();
                    String timeScheduled = mLeak.getLeakTimeScheduled();
                    String timeEnd = mLeak.getLeakTimeEnd();
                    String timeSeen = mLeak.getLeakTimeSeen();
                    String serviceType = mLeak.getLeakServiceType();
                    String userName = mLeak.getLeakAccountName();
                    String contactName = mLeak.getLeakContactName();
                    String address = mLeak.getLeakAddress();
                    String subject = mLeak.getLeakSubject();
                    String cylinderCapacity = mLeak.getLeakCylinderCapacity();
                    String cylinderColor = mLeak.getLeakCylinderColor();
                    String leakChannel = mLeak.getLeakChannel();
                    String folioSalesNote = mLeak.getLeakFolioSalesNote();
                    String status = mLeak.getLeakStatus().toString();
                    String type = mLeak.getLeakType().toString();
                    String priority = mLeak.getLeakPriority().toString();

                    Log.d(DEBUG_TAG, "OrdersAdapter itemView listener for adapter position: " + requestCode);
                    Log.d(DEBUG_TAG, "Id de la fuga: " + id);
                    Log.d(DEBUG_TAG, "Hora de asignación: " + timeAssignment);
                    Log.d(DEBUG_TAG, "Visto: " + timeSeen);
                    Log.d(DEBUG_TAG, "Hora de salida: " + timeDeparture);
                    Log.d(DEBUG_TAG, "Hora programada: " + timeScheduled);
                    Log.d(DEBUG_TAG, "Hora de llegada: " + timeEnd);
                    Log.d(DEBUG_TAG, "Tipo de servicio: " + serviceType);
                    Log.d(DEBUG_TAG, "Nombre del cliente: " + userName);
                    Log.d(DEBUG_TAG, "Nombre del contacto: " + contactName);
                    Log.d(DEBUG_TAG, "Dirección: " + address);
                    Log.d(DEBUG_TAG, "Descripción: " + subject);
                    Log.d(DEBUG_TAG, "Capacidad del cilindro: " + cylinderCapacity);
                    Log.d(DEBUG_TAG, "Color del cilindro: " + cylinderColor);
                    Log.d(DEBUG_TAG, "Canal de la fuga: " + leakChannel);
                    Log.d(DEBUG_TAG, "Folio: " + folioSalesNote);
                    Log.d(DEBUG_TAG, "Status de la fuga: " + status);
                    Log.d(DEBUG_TAG, "Tipo de caso: " + type);
                    Log.d(DEBUG_TAG, "Prioridad: " + priority);

                    Bundle bundle = new Bundle();
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_ID, id);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_WHO_REPORTS, userName);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SERVICE_TYPE, type);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SUBJECT, subject);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, status);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_PRIORITY, priority);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SALES_NOTE, folioSalesNote);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_TECHNICIAN, timeAssignment);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_SCHEDULED, timeScheduled);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_END, timeEnd);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_DEPARTURE, timeDeparture);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_SEEN, timeSeen);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY, cylinderCapacity);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR, cylinderColor);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL, leakChannel);
                    bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS, address);

                    Log.d(DEBUG_TAG, "Bundle - Id de la fuga: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_ID));
                    Log.d(DEBUG_TAG, "Bundle - WhoReports: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_WHO_REPORTS));
                    Log.d(DEBUG_TAG, "Bundle - Subject: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SUBJECT));
                    Log.d(DEBUG_TAG, "Bundle - Status: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_STATUS));
                    Log.d(DEBUG_TAG, "Bundle - RecordTypeName: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SERVICE_TYPE));
                    Log.d(DEBUG_TAG, "Bundle - Priority: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_PRIORITY));
                    Log.d(DEBUG_TAG, "Bundle - FolioSalesNote: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_SALES_NOTE));
                    Log.d(DEBUG_TAG, "Bundle - DateTimeTechnician: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_TECHNICIAN));
                    Log.d(DEBUG_TAG, "Bundle - DateTimeScheduled: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_SCHEDULED));
                    Log.d(DEBUG_TAG, "Bundle - DateTimeEnd: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_END));
                    Log.d(DEBUG_TAG, "Bundle - DateTimeDeparture: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_DATE_DEPARTURE));
                    Log.d(DEBUG_TAG, "Bundle - CylinderCapacity: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_CYLINDER_CAPACITY));
                    Log.d(DEBUG_TAG, "Bundle - CylinderColor: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_COLOR));
                    Log.d(DEBUG_TAG, "Bundle - Channel: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_CHANNEL));
                    Log.d(DEBUG_TAG, "Bundle - Address: " + bundle.getString(ExtrasHelper.LEAK_JSON_OBJECT_ADDRESS));

                    intent = new Intent();
                    if (type.equals(Order.caseTypes.ORDER.toString())) {
                        intent = new Intent(context, DetailActivityOperator.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (type.equals(Order.caseTypes.LEAKAGE.toString())) {
                        intent = new Intent(context, DetailActivityLeakage.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (type.equals(Order.caseTypes.CUSTOM_SERVICE.toString())) {
                        intent = new Intent(context, DetailActivityService.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (type.equals(Order.caseTypes.CUT.toString())) {
                    } else if (type.equals(Order.caseTypes.RECONNECTION.toString())) {
                    }
                }
            });
        }
    }
}
