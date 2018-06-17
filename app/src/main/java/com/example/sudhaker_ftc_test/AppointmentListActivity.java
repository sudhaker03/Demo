package com.example.sudhaker_ftc_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sudhaker_ftc_test.adapter.AppointmentsAdapter;
import com.example.sudhaker_ftc_test.apputils.App_SharedPreferenceConstant;
import com.example.sudhaker_ftc_test.database.App_DataSource;
import com.example.sudhaker_ftc_test.database.Appointment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AppointmentListActivity extends AppCompatActivity {
    ListView lv_Appointments;
    private int backCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 /*Navigate To Add Appointment Activity*/
                Intent intent = new Intent(getApplicationContext(), AddAppointmentActivity.class);
                intent.putExtra("appointment", (Appointment) null);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        lv_Appointments = (ListView) findViewById(R.id.lv_Appointments);
        App_DataSource app_dataSource = App_DataSource.getAppDataSource(getApplicationContext());
        app_dataSource.openDb();
        ArrayList<Appointment> lstApp = app_dataSource.getAllAppointments();
        app_dataSource.closeDb();
        AppointmentsAdapter adapter = new AppointmentsAdapter(getApplicationContext(), lstApp);

        lv_Appointments.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                SharedPreferences s = getSharedPreferences(App_SharedPreferenceConstant.app_PrefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = s.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
        ++backCount;
        Timer t = new Timer();
        t.schedule(new MyTimer(), 3000);
        if (backCount > 1) {
            finish();
        } else {

            Toast.makeText(this, "Press back again to Exit", Toast.LENGTH_SHORT).show();
        }
    }

    class MyTimer extends TimerTask {
        @Override
        public void run() {
            backCount = 0;
        }
    }
}
