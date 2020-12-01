/* HW 1

        Course: CMPT 140 Introduction to Computing Science and Programming I
        Instructor: Dr. Herbert H. Tsang
        Description: <Give a brief description for homework 1>
        Due date: < DATE HERE, YOU CAN USE YYYY/MM/DD format>

        Author: < NAME >
        Input: < IF ANY>
        Output: < IF ANY >
        I pledge that I have completed the programming assignment independently.
        I have not copied the code from a student or any source.
        I have not given my code to any student.

        Sign here: __<PUT DOWN YOUR NAME HERE>______

*/
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

        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");

        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();    // AVD 확인용
        Log.e("Alarm",name + description);    // 로그 확인용

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID1)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(name)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0,builder.build());
    }
}


