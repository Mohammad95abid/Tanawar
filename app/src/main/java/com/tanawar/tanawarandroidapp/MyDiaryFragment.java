package com.tanawar.tanawarandroidapp;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.MyDiaryAdapter;
import adapters.PostsTabAdapter;
import tanawar_objects.Post;
import tanawar_objects.PrivateLessonsDiaryItem;

public class MyDiaryFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyDiaryAdapter myDiaryAdapter;
    private ArrayList<PrivateLessonsDiaryItem> lessons;
    private Context context;
    private View myView;
    private Button history;
    private Button future;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_my_diary, container,false);
        myView = v;
        this.recyclerView = myView.findViewById(R.id.lessonsItemsRCV);
        lessons = new ArrayList<>();
        // find view by id
        this.history = myView.findViewById(R.id.private_lessons_history);
        this.future = myView.findViewById(R.id.private_lessons_future);

        //
        addListeners();

        //
        myDiaryAdapter = new MyDiaryAdapter(context,lessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(myDiaryAdapter);
        initFutureLessons();
        return v;
    }

    private void addListeners() {
        this.future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initHistoryLessons();
            }
        });
        this.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFutureLessons();
            }
        });
    }

    /**
     * a method to initialize the future lessons in the recycle view.
     */
    private void initFutureLessons() {
        Toast.makeText(context,"Future Lessons", Toast.LENGTH_LONG).show();
    }
    /**
     * a method to initialize the lessons from history in the recycle view.
     */
    private void initHistoryLessons() {
        Toast.makeText(context,"History Lessons", Toast.LENGTH_LONG).show();
    }
}
