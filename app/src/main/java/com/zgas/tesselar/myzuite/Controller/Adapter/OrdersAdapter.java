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
import com.zgas.tesselar.myzuite.Service.PutReviewOrderTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;
import com.zgas.tesselar.myzuite.View.Activity.UserOperator.DetailActivityOperator;
import com.zgas.tesselar.myzuite.View.Activity.UserService.DetailActivityService;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class that provides access to the Order model data items; This class works for both orders
 * and service model objects (because they're both 'order' type).
 *
 * @author jarvizu on 24/10/2017
 * @version 2018.0.9
 * @see Order
 * @see RecyclerSwipeAdapter
 */
public class OrdersAdapter extends RecyclerSwipeAdapter {

    private static final String DEBUG_TAG = "OrdersAdapter";
    protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    private Context context;
    private ArrayList<Order> mOrderList;
    private Intent intent;
    private JSONObject params;
    private UserPreferences userPreferences;
    private Order mOrder;
    private Spinner mSpinnerOptions;
    private Dialog dialog;

    /**
     * Constructor for the OrdersAdapter class.
     *
     * @param context    Current context of the application.
     * @param mOrderList List that contains all the items(orders] that will display on the
     *                   RecyclerView.
     */
    public OrdersAdapter(Context context, ArrayList<Order> mOrderList) {
        this.context = context;
        this.mOrderList = mOrderList;
    }

    /**
     * Method for initializing the viewholders, inflates the RowMainFragment layout.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an
     *                 adapter position.
     * @param viewType The type of the new view.
     * @return LeaksAdapter view.
     */
    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_main_fragment_operator_my_orders, parent, false);
        return new OrdersAdapter.OrderViewHolder(v);
    }

    /**
     * Method that displays the data at an specified position. It Updates the contents of the
     * itemView.
     * This method manages, as well, the bundle object for the leaks model, and maps the
     * components of the LeaksViewHolder class. Also, it opens a new intent for the leak object
     * details.
     * The holder uses the SwipeLayout component; this is for independent swiping on each item of
     * the recyclerview, and manages the swipelistener and onClickListener separately.
     *
     * @param viewHolder The ViewHolder which contents should be updated to represent an item
     *                   depending it's position.
     * @param position   The position of the item within the data set.
     * @see OrderViewHolder
     * @see DetailActivityOperator
     * @see DetailActivityService
     * @see Intent
     * @see Bundle
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final OrderViewHolder holder = (OrderViewHolder) viewHolder;
        mOrder = mOrderList.get(position);
        String caseId = mOrder.getOrderId();
        String caseAddress = mOrder.getOrderAddress();
        String caseNotice = mOrder.getOrderNotice();
        Order.caseStatus caseStatus = mOrder.getOrderStatus();
        Order.caseTypes caseType = mOrder.getOrderType();
        String orderHourIn = mOrder.getOrderTimeAssignment();
        String serviceType = mOrder.getOrderServiceType();

        final TextView id = holder.mOrderId;
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

        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(DEBUG_TAG, "onClick en el pedido: " + mOrder.getOrderId());
                final String id = mOrder.getOrderId();
                String address = mOrder.getOrderAddress();
                String status = mOrder.getOrderStatus().toString();
                String timeAssignment = mOrder.getOrderTimeAssignment();
                String timeSeen = mOrder.getOrderTimeSeen();
                String timeArrival = mOrder.getOrderTimeDeparture();
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
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_review_case);
                dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;

                Log.d(DEBUG_TAG, context.getResources().getString(R.string.on_create));
                dialog.setCancelable(false);

                mSpinnerOptions = dialog.findViewById(R.id.dialog_review_case_spinner);
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

                            params = new JSONObject();
                            userPreferences = new UserPreferences(context);

                            try {
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID, userPreferences.getUserObject().getUserId());
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID, mOrder.getOrderId());
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW, mSpinnerOptions.getSelectedItem().toString());

                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW).toString());

                                new PutReviewOrderTask(context, params).execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mSpinnerOptions.setSelection(0);
                            Toast.makeText(context, context.getResources().getString(R.string.order_review_correct), Toast.LENGTH_LONG).show();
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

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
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

    /**
     * Class that describes an item view and its data, for its place within the RecyclerView.
     * It maps the components between the layout resource and this adapter.
     *
     * @see android.support.v7.widget.RecyclerView.ViewHolder
     */
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

