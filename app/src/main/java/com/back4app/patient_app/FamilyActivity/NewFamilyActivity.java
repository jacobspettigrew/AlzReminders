package com.back4app.patient_app.FamilyActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.back4app.patient_app.R;
import com.back4app.patient_app.models.Family;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;


import static com.back4app.patient_app.FamilyActivity.FamilyActivity.NEW_Family_ACTIVITY_REQUEST_CODE;
import static java.net.URI.*;

public class NewFamilyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECTED_PHOTO = 5;
    private static final int FAMILYLIST = 10;

    public static final String EXTRA_REPLY_Family = "com.example.android.patientApp.REPLY";
    private static final String TAG = "NewFamilyActivity";
    public static final String CHANNEL_ID1 = "CHANNEL 1";

    ImageView imageGallery;
    EditText mEditNameView;
    EditText mEditRelationship;
    EditText mEditDescription;
    String url;
    int mId;
    TimePicker mTimePicker;
    private AlarmManager alarmManager;
    private int hour, minute;
    private long interval;
    Button mSaveProfileBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        imageGallery= findViewById(R.id.image_gallery);
        mEditNameView = findViewById(R.id.profile_name);
        mEditRelationship = findViewById(R.id.profile_relationship);
        mEditDescription = findViewById(R.id.profile_description);
        Intent intent = getIntent();

            //this code will be executed on devices running ICS or later
        if(intent.getStringExtra("mode").equals("edit")){
            Family family = intent.getParcelableExtra("family");
            mEditNameView.setText(family.getName());
            mEditRelationship.setText(family.getRelationship());
            mEditDescription.setText(family.getDescription());
            mId = family.getId();

            try {
                Uri imageUri = Uri.parse(family.getImage_url());
                InputStream inputStream = null;
                assert imageUri != null;
                inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                selectedImage = Bitmap.createScaledBitmap(selectedImage, 400,400,false);

                imageGallery.setImageBitmap(selectedImage);

                url = imageUri.toString();
            }

            catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }

        Button mSaveProfileBtn = findViewById(R.id.save_profile);
        mSaveProfileBtn.setOnClickListener(this);

        //Notification
        createNotificationChannel();
        mTimePicker=findViewById(R.id.tp_timepicker);
    }

    public void register(View view) {
    createNotificationChannel();
        Intent intent = new Intent(this, ReminderBroadcast.class);

        String  name = mEditNameView.getText().toString();
        String descrption = mEditDescription.getText().toString();
        intent.putExtra("name", name);
        intent.putExtra("description", descrption);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG, "register: " + name + descrption );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = mTimePicker.getHour();
            minute = mTimePicker.getMinute();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , interval , pendingIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void unregister(View view) {
        try{
        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID1, "new channel1", importance);
            channel.setDescription("some Descriptions here");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void getCamera(View view){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/");
            startActivityForResult(intent, SELECTED_PHOTO);
    }

    public void goToListActivity(String mode){
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mEditNameView.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
        }
        else if(mode.equals("edit")){
            String name = mEditNameView.getText().toString();
            String relationship = mEditRelationship.getText().toString();
            String description = mEditDescription.getText().toString();

            Family family = new Family(mId, name,relationship,description,url ) ;
            replyIntent.putExtra(EXTRA_REPLY_Family, family);

            setResult(2, replyIntent);
        }

        else {
            String name = mEditNameView.getText().toString();
            String relationship = mEditRelationship.getText().toString();
            String description = mEditDescription.getText().toString();

            Family family = new Family(name,relationship,description,url ) ;
            replyIntent.putExtra(EXTRA_REPLY_Family, family);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode);
        if(requestCode == SELECTED_PHOTO){
            try {
                Uri imageUri = data.getData();
                InputStream inputStream = null;
                assert imageUri != null;
                inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                selectedImage = Bitmap.createScaledBitmap(selectedImage, 400,400,false);
                imageGallery.setImageBitmap(selectedImage);
                url = imageUri.toString();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");

        if(mode.equals("add")){
            Log.d(TAG, "onClick: " + "from add task");
            goToListActivity("add");
        }
        else if(mode.equals("edit")){
            goToListActivity("edit");
        }
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_hour:
                if (checked)
                    interval = AlarmManager.INTERVAL_HOUR;
                Log.d(TAG, "onRadioButtonClicked: hour clicked");
                    break;
            case R.id.radio_half_day:
                if (checked)
                    interval = AlarmManager.INTERVAL_HALF_DAY;
                Log.d(TAG, "onRadioButtonClicked: half day");
                    break;
            case R.id.radio_day:
                if (checked)
                    interval = AlarmManager.INTERVAL_DAY;
                Log.d(TAG, "onRadioButtonClicked: day");
                    break;

        }
    }


    

}
