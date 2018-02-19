package com.zgas.tesselar.myzuite.Controller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.View.Activity.UserSupervisor.DetailActivitySupervisor;

import java.util.ArrayList;

/**
 * Created by jarvizu on 04/09/2017.
 */

public class SupervisorAdapter extends RecyclerView.Adapter<SupervisorAdapter.SupervisedViewHolder> {

    private static final String DEBUG_TAG = "SupervisorAdapter";
    private Context context;
    private ArrayList<User> mSupervisedList;

    public SupervisorAdapter(Context context, ArrayList<User> mSupervisedList) {
        this.context = context;
        this.mSupervisedList = mSupervisedList;
    }

    @Override
    public SupervisedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_activity_supervisors_my_supervised, parent, false);
        return new SupervisorAdapter.SupervisedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SupervisedViewHolder holder, int position) {
        User mUser = mSupervisedList.get(position);
        String id = mUser.getUserId();
        String userName = mUser.getUserName();
        User.userStatus userStatus = mUser.getUserStatus();
        TextView name = holder.supervisedName;
        TextView status = holder.supervisedStatus;

        if (userName == null) {
            name.setText(context.getString(R.string.no_data));
        } else {
            name.setText(userName);
        }

        if (userStatus == User.userStatus.NOTACTIVE) {
            status.setTextColor(context.getResources().getColor(R.color.red));
            status.setText(String.valueOf(userStatus));
        } else if (userStatus == User.userStatus.ACTIVE) {
            status.setTextColor(context.getResources().getColor(R.color.light_green));
            status.setText(String.valueOf(userStatus));
        } else if (userStatus == null) {
            status.setText(context.getString(R.string.no_data));
        }
        holder.itemView.setTag(mSupervisedList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mSupervisedList.isEmpty()) {
            return 0;
        } else {
            return mSupervisedList.size();
        }
    }

    /**
     *
     */
    public class SupervisedViewHolder extends RecyclerView.ViewHolder {

        private TextView supervisedName;
        private TextView supervisedStatus;

        public SupervisedViewHolder(View itemView) {
            super(itemView);

            supervisedName = itemView.findViewById(R.id.row_activity_supervisor_tv_supervised_name);
            supervisedStatus = itemView.findViewById(R.id.row_activity_supervisor_tv_supervised_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                    User mUser = mSupervisedList.get(requestCode);

                    String id = mUser.getUserId();
                    String name = mUser.getUserName();
                    String email = mUser.getUserEmail();
                    String zone = mUser.getUserZone();
                    String route = mUser.getUserRoute();
                    User.userType type = mUser.getUserType();
                    User.userStatus status = mUser.getUserStatus();

                    Bundle bundle = new Bundle();
                    bundle.putString(ExtrasHelper.EXTRA_USER_ID, id);
                    bundle.putString(ExtrasHelper.EXTRA_USER_NAME, name);
                    bundle.putString(ExtrasHelper.EXTRA_USER_EMAIL, email);
                    bundle.putString(ExtrasHelper.EXTRA_USER_ZONE, zone);
                    bundle.putString(ExtrasHelper.EXTRA_USER_ROUTE, route);
                    bundle.putString(ExtrasHelper.EXTRA_USER_TYPE, String.valueOf(type));
                    bundle.putString(ExtrasHelper.EXTRA_USER_STATUS, String.valueOf(status));

                    Intent intent = new Intent(context, DetailActivitySupervisor.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}


