package com.tanawar.tanawarandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import adapters.MyDiaryAdapter;
import apis.FirebaseConstants;
import apis.GoogleMapAPI;
import apis.PrivateLessonsAPI;
import interfaces.InitializeComponents;
import tanawar_objects.PrivateLessonsDiaryItem;

public class StudentProfile extends AppCompatActivity implements InitializeComponents {
    private TextView stdName;
    private TextView stdEmail;
    private TextView my_request_for_help;
    private RecyclerView recyclerView;
    private Button askHelp;
    private MyDiaryAdapter adapter;
    private ArrayList<PrivateLessonsDiaryItem> data;
    private String teacherEmail;
    private String teacherName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        initializeActivityComponents();
        initializeComponentsListeners();
    }
    /**
     *
     */
    private void getDataFromIntent() {
        Intent i = getIntent();
        stdName.setText(i.getStringExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME));
        stdEmail.setText(i.getStringExtra(FirebaseConstants.Collections.Users.EMAIL));

    }
    @Override
    public void initializeComponentsListeners() {
        askHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentProfile.this, AddAskHelpOnMap.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void initializeActivityComponents() {
        stdName = findViewById(R.id.studentName_StudentProfile);
        stdEmail = findViewById(R.id.studentEmail_StudentProfile);
        recyclerView = findViewById(R.id.recycle_view_studentProfile);
        askHelp = findViewById(R.id.btnAskHelp_StudentProfile);
        my_request_for_help = findViewById(R.id.my_request_for_help);
        getDataFromIntent();
        loadDataIntoRecycleView();
        loadHelpRequest();
    }

    private void loadHelpRequest() {
        GoogleMapAPI.getInstance().getHelpRequest(Homepage.myEmail, my_request_for_help);
    }

    @Override
    protected void onResume() {
        loadHelpRequest();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        loadHelpRequest();
        super.onRestart();
    }

    private void loadDataIntoRecycleView() {
        data = new ArrayList<>();
        adapter = new MyDiaryAdapter(getApplicationContext(), data );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        PrivateLessonsAPI.getInstance().loadAllPrivateLessonByStudentEmail(
                stdEmail.getText().toString(), this);
    }

    public void addDBItemToList(PrivateLessonsDiaryItem item) {
        teacherEmail = item.getTeacherEmail();
        teacherName = item.getTeacherName();
        this.data.add(item);
    }

    public void notifyDataSetChanged() {
        this.adapter.notifyDataSetChanged();
        this.recyclerView.scrollToPosition(1);
    }
}
