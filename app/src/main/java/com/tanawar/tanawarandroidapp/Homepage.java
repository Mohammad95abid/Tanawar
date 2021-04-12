package com.tanawar.tanawarandroidapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import apis.FirebaseConstants.Collections.Users;
import interfaces.InitializeComponents;
import com.tanawar.tanawarandroidapp.tabs.TanawarDatabaseTab;
import com.tanawar.tanawarandroidapp.tabs.TanawarMabsTab;
import com.tanawar.tanawarandroidapp.tabs.TanawarPostsTab;
import adapters.TanawarTabsAdapter;
import utils.AccountType;
import utils.SharedPreferencesDB;
import com.tanawar.tanawarandroidapp.tabs.TeacherVideosTab;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class Homepage extends AppCompatActivity implements InitializeComponents,
        NavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton chatIC;
    private AppBarConfiguration mAppBarConfiguration;
    private String display_name;
    private String email;
    private String birthday;
    private String account_type;
    private String phone;
    private String profile_image_URL;
    // NavigationView
    private NavigationView navigationView;
    // navigation drawer header
    private ImageView profileIMGV;
    private TextView txtUserFullName;
    private TextView txtUserEmail;
    // navigation drawer menu
    private MenuItem logout;
    // tab layouts part
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private TanawarTabsAdapter tmtAdapter;
    // Google Map Part
    private LocationManager locationManager;
    private LocationListener locationListener;
    // Tabs part
    private TanawarMabsTab mapTab;
    private TanawarPostsTab postsTab;
    private TeacherVideosTab videosTab;
    private TanawarDatabaseTab friendsSuggestionTab;
    public static AccountType userType;
    public static String myEmail;
    public static String myName;
    private boolean is_changed_before = false;
    public static double my_address_lat = 31.779739;
    public static double my_address_long = 35.139914;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        try {
            getDataFromIntent();
            initializeActivityComponents();
            initializeComponentsListeners();
            updateProfileData();
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch (Exception e){
            Log.i("Test",e.getLocalizedMessage());
        }
    }

    /**
     *
     */
    private void updateProfileData() {
        txtUserFullName.setText(display_name);
        txtUserEmail.setText(email);
        setProfileImageUsingURLAsString(profile_image_URL, profileIMGV);
    }

    /**
     *
     * @param profile_image_url
     * @param profileIMGV
     */
    private void setProfileImageUsingURLAsString(String profile_image_url, ImageView profileIMGV) {
        Uri uri;
        if (profile_image_url != null) { //if (profile_image_URL != null) {
            uri = Uri.parse(profile_image_url);
            Picasso.get().load(uri).into(profileIMGV);
        }
    }
    /**
     *
     */
    private void getDataFromIntent() {
        Intent i = getIntent();
        display_name = i.getStringExtra(Users.FIRST_NAME)+" "
                    + i.getStringExtra(Users.LAST_NAME);
        myName = display_name;
        email = i.getStringExtra(Users.EMAIL);
        myEmail = email;
        birthday = i.getStringExtra(Users.BIRTHDAY);
        account_type = i.getStringExtra(Users.ACCOUNT_TYPE);
        userType = AccountType.getAccounType(account_type);
        phone = i.getStringExtra(Users.PHONE);
        profile_image_URL = i.getStringExtra(Users.PROFILE_IMAGE_URI);
        try{
            my_address_lat = Double.parseDouble(i.getStringExtra(Users.LATITUDE));
            my_address_long = Double.parseDouble(i.getStringExtra(Users.LONGITUDE));
        }catch (NumberFormatException e){ my_address_lat = 31.779739; my_address_long = 35.139914;}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,
                R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     *
     */
    @Override
    public void initializeComponentsListeners() {
        navigationView.setNavigationItemSelectedListener(this);
        profileIMGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTeacher()){
                    Intent i = new Intent(Homepage.this,TeacherProfile.class);
                    i.putExtra(Users.EMAIL,email);
                    i.putExtra(Users.DISPLAY_NAME, display_name);
                    i.putExtra(Users.PHONE, phone);
                    i.putExtra(Users.PROFILE_IMAGE_URI,profile_image_URL);
                    startActivity(i);
                } else if(isStudent()){
                    Intent i = new Intent(Homepage.this,StudentProfile.class);
                    i.putExtra(Users.EMAIL,email);
                    i.putExtra(Users.DISPLAY_NAME, display_name);
                    startActivity(i);
                } else if (isParent()){

                } else{

                }

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Tanawar Map",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.LEFT, 0,0);
                    toast.show();
                }else if(tab.getPosition() == 1){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Teacher's posts",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0);
                    toast.show();
                }else if(tab.getPosition() == 2){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Tanawar Videos",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0,0);
                    toast.show();
                }else if(tab.getPosition() == 3){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Tanawar Database",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.RIGHT, 0,0);
                    toast.show();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public boolean isTeacher(){
        if(AccountType.TEACHER.equals(AccountType.getAccounType(account_type)))
            return true;
        return false;
    }
    public boolean isStudent(){
        if(AccountType.STUDENT.equals(AccountType.getAccounType(account_type)))
            return true;
        return false;
    }
    public boolean isParent(){
        if(AccountType.PARENT.equals(AccountType.getAccounType(account_type)))
            return true;
        return false;
    }

    /**
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initializeActivityComponents() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyChatsActivity(view);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // get views from navigation header
        View navHeader = navigationView.getHeaderView(0);
        profileIMGV = navHeader.findViewById(R.id.homepage_profile_pic);
        txtUserFullName = navHeader.findViewById(R.id.txtProfile_user_full_name);
        txtUserEmail = navHeader.findViewById(R.id.txtProfile_user_email);
        // get menu items from navigation menu.
        Menu navMenu = navigationView.getMenu();
        logout = navMenu.findItem(R.id.nav_Logout);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        tabLayout = findViewById(R.id.homepage_tablayout);
        appBarLayout = findViewById(R.id.homepage_app_bar_layout);
        viewPager = findViewById(R.id.homepage_viewer);
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // create maptab and adapter
        tmtAdapter = new TanawarTabsAdapter(getSupportFragmentManager());
        // add fragments to the tabs
        mapTab = new TanawarMabsTab(AccountType.getAccounType(account_type), this);
        postsTab = new TanawarPostsTab();
        videosTab = new TeacherVideosTab();
        friendsSuggestionTab = new TanawarDatabaseTab();
        // create location tools
        createLocationTools();
        // adapter setup
        setupAdapter();

    }

    private void showMyChatsActivity(View view) {
        Intent i = new Intent(this, MyChatsActivity.class);
        startActivity(i);
    }


    /**
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createLocationTools() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(!is_changed_before){
                    mapTab.setLocationWithoutMarker(location.getLatitude(), location.getLongitude());
                    is_changed_before = true;
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        getGPSLocation();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getGPSLocation();
                }
                return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getGPSLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0,
                    locationListener);
        }
    }

    /**
     *
     */
    private void setupAdapter() {
        tmtAdapter.addFragment(mapTab,"Google Maps");
        tmtAdapter.addFragment(postsTab, "Posts");
        tmtAdapter.addFragment(videosTab,"Videos");
        tmtAdapter.addFragment(friendsSuggestionTab, "Friends Suggestions");
        // adapter Setup
        viewPager.setAdapter(tmtAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.map_tab_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.share_post);
        tabLayout.getTabAt(2).setIcon(R.drawable.youtube);
        tabLayout.getTabAt(3).setIcon(R.drawable.warehouse);
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_Logout:
                logout_item_pressed();
                break;
            case R.id.nav_MyProfile:
                myprofile_item_pressed();
                break;
        }
        return true;
    }

    private void myprofile_item_pressed() {
        if(isTeacher()){
            Intent i = new Intent(Homepage.this,TeacherProfile.class);
            i.putExtra(Users.EMAIL,email);
            i.putExtra(Users.DISPLAY_NAME, display_name);
            i.putExtra(Users.PHONE, phone);
            i.putExtra(Users.PROFILE_IMAGE_URI,profile_image_URL);
            startActivity(i);
        } else if(isStudent()){
            Intent i = new Intent(Homepage.this,StudentProfile.class);
            i.putExtra(Users.EMAIL,email);
            i.putExtra(Users.DISPLAY_NAME, display_name);
            startActivity(i);
        }
    }
    /**
     *
     */
    private void logout_item_pressed() {
        SharedPreferencesDB.getInstance().deleteUserFromSharedPreferences(getApplicationContext());
        Intent i = new Intent(Homepage.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
