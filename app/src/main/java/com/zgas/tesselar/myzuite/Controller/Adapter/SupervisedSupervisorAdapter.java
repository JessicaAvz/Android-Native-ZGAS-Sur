package com.zgas.tesselar.myzuite.Controller.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;

import java.util.ArrayList;

/**
 * Created by jarvizu on 04/09/2017.
 */

public class SupervisedSupervisorAdapter extends RecyclerView.Adapter<SupervisedSupervisorAdapter.SupervisedViewHolder> {

    private static final String DEBUG_TAG = "SupervisedSupervisorAdapter";
    private Context context;
    private ArrayList<User> mSupervisedList;

    public SupervisedSupervisorAdapter(Context context, ArrayList<User> mSupervisedList) {
        this.context = context;
        this.mSupervisedList = mSupervisedList;
    }

    @Override
    public SupervisedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.row_activity_supervisors_my_supervised, parent, false);
        return new SupervisedSupervisorAdapter.SupervisedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SupervisedViewHolder holder, int position) {
        User mUser = mSupervisedList.get(position);
        int id = mUser.getUserId();
        String userName = mUser.getUserName();
        String userLastname = mUser.getUserLastname();
        User.userStatus userStatus = mUser.getUserstatus();

        TextView name = holder.supervisedName;
        name.setText(userName + " " + userLastname);

        TextView status = holder.supervisedStatus;
        if (userStatus == User.userStatus.NOTACTIVE) {
            status.setTextColor(context.getResources().getColor(R.color.red));
        } else if (userStatus == User.userStatus.ACTIVE) {
            status.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (userStatus == User.userStatus.VACATION) {
            status.setTextColor(context.getResources().getColor(R.color.amber));
        }
        status.setText(String.valueOf(userStatus));

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

    public class SupervisedViewHolder extends RecyclerView.ViewHolder {

        private TextView supervisedName;
        private TextView supervisedStatus;

        public SupervisedViewHolder(View itemView) {
            super(itemView);

            supervisedName = itemView.findViewById(R.id.row_activity_supervisor_tv_supervised_name);
            supervisedStatus = itemView.findViewById(R.id.row_activity_supervisor_tv_supervised_status);
        }
    }
}
