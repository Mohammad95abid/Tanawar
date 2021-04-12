package com.tanawar.tanawarandroidapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Calendar;
import apis.UserAPI;
import helper_tools.TanawarAlertDialog;
import interfaces.InitializeComponents;
import tanawar_objects.User;
import tanawar_objects.UserOnMap;

public class AddAskHelpOnMap extends AppCompatActivity implements InitializeComponents {
    private EditText description;
    private Button request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ask_help_on_map);
        initializeActivityComponents();
        initializeComponentsListeners();
    }

    @Override
    public void initializeComponentsListeners() {
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = description.getText().toString();
                if(des == null || des.isEmpty()){
                    new TanawarAlertDialog().showSimpleDialog(
                            "Request Help, Adds on Tanawar's Map",
                            "The Description Is Empty!, " +
                                    "\nPlease Enter Your Request Description",
                            AddAskHelpOnMap.this).show();
                    return;
                }

                UserAPI.getInstance().getStudentByEmail(Homepage.myEmail,
                        AddAskHelpOnMap.this);
            }
        });
    }

    @Override
    public void initializeActivityComponents() {
        description = findViewById(R.id.description_AskOnMap);
        request = findViewById(R.id.btnRequestHelp_AskOnMap);
    }

    public void setUserData(User user) {
        Calendar c = Calendar.getInstance();
        UserOnMap um = new UserOnMap(user.getAccountType(), user.getAddress(), user.getLatitude(),
                user.getLongitude(), user.getFname()+" "+ user.getLname(),
                user.getEmail(), user.getPhone(), description.getText().toString(),
                "", "");
        UserAPI.getInstance().addAskHelpForStudent(um, this);
        description.setText("");
    }
}
