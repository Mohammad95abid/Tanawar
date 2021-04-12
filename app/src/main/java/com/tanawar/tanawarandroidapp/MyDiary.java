package com.tanawar.tanawarandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import adapters.MyDiaryAdapter;
import apis.FirebaseConstants;
import apis.PrivateLessonsAPI;
import interfaces.InitializeComponents;
import tanawar_objects.PrivateLessonsDiaryItem;

public class MyDiary extends AppCompatActivity implements InitializeComponents {
    private RecyclerView recyclerView;
    private MyDiaryAdapter adapter;
    private FloatingActionButton addIcon;
    private String teacherEmail;
    private String teacherName;
    private ArrayList<PrivateLessonsDiaryItem> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary2);
        initializeActivityComponents();
        initializeComponentsListeners();
        getDataFromIntent();
        loadDataIntoRecycleView();
    }

    private void getDataFromIntent() {
        Intent i = getIntent();
        teacherEmail = i.getStringExtra(FirebaseConstants.Collections.Users.EMAIL);
        teacherName = i.getStringExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME);
    }

    @Override
    public void initializeComponentsListeners() {
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddPrivateLesson.class);
                i.putExtra(FirebaseConstants.Collections.Users.EMAIL,teacherEmail);
                i.putExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME,teacherName);
                startActivityForResult(i, 1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                loadDataIntoRecycleView();
            }
        }
    }

    @Override
    public void initializeActivityComponents() {
        recyclerView = findViewById(R.id.recv);
        addIcon = findViewById(R.id.addIC);
    }

    private void loadDataIntoRecycleView() {
        data = new ArrayList<>();
        adapter = new MyDiaryAdapter(getApplicationContext(), data );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        PrivateLessonsAPI.getInstance().loadAllPrivateLessonByTeacherEmail(teacherEmail,
                this);
    }

    public void addDBItemToList(PrivateLessonsDiaryItem item) {
        this.data.add(item);
    }

    public void notifyDataSetChanged() {
        this.adapter.notifyDataSetChanged();
        this.recyclerView.scrollToPosition(1);
    }
}
