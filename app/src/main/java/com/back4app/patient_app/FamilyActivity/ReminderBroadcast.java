package com.back4app.patient_app.FamilyActivity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.back4app.patient_app.R;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.back4app.patient_app.FamilyActivity.NewFamilyActivity.CHANNEL_ID1;

public class ReminderBroadcast extends BroadcastReceiver {

    private static final String TAG = "ReminderBroadCast" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Notification
        Toast.makeText(context, "알람~!!", Toast.LENGTH_SHORT).show();    // AVD 확인용
        Log.e("Alarm","알람입니다.");    // 로그 확인용

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID1)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("textTitle")
                .setContentText("str")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0,builder.build());
    }
}


