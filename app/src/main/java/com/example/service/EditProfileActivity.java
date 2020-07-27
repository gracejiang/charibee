package com.example.service;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.service.functions.BitmapScaler;
import com.example.service.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileActivity";
    private ParseUser currentUser;
    private User user;

    // ui views
    private ImageView ivAvatarPreview;
    private Button btnEditAvatar;
    private EditText etUsername;
    private EditText etBio;
    private Button btnCancel;
    private Button btnSave;

    // camera variables
    private boolean updatedPfp = false;
    private File photoFile;
    private String photoFileName;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        currentUser = ParseUser.getCurrentUser();
        user = new User(currentUser);
        photoFileName = currentUser.getUsername() + "_avatar.jpg";

        // bind ui views
        ivAvatarPreview = findViewById(R.id.edit_profile_avatar_preview);
        btnEditAvatar = findViewById(R.id.edit_profile_update_avatar_btn);
        etUsername = findViewById(R.id.edit_profile_username);
        etBio = findViewById(R.id.edit_profile_bio);
        btnCancel = findViewById(R.id.edit_profile_cancel_btn);
        btnSave = findViewById(R.id.edit_profile_save_btn);

        // load in views
        loadInViews();

        // upload new profile pic clicked
        btnEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        // cancel button clicked
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goProfileFragment();
            }
        });

        // save button clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validFields()) {
                    saveProfile();
                    goProfileFragment();
                }
            }
        });
    }

    private void loadInViews() {
        // load in avatar preview
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseFile profilePic = (ParseFile) currentUser.get("profilePic");
        if (profilePic != null) {
            Glide.with(this)
                    .load(httpToHttps(profilePic.getUrl()))
                    .circleCrop()
                    .into(ivAvatarPreview);
        } else {
            Log.e(TAG, "couldn't load profile pic");
        }

        // load in text views
        etUsername.setText(currentUser.getUsername());
        String bio = user.getBio();

        if (bio.length() > 0) {
            etBio.setText(bio);
        }
    }

    private boolean validFields() {
        if (etUsername.getText().length() == 0) {
            makeMessage("Please enter in a valid username.");
            return false;
        } else if (etBio.getText().length() > 120) {
            makeMessage("Your bio cannot exceed 120 characters.");
            return false;
        }
        return true;
    }

    // CAMERA ACTIVITY
    // https://guides.codepath.org/android/Accessing-the-Camera-and-stored-media

    // launch camera application to take pic
    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(EditProfileActivity.this, "com.codepath.service.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // get safe storage directory for photos
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    // resizes picture
    private File resizePicture(int width) {
        Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
        // by this point we have the camera photo on disk
        Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
        // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, width);
        // Configure byte output stream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // Compress the image further
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
        File resizedFile = getPhotoFileUri(photoFileName + "_resized");
        try {
            resizedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(resizedFile);
            fos.write(bytes.toByteArray());
            fos.close();
            return resizedFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // SAVE RESULTS FOR NEW UPDATED AVATAR
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // take picture
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivAvatarPreview.setImageBitmap(takenImage);
                updatedPfp = true;
            }
        }
    }

    // save profile info
    private void saveProfile() {

        // updated pfp?
        if (updatedPfp) {
            File resizedPicture = resizePicture(300);
            currentUser.put("profilePic", new ParseFile(resizedPicture));
        }

        currentUser.setUsername(etUsername.getText().toString());
        user.setBio(etBio.getText().toString());

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    // error while posting pic
                    Log.e(TAG, "error updating your profile", e);
                    return;
                } else {
                    Log.i(TAG, "profile successfully updated!");
                }
            }
        });
    }

    // return to profile fragment
    private void goProfileFragment() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        // doesn't work, but trying to set fragment to profile fragment
//        try {
//            // look up .getSupportFragmentManager().popBackStackImmediate(name); later
//            FragmentManager fm = new MainActivity().getSupportFragmentManager();
//            Fragment fragment = new ProfileFragment();
//            fm.beginTransaction().replace(R.id.main_frame_layout, fragment).commit();
//        } catch (IllegalStateException e) { }

        finish();
    }

    // converts http link to https
    private String httpToHttps(String url) {
        if (url.contains("https")) {
            return url;
        }

        String httpsUrl = "https" + url.substring(4);
        return httpsUrl;
    }

    // checks if an email is valid
    private static boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    // shows user a message
    private void makeMessage(String message) {
        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}