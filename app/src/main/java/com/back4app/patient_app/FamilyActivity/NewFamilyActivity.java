package com.back4app.patient_app.FamilyActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.back4app.patient_app.R;
import com.back4app.patient_app.models.Family;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.back4app.patient_app.FamilyActivity.FamilyActivity.NEW_Family_ACTIVITY_REQUEST_CODE;

public class NewFamilyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECTED_PHOTO = 5;
    private static final int FAMILYLIST = 10;

    public static final String EXTRA_REPLY_Family = "com.example.android.patientApp.REPLY";
    private static final String TAG = "NewFamilyActivity";

    ImageView imageGallery;
    EditText mEditNameView;
    EditText mEditRelationship;
    EditText mEditDescription;
    String url;
    int mId;

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

//        Log.d(TAG, "onCreate: " + family.toString());

        if(intent.getStringExtra("mode").equals("edit")){
            Family family = intent.getParcelableExtra("family");
            mEditNameView.setText(family.getName());
            mEditRelationship.setText(family.getRelationship());
            mEditDescription.setText(family.getDescription());
            mId = family.getId();
        }

        Button mSaveProfileBtn = findViewById(R.id.save_profile);
        mSaveProfileBtn.setOnClickListener(this);


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
//        Log.d(TAG, "onCreate: " + family.toString());
    }
}
