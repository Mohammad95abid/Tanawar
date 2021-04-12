package com.tanawar.tanawarandroidapp.tabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import adapters.PostsTabAdapter;
import apis.PostAPI;
import tanawar_objects.Post;
import com.tanawar.tanawarandroidapp.R;

public class TanawarPostsTab extends Fragment {
    private RecyclerView recyclerView;
    private PostsTabAdapter postsTabAdapter;
    private ArrayList<Post> posts;
    private Context context;
    private View myView;
    private Spinner categories;
    private Spinner languages;
    private String category="";
    private String language="";
    private ImageView filterIcon;
    private ImageView searchIcon;
    private LinearLayout filterLayout;
    private boolean filterIconSelected=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tanawar_posts_tab, container,false);
        myView = v;
        this.recyclerView = myView.findViewById(R.id.post_recycle_view);
        posts = new ArrayList<>();
        // find view by id
        this.languages = myView.findViewById(R.id.language_spinner);
        this.categories = myView.findViewById(R.id.category_spinner);
        this.filterIcon = myView.findViewById(R.id.filter_Icon);
        this.searchIcon = myView.findViewById(R.id.search_Icon);
        this.filterLayout = myView.findViewById(R.id.filter_layout);
        //
        addListenerstoImageview();
        initSpinners();
        hideFilterLayout();
        //
        postsTabAdapter = new PostsTabAdapter(context,posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(postsTabAdapter);
        load_posts();
        return v;
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
                context,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.categories.setAdapter(adapter);
        // Languages Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                context,
                R.array.languages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.languages.setAdapter(adapter1);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    private void addListenerstoImageview() {
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(context,
                        R.anim.button_click_effect);
                filterIcon.startAnimation(animation);
                if(filterIconSelected){
                    hideFilterLayout();
                    filterIcon.setImageResource(R.drawable.filter_black);
                }else{
                    showFilterLayout();
                    filterIcon.setImageResource(R.drawable.filter_blue);
                    searchIcon.setImageResource(R.drawable.search_blue);
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
        PostAPI.getInstance().getAllPostsOrderByPublishedTime(
                category,language,this);
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
