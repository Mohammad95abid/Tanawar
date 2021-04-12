package com.tanawar.tanawarandroidapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import adapters.ChatMessageAdapter;
import adapters.MyDiaryAdapter;
import apis.ChatAPI;
import apis.FirebaseConstants;
import apis.PrivateLessonsAPI;
import apis.TeacherConnectionAPI;
import apis.UserAPI;
import interfaces.InitializeComponents;
import tanawar_objects.ChatItem;
import tanawar_objects.PrivateLessonsDiaryItem;
import utils.AccountType;

public class ChatActivity extends AppCompatActivity implements InitializeComponents {
    private RecyclerView recyclerView;
    private EditText messageEDT;
    private TextView recipientName;
    private ImageView sendIC;
    private String teacherName;
    private String teacherEmail;
    private String studentEmail;
    private String studentName;
    private ChatMessageAdapter adapter;
    private ArrayList<ChatItem> data;
    private String teacher_em;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getDateFromIntent();
        initializeActivityComponents();
        initializeComponentsListeners();
    }



    private void getDateFromIntent() {
        Intent i =getIntent();
        teacherName = i.getStringExtra(FirebaseConstants.Collections.PrivateLesson.TEACHER_NAME);
        teacher_em = i.getStringExtra(FirebaseConstants.Collections.PrivateLesson.TEACHER_EMAIL);
        teacherEmail  = teacher_em ;
        studentName = i.getStringExtra(FirebaseConstants.Collections.PrivateLesson.STUDENT_NAME);
        studentEmail = i.getStringExtra(FirebaseConstants.Collections.PrivateLesson.STUDENT_EMAIL);
    }

    @Override
    public void initializeComponentsListeners() {
        sendIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess = messageEDT.getText().toString();
                if(mess == null || mess.isEmpty())
                    return;
                TeacherConnectionAPI.getInstance().connectWith(teacher_em, teacherName,
                        studentEmail, studentName);
                String sender = "";
                String recipient = "";
                if(Homepage.myEmail!=null && Homepage.myEmail.equals(teacherEmail)){
                    // the sender is teacher
                    sender = teacherEmail; // teacherEmail
                    recipient = studentEmail;
                    ChatAPI.getInstance().createConnection(teacherEmail, teacherName,
                            studentEmail,studentName);
                }else if(Homepage.myEmail!=null && Homepage.myEmail.equals(studentEmail)){
                    // the sender is student
                    sender = studentEmail;
                    recipient = teacherEmail; // teacherEmail
                    ChatAPI.getInstance().createConnection(studentEmail,studentName,
                            teacherEmail, teacherName);
                }
                ChatAPI.getInstance().sendMessage(sender, recipient, mess,
                        ChatActivity.this);
                messageEDT.setText("");
            }
        });

    }

    @Override
    public void initializeActivityComponents() {
        recyclerView = findViewById(R.id.chatBody_chatActivity);
        messageEDT = findViewById(R.id.messageEDT_ChatActivity);
        sendIC = findViewById(R.id.sendIC);
        recipientName = findViewById(R.id.recipientName);
        loadDataIntoRecycleView();
        setRecipientName();
    }

    public void addDBItemToList(ChatItem item) {
        this.data.add(item);
        Collections.sort(this.data, new Comparator<ChatItem>() {
            @Override
            public int compare(ChatItem o1, ChatItem o2) {
                return o1.getTime().getTime().compareTo(o2.getTime().getTime());
            }
        });
    }

    public void notifyDataSetChanged() {
        this.adapter.notifyDataSetChanged();
        this.recyclerView.scrollToPosition(1);
    }

    private void loadDataIntoRecycleView() {
        data = new ArrayList<>();
        adapter = new ChatMessageAdapter(getApplicationContext(), data );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ChatAPI.getInstance().loadAllChatMessages(teacherEmail, studentEmail, this);
        ChatAPI.getInstance().loadAllChatMessages(studentEmail, teacherEmail,this);
    }
    /**
     * a method to set the second chat participant.
     */
    private void setRecipientName(){
        if(Homepage.userType == AccountType.TEACHER){
            recipientName.setText(studentName);
        }else if(Homepage.userType == AccountType.STUDENT){
            if(Homepage.myEmail.equals(teacherEmail)){
                recipientName.setText(studentName);
            }else{
                recipientName.setText(teacherName);
            }
        }
    }

    public void updateRecipientName(String username) {
        recipientName.setText(username);
    }
}
