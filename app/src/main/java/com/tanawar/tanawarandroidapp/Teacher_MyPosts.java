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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import adapters.PostsTabAdapter;
import adapters.VideosTabAdapter;
import apis.FirebaseConstants;
import apis.PostAPI;
import interfaces.InitializeComponents;
import tanawar_objects.Post;
import tanawar_objects.VideoPost;

public class Teacher_MyPosts extends AppCompatActivity implements InitializeComponents {
    // TeacherOnMap data from intent (Homepage Activity).
    String email;
    String display_name;
    //
    private ImageView addPost;
    // using for the loading posts from firestore
    private RecyclerView recyclerView;
    private PostsTabAdapter postsTabAdapter;
    private ArrayList<Post> posts;
    private Spinner categories;
    private Spinner languages;
    private String category="";
    private String language="";
    private ImageView filterIcon;
    private ImageView searchIcon;
    private LinearLayout filterLayout;
    private boolean filterIconSelected=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__my_posts);
        getDataFromIntent();
        initializeActivityComponents();
        initializeComponentsListeners();
    }
    /**
     *
     */
    private void getDataFromIntent() {
        Intent i = getIntent();
        email = i.getStringExtra(FirebaseConstants.Collections.Users.EMAIL);
        display_name = i.getStringExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME);
    }
    private void showFilterLayout() {
        filterIconSelected = true;
        filterLayout.setVisibility(LinearLayout.VISIBLE);
    }

    private void hideFilterLayout() {
        filterIconSelected = false;
        filterLayout.setVisibility(LinearLayout.GONE);
        searchIcon.setImageResource(R.drawable.search_black);
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
        this.categories.setAdapter(adapter);
        // Languages Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.languages.setAdapter(adapter1);
    }
    @Override
    public void initializeComponentsListeners() {
            addPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Teacher_MyPosts.this,
                            Teacher_AddPost.class);
                    i.putExtra(FirebaseConstants.Collections.Users.EMAIL,email);
                    i.putExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME,display_name);
                    startActivity(i);
                }
            });
    }

    @Override
    public void initializeActivityComponents() {
        addPost = findViewById(R.id.addPostIcon);
        //
        this.languages = findViewById(R.id.language_spinner);
        this.categories = findViewById(R.id.category_spinner);
        this.filterIcon = findViewById(R.id.filter_Icon);
        this.searchIcon = findViewById(R.id.search_Icon);
        this.filterLayout = findViewById(R.id.filter_layout);
        addListenerstoImageview();
        initSpinners();
        hideFilterLayout();
        posts = new ArrayList<>();
        this.recyclerView = findViewById(R.id.post_recycle_view);
        postsTabAdapter = new PostsTabAdapter(getApplicationContext(), posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(postsTabAdapter);
        load_posts();
    }
    private void addListenerstoImageview() {
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.button_click_effect);
                filterIcon.startAnimation(animation);
                if(filterIconSelected){
                    hideFilterLayout();
                    filterIcon.setImageResource(R.drawable.filter_black);
                    addPost.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);

                }else{
                    showFilterLayout();
                    filterIcon.setImageResource(R.drawable.filter_blue);
                    searchIcon.setImageResource(R.drawable.search_blue);
                    addPost.setImageResource(R.drawable.ic_add_circle_outline_blue_24dp);
                }
            }
        });
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFilterLayout();
                load_posts();
            }
        });
    }
    /**
     *
     */
    private void load_posts() {
        language = languages.getSelectedItem().toString();
        category = categories.getSelectedItem().toString();
        posts.clear();
        PostAPI.getInstance().getTeacherPostsOrderByPublishedTime(
                category,language,email,this);
    }
    /**
     *
     * @param p
     */
    public void addPostToList(Post p) {
        posts.add(p);
    }
    /**
     *
     */
    public void notifyDataSetChanged() {
        this.postsTabAdapter.notifyDataSetChanged();
        this.recyclerView.scrollToPosition(1);
    }
}
