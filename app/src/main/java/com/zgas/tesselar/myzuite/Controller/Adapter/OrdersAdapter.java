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
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutReviewOrderTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;
import com.zgas.tesselar.myzuite.View.Activity.UserOperator.DetailActivityOperator;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Class that provides access to the Order model data items; This class works for both orders
 * and service model objects (because they're both 'order' type).
 *
 * @author jarvizu on 24/10/2017
 * @version 2018.0.9
 * @see Order
 * @see ButterKnife
 * @see RecyclerSwipeAdapter
 */
public class OrdersAdapter extends RecyclerSwipeAdapter<OrdersAdapter.OrderViewHolder> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    private Context context;
    private ArrayList<Order> mOrderList;
    private Intent intent;
    private JSONObject params;
    private UserPreferences userPreferences;
    private Order mOrder;
    private Spinner mSpinnerOptions;
    private Dialog dialog;

    private String caseId;
    private String caseAddress;
    private String caseNotice;
    private String caseStatus;
    private String caseType;
    private String orderHourIn;
    private String serviceType;

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
        return new OrderViewHolder(v);
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
     * @see Intent
     * @see Bundle
     */
    @Override
    public void onBindViewHolder(final OrderViewHolder viewHolder, final int position) {
        mOrder = mOrderList.get(position);
        caseId = mOrder.getOrderId();
        caseAddress = mOrder.getOrderAddress();
        caseNotice = mOrder.getOrderNotice();
        caseStatus = mOrder.getOrderStatus();
        caseType = mOrder.getOrderType();
        orderHourIn = mOrder.getOrderTimeAssignment();
        serviceType = mOrder.getOrderServiceType();

        Log.d(DEBUG_TAG, String.valueOf(position));
        Log.d(DEBUG_TAG, caseId);
        Log.d(DEBUG_TAG, caseAddress);
        Log.d(DEBUG_TAG, caseNotice);
        Log.d(DEBUG_TAG, caseStatus);

        TextView id = viewHolder.mOrderId;
        TextView address = viewHolder.mOrderAddress;
        TextView hourIn = viewHolder.mOrderTimeIn;
        TextView type = viewHolder.mOrderType;
        TextView notice = viewHolder.mOrderNotice;
        TextView status = viewHolder.mOrderStatus;

        if (caseNotice.equals("Sin aviso")) {
            notice.setVisibility(View.GONE);
        } else {
            notice.setText("Avisar al cliente: " + caseNotice);
        }

        id.setText("Pedido número: " + String.valueOf(caseId));
        address.setText("Dirección: " + caseAddress);
        type.setText("Tipo: " + caseType + " - " + serviceType);

        if (orderHourIn == null || orderHourIn.equals("")) {
            hourIn.setText("Hora del pedido: " + context.getResources().getString(R.string.no_data));
        } else {
            hourIn.setText("Hora del pedido: " + orderHourIn);
        }

        if (caseStatus.equals(context.getResources().getString(R.string.order_status_canceled))) {
            status.setTextColor(context.getResources().getColor(R.color.red));
        } else if (caseStatus.equals(context.getResources().getString(R.string.order_status_finished))) {
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (caseStatus.equals(context.getResources().getString(R.string.order_status_in_progress))) {
            status.setTextColor(context.getResources().getColor(R.color.amber));
        } else {
            status.setTextColor(context.getResources().getColor(R.color.blue));
        }
        status.setText(caseStatus.toString());

        viewHolder.mSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(DEBUG_TAG, "onClick en el pedido: " + mOrderList.get(position).getOrderId());
                mOrder = mOrderList.get(position);
                String id = mOrder.getOrderId();
                String address = mOrder.getOrderAddress();
                String status = mOrder.getOrderStatus();
                String timeAssignment = mOrder.getOrderTimeAssignment();
                String timeSeen = mOrder.getOrderTimeSeen();
                String timeArrival = mOrder.getOrderTimeDeparture();
                String timeScheduled = mOrder.getOrderTimeScheduled();
                String priority = mOrder.getOrderPriority();
                String userName = mOrder.getOrderAccountName();
                String paymentMethod = mOrder.getOrderPaymentMethod();
                String serviceType = mOrder.getOrderServiceType();
                String recordType = mOrder.getOrderType();

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
                intent = new Intent(context, DetailActivityOperator.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        viewHolder.mOrderReview.setOnClickListener(new View.OnClickListener() {
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
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID, mOrderList.get(position).getOrderId());
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW, mSpinnerOptions.getSelectedItem().toString());

                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW).toString());

                                PutReviewOrderTask.OrderReviewTaskListener listener = new PutReviewOrderTask.OrderReviewTaskListener() {
                                    @Override
                                    public void reviewOrderErrorResponse(String error) {
                                        Log.d(DEBUG_TAG, "Daniel Come caca");
                                    }

                                    @Override
                                    public void reviewOrderSuccessResponse(Order order) {
                                        Log.d(DEBUG_TAG, "Jessica Come kk");
                                    }
                                };

                                PutReviewOrderTask t = new PutReviewOrderTask(context, params);
                                t.setOrderReviewTaskListener(listener);
                                t.execute();

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

        SwipeLayout mSwipeLayout;
        TextView mOrderReview;
        TextView mOrderId;
        TextView mOrderStatus;
        TextView mOrderAddress;
        TextView mOrderTimeIn;
        TextView mOrderType;
        TextView mOrderNotice;

        OrderViewHolder(final View itemView) {
            super(itemView);

            mSwipeLayout = itemView.findViewById(R.id.row_main_fragment_swipe_orders);
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
                    Log.d(DEBUG_TAG, "OrdersAdapter view listener for adapter position: " + requestCode);
                }
            });
        }
    }
}

