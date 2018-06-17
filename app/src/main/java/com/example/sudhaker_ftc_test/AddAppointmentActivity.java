package com.example.sudhaker_ftc_test;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sudhaker_ftc_test.database.App_DataSource;
import com.example.sudhaker_ftc_test.database.Appointment;
import com.example.sudhaker_ftc_test.receiver.AlarmReceiver;


public class AddAppointmentActivity extends AppCompatActivity {
    private static final int CONTACT_REQUEST_CODE = 1002;
    private static final int ALARM_REQUEST_CODE = 1003;
    private EditText et_Title, et_Description, et_Time, et_Participants;
    private Button btn_Save;
    private Spinner sp_RemindBefore;
    private StringBuilder alarmTimeString;
    private int appointmentIdForEditCase = 0;

    public static Set<String> contactListToCheckForUpdateCase = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        findViewsByIds();
        if (getIntent().getExtras().getSerializable("appointment") != null) {
            Appointment appointment = (Appointment) (getIntent().getExtras().getSerializable("appointment"));
            btn_Save.setText("Update");
            et_Description.setText(appointment.getDescription());
            et_Time.setText(appointment.getTime());
            et_Title.setText(appointment.getTitle());
            et_Participants.setText(appointment.getCommaSepratedParticipants());
            appointmentIdForEditCase = appointment.getAppointmentId();
            contactListToCheckForUpdateCase.addAll(appointment.getParticipants());
        }


        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateUserInputs()) {
                    ContentValues values = new ContentValues();
                    values.put(Appointment.TITLE, et_Title.getText().toString());
                    values.put(Appointment.DESCRIPTION, et_Description.getText().toString());
                    values.put(Appointment.TIME, et_Time.getText().toString());
                    values.put(Appointment.REMINDMEBEFORE, sp_RemindBefore.getSelectedItem().toString());
                    values.put(Appointment.PARTICIPANTS, et_Participants.getText().toString());
                    App_DataSource app_dataSource = App_DataSource.getAppDataSource(getApplicationContext());
                    app_dataSource.openDb();
                    long count = 0;
                    if (btn_Save.getText().toString().equals("Update") && appointmentIdForEditCase != 0) {
                        count = app_dataSource.updateAppointmentById(appointmentIdForEditCase, values);
                    } else {
                        count = app_dataSource.insertAppointment(values);
                    }
                    app_dataSource.closeDb();
                    if (count > 0) {


                        /////Set Alarm
                        String s = et_Time.getText().toString();

                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent intent = new Intent(AddAppointmentActivity.this, AlarmReceiver.class);
//                       put all appointment related data in intent
                        intent.putExtra("title", et_Title.getText().toString());
                        intent.putExtra("des", et_Description.getText().toString());
                        intent.putExtra("time", et_Time.getText().toString());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddAppointmentActivity.this, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                        long millis = getMillis(s);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);

                        ////Set Alarm

                        if (btn_Save.getText().toString().equals("Update") && appointmentIdForEditCase != 0) {
                            Toast.makeText(getApplicationContext(), R.string.app_updated, Toast.LENGTH_SHORT).show();
                            contactListToCheckForUpdateCase.clear();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.app_added, Toast.LENGTH_SHORT).show();
                            contactListToCheckForUpdateCase.clear();
                        }
                        et_Participants.setText("");
                        et_Title.setText("");
                        et_Time.setText("");
                        et_Description.setText("");
                        Intent i = new Intent(getApplicationContext(), AppointmentListActivity.class);
                        startActivity(i);
                        finish();
                    }


                }


            }
        });
        et_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                alarmTimeString = new StringBuilder();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddAppointmentActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        et_Participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddAppointmentActivity.this, ContactList.class), CONTACT_REQUEST_CODE);
            }
        });

        setDataToRemindSpinner();
    }

    private boolean validateUserInputs() {
        if (et_Title.getText().toString().matches("")) {
            Toast.makeText(this, R.string.enterTitle, Toast.LENGTH_SHORT).show();
            return false;

        }

        if (et_Description.getText().toString().matches("")) {
            Toast.makeText(this, R.string.enter_des, Toast.LENGTH_SHORT).show();
            return false;

        }
        if (et_Time.getText().toString().matches("")) {
            Toast.makeText(this, R.string.setTime, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (getMillis(et_Time.getText().toString()) < System.currentTimeMillis()) {
            Toast.makeText(this, "Please Select Mininmum 30 Minute later of current time", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_Participants.getText().toString().matches("")) {
            Toast.makeText(this, R.string.add_participants, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setDataToRemindSpinner() {
        final String[] remind = getResources().getStringArray(R.array.RemindBefore);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text, remind);
        sp_RemindBefore.setAdapter(aa);
        sp_RemindBefore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void findViewsByIds() {
        btn_Save = (Button) findViewById(R.id.btn_Save);
        sp_RemindBefore = (Spinner) findViewById(R.id.sp_RemindBefore);
        et_Title = (EditText) findViewById(R.id.et_Title);
        et_Description = (EditText) findViewById(R.id.et_Description);
        et_Time = (EditText) findViewById(R.id.et_Time);
        et_Participants = (EditText) findViewById(R.id.et_Participants);
    }


    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (view.isShown()) {

                alarmTimeString.append(dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth);
                alarmTimeString.append("/");
                alarmTimeString.append(month < 9 ? ("0" + (month + 1)) : (month + 1));
                alarmTimeString.append("/");
                alarmTimeString.append(year);
                Calendar instance = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddAppointmentActivity.this, onTimeSetListener, instance.get(Calendar.HOUR_OF_DAY), instance.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (view.isShown()) {

                alarmTimeString.append("  ");
                alarmTimeString.append(hourOfDay < 10 ? ("0" + hourOfDay) : hourOfDay);
                alarmTimeString.append(":");
                alarmTimeString.append(minute < 10 ? ("0" + minute) : minute);
                et_Time.setText(alarmTimeString);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_REQUEST_CODE && resultCode == RESULT_OK) {
            String contact = data.getStringExtra("contact");
            et_Participants.setText(contact);
        }
    }

    public long getMillis(String time) {
        long millis = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        try {
            millis = dateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (sp_RemindBefore.getSelectedItem().toString().equals("30 Minute")) {
            millis = millis - 30 * 60 * 1000;
        } else if (sp_RemindBefore.getSelectedItem().toString().equals("1 Hour")) {
            millis = millis - 60 * 60 * 1000;
        } else if (sp_RemindBefore.getSelectedItem().toString().equals("2 Hour")) {
            millis = millis - 120 * 60 * 1000;
        }
        return millis;
    }
}
