package com.example.sudhaker_ftc_test.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


import com.example.sudhaker_ftc_test.AppointmentListActivity;
import com.example.sudhaker_ftc_test.R;
import com.example.sudhaker_ftc_test.database.App_DataSource;
/**
 * Created by sudhaker on 1/7/2018.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        sendNotification(context, intent);
    }


    public void sendNotification(Context context, Intent intent1) {

//  1      get all data from intent which you have put on alarm creation time
        String title = "Notification From FTC";
        String des = "Notification From FTC ";
        String time = "";
        if (intent1.getExtras() != null) {
            title = intent1.getExtras().getString("title");
            des = intent1.getExtras().getString("des");
            time = intent1.getExtras().getString("time");
        }
        if (!title.equals("Notification From FTC") && !des.equals("Notification From FTC") && !time.equals("")) {
            App_DataSource app_dataSource = App_DataSource.getAppDataSource(context);
            app_dataSource.openDb();
            int count = app_dataSource.getCountOfAppointmentsByTimeTitleAndDes(time, title, des);
            app_dataSource.closeDb();
            if (count > 0) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(des);
                Intent intent = new Intent(context, AppointmentListActivity.class);
// 2    now put all data of 1 in this intent
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                mBuilder.setContentIntent(pendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(001, mBuilder.build());
            }
        }

    }
}
