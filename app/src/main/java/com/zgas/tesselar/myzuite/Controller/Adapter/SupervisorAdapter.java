package com.zgas.tesselar.myzuite.Controller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zgas.tesselar.myzuite.Controller.Activity.MainActivity;
import com.zgas.tesselar.myzuite.Controller.Activity.UserSupervisor.DetailActivitySupervisor;
import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = getAdapterPosition();
                    User mUser = mSupervisedList.get(requestCode);

                    int id = mUser.getUserId();
                    String name = mUser.getUserName();
                    String lastname = mUser.getUserLastname();
                    String email = mUser.getUserEmail();
                    String zone = mUser.getUserZone();
                    String route = mUser.getUserRoute();
                    User.userType type = mUser.getUserType();
                    User.userStatus status = mUser.getUserstatus();

                    Log.d(DEBUG_TAG, "Id del usuario: " + String.valueOf(id));
                    Log.d(DEBUG_TAG, "Nombre del usuario: " + name);
                    Log.d(DEBUG_TAG, "Apellido del usuario:" + lastname);
                    Log.d(DEBUG_TAG, "Correo: " + email);
                    Log.d(DEBUG_TAG, "Zona: " + zone);
                    Log.d(DEBUG_TAG, "Ruta: " + route);
                    Log.d(DEBUG_TAG, "Tipo de usuario: " + String.valueOf(type));
                    Log.d(DEBUG_TAG, "Estatus del usuario: " + String.valueOf(status));

                    Bundle bundle = new Bundle();
                    bundle.putInt(MainActivity.EXTRA_USER_ID, id);
                    bundle.putString(MainActivity.EXTRA_USER_NAME, name);
                    bundle.putString(MainActivity.EXTRA_USER_LASTNAME, lastname);
                    bundle.putString(MainActivity.EXTRA_USER_EMAIL, email);
                    bundle.putString(MainActivity.EXTRA_USER_ZONE, zone);
                    bundle.putString(MainActivity.EXTRA_USER_ROUTE, route);
                    bundle.putString(MainActivity.EXTRA_USER_TYPE, String.valueOf(type));
                    bundle.putString(MainActivity.EXTRA_USER_STATUS, String.valueOf(status));

                    Log.d(DEBUG_TAG, "SupervisorAdapter itemView listener for adapter position: " + requestCode);

                    Intent intent = new Intent(context, DetailActivitySupervisor.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}

