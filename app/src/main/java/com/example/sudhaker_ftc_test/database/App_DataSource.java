package com.example.sudhaker_ftc_test.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by sudhaker on 1/6/2018.
 */

public class App_DataSource {
    private static App_DataSource app_DataSource = null;
    private Context mContext;
    App_SqliteHelper app_SqliteHelper;
    SQLiteDatabase app_Db;

    private App_DataSource(Context context) {
        app_SqliteHelper = new App_SqliteHelper(context);
    }

    public static App_DataSource getAppDataSource(Context context) {
        if (app_DataSource == null) {
            return new App_DataSource(context);
        }
        return app_DataSource;

    }

    public void openDb() {
        app_Db = app_SqliteHelper.getWritableDatabase();
    }

    public void closeDb() {
        app_Db.close();
    }

    public long insertIntoUserTable(ContentValues userContent) {
        return app_Db.insert(User.TBL_USER, null, userContent);
    }

    public boolean isUserAvailable(String email, String password) {
        String selection = "" + User.USER_EMAIL + "=? and " + User.USER_PASSWORD + "=?";
        String[] selectionArgs = {email, password};
        String[] allColumns = {User.USER_ID};
        Cursor cursor = app_Db.query(User.TBL_USER, allColumns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() > 0) {
            return true;

        }
        return false;
    }

    public int resetPassword(String email, ContentValues values) {
        String selection = "" + User.USER_EMAIL + "=?";
        String[] selectionArgs = {email};
        return app_Db.update(User.TBL_USER, values, selection, selectionArgs);
    }

    public boolean userAllreadyExist(String email) {
        String selection = "" + User.USER_EMAIL + "=?";
        String[] selectionArgs = {email};
        String[] allColumns = {User.USER_ID};
        Cursor cursor = app_Db.query(User.TBL_USER, allColumns, selection, selectionArgs, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> lstAppoinment = new ArrayList<>();
        String[] allColumns = {Appointment._ID, Appointment.TITLE, Appointment.DESCRIPTION, Appointment.TIME, Appointment.REMINDMEBEFORE, Appointment.PARTICIPANTS};
        Cursor cursor = app_Db.query(Appointment.TBL_APPOINTMENT, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        Appointment appointment;
        while (!cursor.isAfterLast()) {
            appointment = new Appointment();
            appointment.setAppointmentId(cursor.getInt(0));
            appointment.setTitle(cursor.getString(1));
            appointment.setDescription(cursor.getString(2));
            appointment.setTime(cursor.getString(3));
            appointment.setRemindBefore(cursor.getString(4));
            ArrayList<String> participants = new ArrayList<>();
            if (cursor.getString(5).contains(",")) {
                String[] arr = cursor.getString(5).split(",");
                for (int i = 0; i < arr.length; i++) {
                    participants.add(arr[i]);
                }
            } else {
                participants.add(cursor.getString(5));
            }
            appointment.setCommaSepratedParticipants(cursor.getString(5));
            appointment.setParticipants(participants);
            lstAppoinment.add(appointment);
            cursor.moveToNext();
        }
        cursor.close();
        return lstAppoinment;
    }

    public long insertAppointment(ContentValues values) {
        return app_Db.insert(Appointment.TBL_APPOINTMENT, null, values);
    }

    public long updateAppointmentById(int appointmentId, ContentValues values) {
        String where = "" + Appointment._ID + "=?";
        String[] whereArgs = {appointmentId + ""};
        return app_Db.update(Appointment.TBL_APPOINTMENT, values, where, whereArgs);
    }

    public int deleteAppointment(int appointmentId) {
        String where = "" + Appointment._ID + "=?";
        String[] whereArgs = {appointmentId + ""};
        return app_Db.delete(Appointment.TBL_APPOINTMENT, where, whereArgs);
    }

    public int getCountOfAppointmentsByTimeTitleAndDes(String time, String title, String des) {

        String selection = "" + Appointment.TIME + "=? and " + Appointment.TITLE + "=? and " + Appointment.DESCRIPTION + "=?";
        String[] selectionArgs = {time, title, des};
        String[] allColumns = {Appointment._ID};
        Cursor cursor = app_Db.query(Appointment.TBL_APPOINTMENT, allColumns, selection, selectionArgs, null, null, null);
        return cursor.getCount();
    }
}
