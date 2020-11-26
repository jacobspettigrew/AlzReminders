package com.back4app.patient_app.FamilyActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.back4app.patient_app.R;
import com.back4app.patient_app.models.Family;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewFamilyActivity extends AppCompatActivity {

    private static final int SELECTED_PHOTO = 5;
    private static final int FAMILYLIST = 10;

    public static final String EXTRA_REPLY_Family = "com.example.android.patientApp.REPLY";

    ImageView imageGallery;
    EditText mEditNameView;
    EditText mEditRelationship;
    EditText mEditDescription;
    String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        imageGallery= findViewById(R.id.image_gallery);
        mEditNameView = findViewById(R.id.profile_name);
        mEditRelationship = findViewById(R.id.profile_relationship);
        mEditDescription = findViewById(R.id.profile_description);


    }

    public void getCamera(View view){

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/");
            startActivityForResult(intent, SELECTED_PHOTO);
    }

    public void goToListActivity(View view){
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(mEditNameView.getText())) {
            setResult(RESULT_CANCELED, replyIntent);

        } else {
            String name = mEditNameView.getText().toString();
            String relationship = mEditRelationship.getText().toString();
            String description = mEditDescription.getText().toString();

            Family family = new Family(name,relationship,description,url  ) ;
            replyIntent.putExtra(EXTRA_REPLY_Family, family);
            setResult(RESULT_OK, replyIntent);
        }
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECTED_PHOTO){
            try {
                Uri imageUri = data.getData();
                InputStream inputStream = null;
                assert imageUri != null;
                inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                selectedImage = Bitmap.createScaledBitmap(selectedImage, 400,400,false);

                imageGallery.setImageBitmap(selectedImage);
                Log.d("TAG", "onActivityResult: " + imageUri);
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
}
