package com.tanawar.tanawarandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class ShowPostImageInFullScreen extends AppCompatActivity {
    private PhotoView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post_image_in_full_screen);
        img = findViewById(R.id.img_in_fullscreen_activity);
        Intent i =getIntent();
        String imgURL = i.getStringExtra("IMG_URL");
        Glide.with(getApplicationContext()).load(imgURL).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
