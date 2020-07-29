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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
        ivAvatarPreview = findViewById(R.id.welcome_icon);
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
                // launchCamera();

                PopupMenu popup = new PopupMenu(EditProfileActivity.this, btnEditAvatar);
                popup.getMenuInflater().inflate(R.menu.profile_pic_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Take Photo")) {
                            launchCamera();
                        } else if (menuItem.getTitle().equals("Remove Current Photo")) {
                            removePic();
                        }
                        return true;
                    }
                });

                popup.show();

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

    // load in current user's fields into views
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

    // checks if all fields are valid before saving
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

    // remove picture
    public void removePic() {
        String path = getApplication().getFilesDir().getAbsolutePath();
        path += "/assets/default_avatar.png";
        Log.i(TAG, path);

        File imgFile = new File(path);

        if (imgFile.exists()) {
            Log.i(TAG, "exists!");
            ivAvatarPreview.setImageURI(Uri.fromFile(imgFile));
            updatedPfp = true;
        } else {
            Log.i(TAG, "error in loading in file");
        }
    }

    // resizes picture
    private File resizePicture(File file, int width) {
        // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
        Uri photoUri = Uri.fromFile(file);
        Bitmap rawTakenImage = BitmapFactory.decodeFile(photoUri.getPath());
        Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, width);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
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

    // save profile info
    private void saveProfile() {
        // updated pfp?
        if (updatedPfp) {
            File resizedFile = resizePicture(getPhotoFileUri(photoFileName), 300);
            currentUser.put("profilePic", new ParseFile(resizedFile));
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

    // shows user a message
    private void makeMessage(String message) {
        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}