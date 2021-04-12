package com.tanawar.tanawarandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class ShowVideoInFullScreen extends AppCompatActivity {
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video_in_full_screen);
        videoView = findViewById(R.id.videoview_in_fullscreen);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });
        Intent i = getIntent();
        String vidURL = i.getStringExtra("Video_URL");
        showVideo(vidURL);
    }
    private void showVideo(String videoURL) {
        Uri videoUri = Uri.parse(videoURL);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        MediaController mediaController = new MediaController(videoView.getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
