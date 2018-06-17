package com.example.sudhaker_ftc_test.adapter;


import android.app.Dialog;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sudhaker_ftc_test.AddAppointmentActivity;
import com.example.sudhaker_ftc_test.R;

import com.example.sudhaker_ftc_test.database.App_DataSource;
import com.example.sudhaker_ftc_test.database.Appointment;

import java.util.ArrayList;

/**
 * Created by sudhaker on 1/6/2018.
 */

public class AppointmentsAdapter extends ArrayAdapter<Appointment> {
    ArrayList<Appointment> lstAppointment;
    Context mContext;



    public AppointmentsAdapter(@NonNull Context context, ArrayList<Appointment> lstAppointment ) {
        super(context, R.layout.row_appointment, lstAppointment);
        this.lstAppointment = lstAppointment;
        this.mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.row_appointment, parent, false);
        } else {
            v = convertView;
        }
        TextView tv_Title = (TextView) v.findViewById(R.id.tv_Title);
        TextView tv_With = (TextView) v.findViewById(R.id.tv_With);
        ImageView iv_Edit = (ImageView) v.findViewById(R.id.iv_Edit);
        ImageView iv_Delete = (ImageView) v.findViewById(R.id.iv_Delete);

        tv_Title.setText(lstAppointment.get(position).getTitle());
        tv_With.setText("With : " + lstAppointment.get(position).getCommaSepratedParticipants());
        iv_Edit.setTag(lstAppointment.get(position));
        iv_Delete.setTag(lstAppointment.get(position));
        iv_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appointment appointment = (Appointment) view.getTag();

                App_DataSource app_dataSource = App_DataSource.getAppDataSource(mContext);
                app_dataSource.openDb();
                if (app_dataSource.deleteAppointment(appointment.getAppointmentId()) > 0) {
                    lstAppointment.remove(appointment);
                    notifyDataSetChanged();
                }
                app_dataSource.closeDb();
            }
        });
        iv_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Appointment appointment = (Appointment) view.getTag();
                Intent intent = new Intent(mContext, AddAppointmentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("appointment", appointment);
                mContext.startActivity(intent);

            }
        });

        return v;
    }


}
