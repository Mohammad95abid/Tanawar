package com.tanawar.tanawarandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import apis.FirebaseConstants;
import interfaces.InitializeComponents;
public class TeacherProfile extends AppCompatActivity implements InitializeComponents {
    // TeacherOnMap data from intent (Homepage Activity).
    String display_name;
    String email;
    String phone;
    String profile_image_URL;
    //
    private Button my_posts;
    private Button my_diary;
    private Button share_on_students_map;
    private ImageView profileIMG;
    private ImageView settingsIcon;
    private TextView txtEmail;
    private TextView txtPhone;
    private TextView txtAddress;
    private TextView txtWebsite;
    private TextView txtTeacherName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);
        getDataFromIntent();
        initializeActivityComponents();
        initializeComponentsListeners();
    }
    /**
     *
     */
    private void getDataFromIntent() {
        Intent i = getIntent();
        display_name = i.getStringExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME);
        email = i.getStringExtra(FirebaseConstants.Collections.Users.EMAIL);
        phone = i.getStringExtra(FirebaseConstants.Collections.Users.PHONE);
        profile_image_URL = i.getStringExtra(FirebaseConstants.Collections.Users.PROFILE_IMAGE_URI);
    }
    /**
     *
     * @param profile_image_url
     */
    private void setProfileImageUsingURLAsString(String profile_image_url) {
        Uri uri;
        if (profile_image_URL != null) {
            uri = Uri.parse(profile_image_url);
            Picasso.get().load(uri).into(profileIMG);
        }
    }
    /**
     *
     */
    @Override
    public void initializeComponentsListeners() {
        my_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherProfile.this,Teacher_MyPosts.class);
                i.putExtra(FirebaseConstants.Collections.Users.EMAIL,email);
                i.putExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME,display_name);
                startActivity(i);
            }
        });
        my_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherProfile.this, MyDiary.class);
                i.putExtra(FirebaseConstants.Collections.Users.EMAIL,email);
                i.putExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME,display_name);
                startActivity(i);
            }
        });
        share_on_students_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherProfile.this,
                        ShareTeacherProfileOnMap.class);
                startActivity(i);
            }
        });
    }
    /**
     *
     */
    @Override
    public void initializeActivityComponents() {
        my_posts = findViewById(R.id.my_posts);
        my_diary = findViewById(R.id.my_diary);
        profileIMG = findViewById(R.id.teacher_profile_image);
        setProfileImageUsingURLAsString(profile_image_URL);
        settingsIcon = findViewById(R.id.settingsIcon);
        txtEmail = findViewById(R.id.teacher_profile_email);
        txtEmail.setText(email);
        txtAddress = findViewById(R.id.teacher_profile_address);
        txtAddress.setText("Sheikh Danun");
        txtPhone = findViewById(R.id.teacher_profile_phone);
        txtPhone.setText(phone);
        txtWebsite = findViewById(R.id.teacher_profile_website);
        txtWebsite.setText("tanawar.com");
        txtTeacherName = findViewById(R.id.teacher_name);
        txtTeacherName.setText(display_name);
        share_on_students_map = findViewById(R.id.share_on_students_map);
    }
}
