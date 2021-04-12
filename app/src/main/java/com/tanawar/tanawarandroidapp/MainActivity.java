package com.tanawar.tanawarandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import apis.FirebaseConstants;
import tanawar_objects.User;
import utils.SharedPreferencesDB;
import utils.TanawarCalendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        User user = SharedPreferencesDB.getInstance().getUser(getApplicationContext());
        if( user == null){
            Intent i = new Intent(MainActivity.this , Login.class);
            startActivity(i);
        }else{
            Intent i = new Intent(MainActivity.this, Homepage.class);
            i.putExtra(FirebaseConstants.Collections.Users.FIRST_NAME,user.getFname());
            i.putExtra(FirebaseConstants.Collections.Users.LAST_NAME,user.getLname());
            i.putExtra(FirebaseConstants.Collections.Users.EMAIL,user.getEmail());
            i.putExtra(FirebaseConstants.Collections.Users.ACCOUNT_TYPE,user.getAccountType().getValue());
            i.putExtra(FirebaseConstants.Collections.Users.BIRTHDAY, TanawarCalendar.getDateTimeAsString(user.getBirthday()));
            i.putExtra(FirebaseConstants.Collections.Users.PHONE,user.getPhone());
            i.putExtra(FirebaseConstants.Collections.Users.PROFILE_IMAGE_URI,user.getProfileImGURL());
            i.putExtra(FirebaseConstants.Collections.Users.LATITUDE, user.getLatitude()+"");
            i.putExtra(FirebaseConstants.Collections.Users.LONGITUDE, user.getLongitude()+"");
            startActivity(i);
        }
        finish();
    }
}
