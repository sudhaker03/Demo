package com.example.sudhaker_ftc_test.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sudhaker on 1/6/2018.
 */

public class App_SqliteHelper extends SQLiteOpenHelper {

    private static int db_Version = 1;
    private static final String DB_NAME = "app_Db";

    public App_SqliteHelper(Context context) {
        super(context, DB_NAME, null, db_Version);

    }

    private String createUserTable = "create table " + User.TBL_USER + "(" + User.USER_ID + " integer primary key autoincrement," + User.USER_NAME + " text," + User.USER_EMAIL + " text," + User.USER_PASSWORD + " text)";
    private String createAppointmentTable = "create table " + Appointment.TBL_APPOINTMENT + "(" + Appointment._ID + " integer primary key autoincrement," + Appointment.TITLE + " text," + Appointment.DESCRIPTION + " text," + Appointment.TIME + " text," + Appointment.REMINDMEBEFORE + " text," + Appointment.PARTICIPANTS + " text)";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createUserTable);
        sqLiteDatabase.execSQL(createAppointmentTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
