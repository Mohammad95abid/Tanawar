package com.tanawar.tanawarandroidapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import apis.UserAPI;
import helper_tools.TanawarAlertDialog;
import interfaces.InitializeComponents;

public class Login extends AppCompatActivity implements InitializeComponents {
    private Button btnRegister;
    private Button btnLogin;
    private EditText edtUsername;
    private EditText edtPassword;
    private ImageView facebookIC;
    private ImageView googleIC;
    private ImageView userGuideIC;
    private TextView userGuideTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initializeActivityComponents();
        initializeComponentsListeners();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent i =getIntent();
        String em = i.getStringExtra("email");
        String pass = i.getStringExtra("password");
        String userID = i.getStringExtra("userID");
        if(em != null && !em.isEmpty())
            this.edtUsername.setText(em);
        if(pass != null && !pass.isEmpty())
            this.edtPassword.setText(pass);
    }
    /**
     *
     */
    private void startRegisterActivity() {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
    }
    /**
     *
     */
    @Override
    public void initializeComponentsListeners() {
        this.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterActivity();
            }
        });
        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        this.facebookIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this , FacebookLogin.class);
                startActivity(i);
                finish();
            }
        });
        this.googleIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this , GoogleLogin.class);
                startActivity(i);
                finish();
            }
        });
        this.userGuideIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserGuide();
            }
        });
        this.userGuideTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserGuide();
            }
        });
    }

    private void showUserGuide() {
        String url = "https://firebasestorage.googleapis.com/v0/b/tanawar-mr.appspot.com/o/Files%2FBiology%2F%D7%9E%D7%91%D7%95%D7%90%20%D7%9C%D7%9E%D7%A2%D7%A8%D7%9B%D7%AA%20%D7%94%D7%A2%D7%A6%D7%91%D7%99%D7%9D.pdf?alt=media&token=73a1110f-da68-4377-948b-e505d36df4d5";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(browserIntent);
    }

    private void login() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        if(username == null || username.isEmpty()){
            new TanawarAlertDialog().showSimpleDialog("Login Failed",
                    "The username field (Email / Phone) is Empty.",
                    Login.this).show();
        }else{
            if(password == null || password.isEmpty()){
                new TanawarAlertDialog().showSimpleDialog("Login Failed",
                        "The password field is Empty.",
                        Login.this).show();
            }else{
                UserAPI.getInstance().login(username, password,Login.this);
            }
        }
    }

    @Override
    public void initializeActivityComponents() {
        this.btnRegister = findViewById(R.id.btnRegister);
        this.edtUsername = findViewById(R.id.edtUsername);
        this.edtPassword = findViewById(R.id.edtPasswordLogin);
        this.btnLogin = findViewById(R.id.btnLogin);
        this.facebookIC = findViewById(R.id.facebookIC);
        this.googleIC = findViewById(R.id.socialGoogleIC);
        this.userGuideIC = findViewById(R.id.userGuideIC);
        this.userGuideTV = findViewById(R.id.userGuideTV);
    }
}
