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
import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Service.PutReviewLeakTask;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;
import com.zgas.tesselar.myzuite.View.Activity.UserLeakage.DetailActivityLeakage;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that provides access to the Leak model data items.
 *
 * @author jarvizu on 24/10/2017
 * @version 2018.0.9
 * @see Leak
 * @see ButterKnife
 * @see RecyclerSwipeAdapter
 */
public class LeaksAdapter extends RecyclerSwipeAdapter {

    private static final String DEBUG_TAG = "LeaksAdapter";
    protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    private Context context;
    private ArrayList<Leak> mLeaksList;
    private Intent intent;
    private JSONObject params;
    private Spinner mSpinnerOptions;
    private Dialog dialog;
    private Leak mLeak;
    private UserPreferences userPreferences;

    /**
     * Constructor for the LeaksAdapter class.
     *
     * @param context    Current context of the application.
     * @param mLeaksList List that contains all the items(leaks] that will display on the
     *                   RecyclerView.
     */
    public LeaksAdapter(Context context, ArrayList<Leak> mLeaksList) {
        this.context = context;
        this.mLeaksList = mLeaksList;
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
    public LeaksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_main_fragment_operator_my_orders, parent, false);
        return new LeaksAdapter.LeaksViewHolder(v);
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
     * @see LeaksViewHolder
     * @see DetailActivityLeakage
     * @see Intent
     * @see Bundle
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final LeaksViewHolder holder = (LeaksViewHolder) viewHolder;
        mLeak = mLeaksList.get(position);
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

        id.setText("Reporte número: " + String.valueOf(leakId));
        address.setText("Dirección: " + leakAddress);
        type.setText("Tipo: " + leakType);
        if (leakHourIn == null || leakHourIn.equals("")) {
            hourIn.setText("Hora del reporte: " + context.getResources().getString(R.string.no_data));
        } else {
            hourIn.setText("Hora del reporte: " + leakHourIn);
        }

        TextView status = holder.mLeakStatus;
        if (leakStatus == Leak.leakStatus.CANCELLED) {
            status.setTextColor(context.getResources().getColor(R.color.red));
        } else if (leakStatus == Leak.leakStatus.FINISHED) {
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (leakStatus == Leak.leakStatus.INPROGRESS) {
            status.setTextColor(context.getResources().getColor(R.color.amber));
        } else {
            status.setTextColor(context.getResources().getColor(R.color.blue));
        }
        status.setText(leakStatus.toString());
        holder.itemView.setTag(mLeaksList.get(position));

        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Leak mLeak = mLeaksList.get(position);
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
                String priority = mLeak.getLeakPriority().toString();

                Bundle bundle = new Bundle();
                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_ID, id);
                bundle.putString(ExtrasHelper.LEAK_JSON_OBJECT_WHO_REPORTS, userName);
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

                intent = new Intent();
                intent = new Intent(context, DetailActivityLeakage.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        holder.mLeakReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_review_case);
                dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_Dialog_Animation;

                dialog.setCancelable(false);

                mSpinnerOptions = dialog.findViewById(R.id.dialog_review_case_spinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.order_prompts_review,
                        android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerOptions.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
                        R.layout.contact_spinner_row_nothing_selected, context));

                Button mBtnAccept = dialog.findViewById(R.id.dialog_review_case_button_accept);
                mBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSpinnerOptions.getSelectedItem() == null) {
                            Toast.makeText(context, context.getResources().getString(R.string.order_review_incorrect)
                                    , Toast.LENGTH_LONG).show();
                        } else {
                            params = new JSONObject();
                            userPreferences = new UserPreferences(context);

                            try {
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID, userPreferences.getUserObject().getUserId());
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID, mLeak.getLeakId());
                                params.put(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW, mSpinnerOptions.getSelectedItem().toString());

                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_OPERATOR_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_ORDER_ID).toString());
                                Log.d(DEBUG_TAG, params.get(ExtrasHelper.REVIEW_JSON_OBJECT_REVIEW).toString());

                                new PutReviewLeakTask(context, params).execute();

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
        if (mLeaksList.isEmpty()) {
            return 0;
        } else {
            return mLeaksList.size();
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
    public class LeaksViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_main_fragment_swipe_orders)
        SwipeLayout swipeLayout;
        @BindView(R.id.row_visit_recycler_tv_review_visit)
        TextView mLeakReview;
        @BindView(R.id.row_main_fragment_tv_order_id)
        TextView mLeakId;
        @BindView(R.id.row_main_fragment_tv_order_status)
        TextView mLeakStatus;
        @BindView(R.id.row_main_fragment_tv_order_address)
        TextView mLeakAddress;
        @BindView(R.id.row_main_fragment_tv_order_in)
        TextView mLeakTimeIn;
        @BindView(R.id.row_main_fragment_tv_order_type)
        TextView mLeakType;
        @BindView(R.id.row_main_fragment_tv_notice)
        TextView mLeakNotice;

        public LeaksViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mLeakNotice.setVisibility(View.GONE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                    Log.d(DEBUG_TAG, "OrdersAdapter view listener for adapter position: " + requestCode);
                }
            });
        }
    }
}
