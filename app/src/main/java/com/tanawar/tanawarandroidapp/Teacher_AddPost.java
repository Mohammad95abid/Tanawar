package com.tanawar.tanawarandroidapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Calendar;

import apis.FirebaseConstants;
import apis.PostAPI;
import interfaces.InitializeComponents;

public class Teacher_AddPost extends AppCompatActivity implements InitializeComponents {
    // help variables
    private static final int PICK_IMAGE = 100;
    private Uri imageURI;
    private String email;
    private String display_name;
    // Progress Dialog object
    private ProgressDialog progressDialog;
    // layout components
    private EditText edtTitle;
    private EditText edtdescription;
    private Spinner spnCateogry;
    private Spinner spnLanguages;
    private EditText edtDataFileURL;
    private ImageView img;
    private ImageView chooseImgIcon;
    private Button btnPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__add_posts);
        getDataFromIntent();
        initializeActivityComponents();
        initializeComponentsListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageURI = data.getData();
            img.setImageURI(imageURI);
        }
    }
    @Override
    public void initializeActivityComponents() {
        edtTitle = findViewById(R.id.edtPostTitle);
        edtdescription = findViewById(R.id.edtPostdescription);
        spnCateogry = findViewById(R.id.spnPostCategory);
        spnLanguages = findViewById(R.id.spnPostLanguage);
        edtDataFileURL = findViewById(R.id.edtPostFileURL);
        img = findViewById(R.id.postImage);
        chooseImgIcon = findViewById(R.id.chooseImgIcon);
        btnPost = findViewById(R.id.btnPost);
        initSpinners();
    }

    @Override
    public void initializeComponentsListeners() {
        chooseImgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize Progress Dialog
                progressDialog = new ProgressDialog(Teacher_AddPost.this);
                // Show Dialog
                progressDialog.show();
                // Set Content View
                progressDialog.setContentView(R.layout.progress_dialog);
                //Set Transparent Background
                progressDialog.getWindow()
                        .setBackgroundDrawableResource(android.R.color.transparent);
                // Upload Image to storage, then create post
                if(imageURI == null){
                    uploadPost("");
                }else{
                    uploadImageToStorage();
                }
            }
        });
    }

    private void uploadImageToStorage() {
        String imageName = display_name+"_"+System.currentTimeMillis()+"."+getExtension(imageURI);
        PostAPI.getInstance().uploadPostImage(imageName,imageURI,this);
    }

    private String getExtension(Uri imageURI) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(imageURI));
    }
    /**
     * A method to get data from intent.
     */
    private void getDataFromIntent() {
        Intent i = getIntent();
        email = i.getStringExtra(FirebaseConstants.Collections.Users.EMAIL);
        display_name = i.getStringExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME);
    }
    /**
     * A method to upload post to the firestore.
     */
    public void uploadPost(String imageDownloadURL) {
        String title = edtTitle.getText().toString();
        String description = edtdescription.getText().toString();
        String fileURL = edtDataFileURL.getText().toString();
        String category = spnCateogry.getSelectedItem().toString();
        String language = spnLanguages.getSelectedItem().toString();
        // the image url saved in imageURI, should upload the image to the storage and get url link
        // as string before upload the post, found in imageDownloadURL
        // for the datetime as timestamp
        Calendar c = Calendar.getInstance();
        Timestamp timestamp = new Timestamp(c.getTimeInMillis());
        // email and display name for the owner (teacher),
        // found in email and display_name variables.
        PostAPI.getInstance().addPost(title,language,category,imageDownloadURL,description,
                timestamp,fileURL,display_name,email,this);
    }

    /**
     * A method to open the device gallery to choose an image.
     */
    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i,PICK_IMAGE);
    }

    private void initSpinners() {
        // Categories Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spnCateogry.setAdapter(adapter);
        // Languages Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spnLanguages.setAdapter(adapter1);
    }

    /**
     * A method called when the post uploaded successfully to clear all the fields.
     */
    public void clearForum() {
        edtDataFileURL.setText("");
        edtdescription.setText("");
        edtTitle.setText("");
        spnLanguages.setSelection(0);
        spnCateogry.setSelection(0);
        img.setImageResource(android.R.color.transparent);
        // dismiss the progress dialog.
        progressDialog.dismiss();
    }
}
