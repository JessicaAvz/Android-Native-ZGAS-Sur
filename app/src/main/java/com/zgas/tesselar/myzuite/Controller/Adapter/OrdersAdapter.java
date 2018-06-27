package com.zgas.tesselar.myzuite.Controller.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutReviewOrderTask;
import com.zgas.tesselar.myzuite.Service.PutSeenTimeTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;
import com.zgas.tesselar.myzuite.View.Activity.UserOperator.DetailActivityOperator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
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

    private String caseTimeSeen;

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
        userPreferences = new UserPreferences(context);
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
        String caseId = mOrder.getOrderId();
        String caseAddress = mOrder.getOrderAddress();
        String caseNotice = mOrder.getOrderNotice();
        String caseStatus = mOrder.getOrderStatus();
        String caseType = mOrder.getOrderType();
        String caseOrderHourIn = mOrder.getOrderTimeAssignment();
        String caseServiceType = mOrder.getOrderServiceType();
        caseTimeSeen = mOrder.getOrderTimeSeen();

        TextView id = viewHolder.mOrderId;
        TextView address = viewHolder.mOrderAddress;
        TextView hourIn = viewHolder.mOrderTimeIn;
        TextView type = viewHolder.mOrderType;
        TextView notice = viewHolder.mOrderNotice;
        TextView status = viewHolder.mOrderStatus;
        TextView statusText = viewHolder.mOrderStatusText;
        ImageView seenDot = viewHolder.mOrderSeenDot;

        if (caseTimeSeen == null || caseTimeSeen.equals("") || caseTimeSeen.equals("null")) {
            seenDot.setVisibility(View.VISIBLE);
            id.setTypeface(null, Typeface.BOLD);
            address.setTypeface(null, Typeface.BOLD);
            hourIn.setTypeface(null, Typeface.BOLD);
            type.setTypeface(null, Typeface.BOLD);
            notice.setTypeface(null, Typeface.BOLD);
            status.setTypeface(null, Typeface.BOLD);
            statusText.setTypeface(null, Typeface.BOLD);
        }

        if (caseNotice.equals("Sin aviso") || caseNotice.equals("") || caseNotice.equals(null)) {
            notice.setVisibility(View.GONE);
        } else {
            notice.setText("Avisar al cliente: " + caseNotice);
        }

        id.setText("Pedido número: " + String.valueOf(caseId));
        address.setText("Dirección: " + caseAddress);
        type.setText("Tipo: " + caseType + " - " + caseServiceType);

        if (caseOrderHourIn == null || caseOrderHourIn.equals("") || caseOrderHourIn.equals("null")) {
            hourIn.setText("Hora del pedido: " + context.getResources().getString(R.string.no_data));
        } else {
            hourIn.setText("Hora del pedido: " + caseOrderHourIn);
        }

        if (caseStatus.equals(context.getResources().getString(R.string.order_status_failed))) {
            status.setTextColor(context.getResources().getColor(R.color.red));
            viewHolder.mSwipeLayout.setSwipeEnabled(false);
        } else if (caseStatus.equals(context.getResources().getString(R.string.order_status_finished))) {
            viewHolder.mSwipeLayout.setSwipeEnabled(false);
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (caseStatus.equals(context.getResources().getString(R.string.order_status_in_progress))) {
            status.setTextColor(context.getResources().getColor(R.color.amber));
        } else {
            status.setTextColor(context.getResources().getColor(R.color.blue));
        }
        status.setText(caseStatus.toString());

        if (caseStatus.equals(context.getResources().getString(R.string.order_status_reviewing))) {
            viewHolder.mSwipeLayout.setSwipeEnabled(false);
        }

        viewHolder.mSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy h:mm a");
                final String date = dateFormat.format(calendar.getTime());

                Log.d(DEBUG_TAG, "onClick en el pedido: " + mOrderList.get(position).getOrderId());
                mOrder = mOrderList.get(position);
                final String id = mOrder.getOrderId();
                final String address = mOrder.getOrderAddress();
                final String status = mOrder.getOrderStatus();
                final String timeAssignment = mOrder.getOrderTimeAssignment();
                final String timeArrival = mOrder.getOrderTimeDeparture();
                final String timeScheduled = mOrder.getOrderTimeScheduled();
                final String priority = mOrder.getOrderPriority();
                final String userName = mOrder.getOrderAccountName();
                final String paymentMethod = mOrder.getOrderPaymentMethod();
                final String serviceType = mOrder.getOrderServiceType();
                final String recordType = mOrder.getOrderType();
                final String treatmentType = mOrder.getOrderTreatment();
                caseTimeSeen = mOrder.getOrderTimeSeen();

                if (caseTimeSeen == null || caseTimeSeen.equals("") || caseTimeSeen.equals("null")) {
                    params = new JSONObject();
                    userPreferences = new UserPreferences(context);
                    try {
                        PutSeenTimeTask.SeenTimeTaskListener listener = new PutSeenTimeTask.SeenTimeTaskListener() {
                            @Override
                            public void seenTimeErrorResponse(String error) {
                                viewHolder.mSwipeLayout.close(true);
                                Toast.makeText(context, context.getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void seenTimeSuccessResponse(Order order) {
                                viewHolder.mSwipeLayout.close(true);
                                mOrder.setOrderTimeSeen(date);
                                caseTimeSeen = mOrder.getOrderTimeSeen();
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ID, id);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS, address);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, status);
                                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT, timeAssignment);
                                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN, caseTimeSeen);
                                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL, timeArrival);
                                bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED, timeScheduled);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY, priority);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME, userName);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD, paymentMethod);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_RECORD_TYPE, recordType);
                                bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);
                                bundle.putString(ExtrasHelper.ORDER_JSON_TREATMENT, treatmentType);

                                intent = new Intent();
                                intent = new Intent(context, DetailActivityOperator.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        };

                        params.put(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID, userPreferences.getUserObject().getUserId());
                        params.put(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID, id);
                        params.put(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN, date);

                        PutSeenTimeTask putSeenTimeTask = new PutSeenTimeTask(context, params);
                        putSeenTimeTask.setSeenTimeTaskListener(listener);
                        putSeenTimeTask.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ID, id);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS, address);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_STATUS, status);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT, timeAssignment);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SEEN, caseTimeSeen);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ARRIVAL, timeArrival);
                    bundle.putSerializable(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED, timeScheduled);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY, priority);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME, userName);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD, paymentMethod);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_RECORD_TYPE, recordType);
                    bundle.putString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE, serviceType);
                    bundle.putString(ExtrasHelper.ORDER_JSON_TREATMENT, treatmentType);

                    intent = new Intent();
                    intent = new Intent(context, DetailActivityOperator.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
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
                                        viewHolder.mSwipeLayout.close(true);
                                        Toast.makeText(context, context.getResources().getString(R.string.order_review_incorrect), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void reviewOrderSuccessResponse(Order order) {
                                        viewHolder.mSwipeLayout.close(true);
                                        Toast.makeText(context, context.getResources().getString(R.string.order_review_correct), Toast.LENGTH_LONG).show();
                                    }
                                };

                                PutReviewOrderTask putReviewOrderTask = new PutReviewOrderTask(context, params);
                                putReviewOrderTask.setOrderReviewTaskListener(listener);
                                putReviewOrderTask.execute();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mSpinnerOptions.setSelection(0);
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

        @BindView(R.id.row_main_fragment_swipe_orders)
        SwipeLayout mSwipeLayout;
        @BindView(R.id.row_visit_recycler_tv_review_visit)
        TextView mOrderReview;
        @BindView(R.id.row_main_fragment_tv_order_id)
        TextView mOrderId;
        @BindView(R.id.row_main_fragment_tv_order_status)
        TextView mOrderStatus;
        @BindView(R.id.row_main_fragment_prompt_order_status)
        TextView mOrderStatusText;
        @BindView(R.id.row_main_fragment_tv_order_address)
        TextView mOrderAddress;
        @BindView(R.id.row_main_fragment_tv_order_in)
        TextView mOrderTimeIn;
        @BindView(R.id.row_main_fragment_tv_order_type)
        TextView mOrderType;
        @BindView(R.id.row_main_fragment_tv_notice)
        TextView mOrderNotice;
        @BindView(R.id.row_main_fragment_seen_dot)
        ImageView mOrderSeenDot;

        OrderViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

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

