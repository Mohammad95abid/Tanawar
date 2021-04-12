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

import com.tanawar.tanawarandroidapp.R;

import java.util.ArrayList;
import adapters.TanawarDBAdapter;
import apis.TanawarDBAPI;
import tanawar_objects.DBItem;

public class TanawarDatabaseTab extends Fragment {
    private RecyclerView recyclerView;
    private TanawarDBAdapter dbTabAdapter;
    private ArrayList<DBItem> items;
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
        View v = inflater.inflate(R.layout.tanawar_database_tab, container,false);
        myView = v;
        this.recyclerView = myView.findViewById(R.id.db_recycle_view);
        items = new ArrayList<>();
        // find view by id
        this.languages = myView.findViewById(R.id.db_language_spinner);
        this.categories = myView.findViewById(R.id.db_category_spinner);
        this.filterIcon = myView.findViewById(R.id.db_filter_Icon);
        this.searchIcon = myView.findViewById(R.id.db_search_Icon);
        this.filterLayout = myView.findViewById(R.id.db_filter_layout);
        //
        addListenerstoImageview();
        initSpinners();
        hideFilterLayout();
        //
        dbTabAdapter = new TanawarDBAdapter(context,items);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(dbTabAdapter);
        load_items();
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
                load_items();
            }
        });
    }
    /**
     *
     */
    private void load_items() {
        language = languages.getSelectedItem().toString();
        category = categories.getSelectedItem().toString();
        items.clear();
        TanawarDBAPI.getInstance().getAllDBItemsOrderByPublishedTime(
                category,language,this);
    }
    /**
     *
     * @param item
     */
    public void addDBItemToList(DBItem item) {
        items.add(item);
    }
    /**
     *
     */
    public void notifyDataSetChanged() {
        this.dbTabAdapter.notifyDataSetChanged();
        this.recyclerView.scrollToPosition(1);
    }
}
