package com.zgas.tesselar.myzuite.Controller.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.View.Activity.UserOperator.DetailActivityOperator;
import com.zgas.tesselar.myzuite.View.Activity.UserService.DetailActivityService;

import java.util.ArrayList;

/**
 * Created by jarvizu on 29/08/2017.
 */

public class OrdersAdapter extends RecyclerSwipeAdapter {

    private static final String DEBUG_TAG = "OrdersAdapter";
    protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final OrderViewHolder holder = (OrderViewHolder) viewHolder;
        final Order mOrder = mOrderList.get(position);
        String caseId = mOrder.getOrderId();
        String caseAddress = mOrder.getOrderAddress();
        String caseNotice = mOrder.getOrderNotice();
        Order.caseStatus caseStatus = mOrder.getOrderStatus();
        Order.caseTypes caseType = mOrder.getOrderType();
        String orderHourIn = mOrder.getOrderTimeAssignment();
        String serviceType = mOrder.getOrderServiceType();

        TextView id = holder.mOrderId;
        TextView address = holder.mOrderAddress;
        TextView hourIn = holder.mOrderTimeIn;
        TextView type = holder.mOrderType;
        TextView notice = holder.mOrderNotice;

        if (caseNotice.equals("Sin aviso")) {
            notice.setVisibility(View.GONE);
        } else {
            notice.setText("Avisar al cliente: " + caseNotice);
        }

        id.setText(caseId);
        address.setText(caseAddress);


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
            status.setTextColor(context.getResources().getColor(R.color.red));
        } else if (caseStatus == Order.caseStatus.FINISHED) {
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (caseStatus == Order.caseStatus.INPROGRESS) {
            status.setTextColor(context.getResources().getColor(R.color.amber));
        } else {
            status.setTextColor(context.getResources().getColor(R.color.blue));
        }
        status.setText(caseStatus.toString());
        holder.itemView.setTag(mOrderList.get(position));

        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(DEBUG_TAG, "onClick en el pedido: " + mOrder.getOrderId());
                String id = mOrder.getOrderId();
                String address = mOrder.getOrderAddress();
                String status = mOrder.getOrderStatus().toString();
                String timeAssignment = mOrder.getOrderTimeAssignment();
                String timeSeen = mOrder.getOrderTimeSeen();
                String timeArrival = mOrder.getOrderTimeArrival();
                String timeScheduled = mOrder.getOrderTimeScheduled();
                String priority = mOrder.getOrderPriority().toString();
                String userName = mOrder.getOrderAccountName();
                String paymentMethod = mOrder.getOrderPaymentMethod();
                String serviceType = mOrder.getOrderServiceType();
                String recordType = mOrder.getOrderType().toString();

                Bundle bundle = new Bundle();
                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ID, id);
                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS, address);
                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, status);
                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT, timeAssignment);
                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN, timeSeen);
                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL, timeArrival);
                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED, timeScheduled);
                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY, priority);
                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME, userName);
                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);
                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD, paymentMethod);
                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_RECORD_TYPE, recordType);
                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);

                intent = new Intent();
                if (serviceType.equals(Order.caseTypes.MEASURED.toString()) && recordType.equals(Order.caseTypes.ORDER.toString())) {
                    intent = new Intent(context, DetailActivityService.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else if (recordType.equals(Order.caseTypes.ORDER.toString())) {
                    intent = new Intent(context, DetailActivityOperator.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });

        holder.mOrderReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_review_case);
                dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;

                Log.d(DEBUG_TAG, context.getResources().getString(R.string.on_create));
                dialog.setCancelable(false);

                final Spinner mSpinnerOptions = dialog.findViewById(R.id.dialog_review_case_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.order_prompts_review, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter, R.layout.contact_spinner_row_nothing_selected, context));

                Button mBtnAccept = dialog.findViewById(R.id.dialog_review_case_button_accept);
                mBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSpinnerOptions.getSelectedItem() == null) {
                            Toast.makeText(context, context.getResources().getString(R.string.order_review_incorrect), Toast.LENGTH_LONG).show();
                        } else {
                            //srtCancellationReason = mSpinnerOptions.getSelectedItem().toString();
                            mSpinnerOptions.setSelection(0);
                            Toast.makeText(context, context.getResources().getString(R.string.order_review_correct), Toast.LENGTH_LONG).show();
                            //callAsyncTaskCancelled();
                            dialog.dismiss();
                        }
                    }
                });

                Button mBtnCancel = dialog.findViewById(R.id.dialog_review_case_button_cancel);
                mBtnCancel.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mOrderList.isEmpty()) {
            return 0;
        } else {
            return mOrderList.size();
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return position;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private SwipeLayout swipeLayout;
        private TextView mOrderReview;
        private TextView mOrderId;
        private TextView mOrderStatus;
        private TextView mOrderAddress;
        private TextView mOrderTimeIn;
        private TextView mOrderType;
        private TextView mOrderNotice;

        public OrderViewHolder(final View itemView) {
            super(itemView);

            //if de swipe layout si es order o service
            swipeLayout = itemView.findViewById(R.id.row_main_fragment_swipe_orders);
            mOrderReview = itemView.findViewById(R.id.row_visit_recycler_tv_review_visit);
            mOrderId = itemView.findViewById(R.id.row_main_fragment_tv_order_id);
            mOrderStatus = itemView.findViewById(R.id.row_main_fragment_tv_order_status);
            mOrderAddress = itemView.findViewById(R.id.row_main_fragment_tv_order_address);
            mOrderTimeIn = itemView.findViewById(R.id.row_main_fragment_tv_order_in);
            mOrderType = itemView.findViewById(R.id.row_main_fragment_tv_order_type);
            mOrderNotice = itemView.findViewById(R.id.row_main_fragment_tv_notice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                    Log.d(DEBUG_TAG, "OrdersAdapter itemView listener for adapter position: " + requestCode);
                }
            });
        }
    }
}

