package com.tanawar.tanawarandroidapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import apis.FirebaseConstants.Collections.Users;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;
import helper_tools.TanawarAlertDialog;
import interfaces.InitializeComponents;
import com.tanawar.tanawarandroidapp.R;

public class FacebookLogin extends AppCompatActivity implements InitializeComponents {
    private LoginButton login_facebook_button;
    private CircleImageView circleIMGV;
    private TextView fb_name, fb_email;
    private CallbackManager callbackManager;
    private Button login_facebook_continue_buttonID;
    private HashMap<String,String> profileParamsToPass;
    private FirebaseAuth mAuth;
    private boolean continue_button_pressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        makeAccessTokenTracker();
        initializeActivityComponents();
        initializeComponentsListeners();
    }
    /**
     *
     */
    @Override
    public void initializeComponentsListeners() {
        login_facebook_continue_buttonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continue_button_pressed = true;
                Intent i = new Intent(FacebookLogin.this, Homepage.class);
                i.putExtra(Users.DISPLAY_NAME, profileParamsToPass.get(Users.DISPLAY_NAME));
                i.putExtra(Users.EMAIL, profileParamsToPass.get(Users.EMAIL));
                i.putExtra(Users.PROFILE_IMAGE_URI,
                        profileParamsToPass.get(Users.PROFILE_IMAGE_URI));
                startActivity(i);
                logout();
                finish();
            }
        });
    }
    /**
     *
     */
    private void logout() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }
    /**
     *
     */
    @Override
    public void initializeActivityComponents() {
        login_facebook_button = findViewById(R.id.login_facebook_button);
        circleIMGV = findViewById(R.id.facebook_prof_pic);
        fb_name = findViewById(R.id.profile_name);
        fb_email = findViewById(R.id.profile_email);
        profileParamsToPass = new HashMap<>();
        login_facebook_continue_buttonID = findViewById(R.id.login_facebook_continue_buttonID);
        callbackManager = CallbackManager.Factory.create();
        login_facebook_button.setPermissions(Arrays.asList("email","public_profile"));
        login_facebook_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                new TanawarAlertDialog().showSimpleDialog(
                        "Authenticate Using Facebook Login Failed",
                        "Login was canceled.",
                        FacebookLogin.this).show();
            }

            @Override
            public void onError(FacebookException error) {
                new TanawarAlertDialog().showSimpleDialog(
                        "Authenticate Using Facebook Login Failed",
                        "Error: "+error.getMessage(),
                        FacebookLogin.this).show();
            }
        });
    }
    // Start part of auth & login by facebook & firebase
    @Override
    protected void onStart() {
        super.onStart();
        getCurrentFacebookUser();
    }
    /**
     *
     */
    private void getCurrentFacebookUser() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null && currentUser.getProviderId().equals("facebook.com")){
            updateUI(currentUser);
        }else{
            updateUI(null);
        }
    }
    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     *
     * @param token
     */
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            new TanawarAlertDialog().showSimpleDialog(
                                    "Authenticate Using Facebook Login Failed",
                                    "Error: "+task.getException().getMessage(),
                                    FacebookLogin.this).show();
                            updateUI(null);
                        }
                    }
                });
    }
    /**
     *
     * @param currentUser
     */
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            //String phone = currentUser.getPhoneNumber();
            fb_email.setText(email);
            fb_name.setText(name);
            Picasso.get().load(currentUser.getPhotoUrl()).into(circleIMGV);
            login_facebook_continue_buttonID.setVisibility(View.VISIBLE);
            insertProfileDataIntoMap(name,email,currentUser.getPhotoUrl());
        }else{
            fb_name.setText(getResources().getString(R.string.profile_name));
            fb_email.setText(getResources().getString(R.string.email));
            circleIMGV.setImageResource(R.drawable.profile_pic);
            login_facebook_continue_buttonID.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * A method used to track every change on access token, and call updateUI with
     *  suitable access token .
     *  when the user logout pass null with updateUI, otherwise when user login pass current user
     *  to updateUI.
     */
    private void makeAccessTokenTracker(){
        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    if(!continue_button_pressed) {
                        new TanawarAlertDialog().showSimpleDialog("Logged out",
                                "Logged out successfully.",
                                FacebookLogin.this).show();
                    }
                    logout();
                    updateUI(null);
                }else{
                    getCurrentFacebookUser();
                }
            }
        };
    }
    /**
     * A method to collect a profile details in one collection to use and get it fast and easy.
     * @param display_name the user first name.
     * @param email the user email.
     * @param photoUrl the facebook profile's image path - uri .
     */
    private void insertProfileDataIntoMap(String display_name, String email, Uri photoUrl) {
        profileParamsToPass.put(Users.DISPLAY_NAME, display_name);
        profileParamsToPass.put(Users.EMAIL, email);
        profileParamsToPass.put(Users.PROFILE_IMAGE_URI, photoUrl.toString());
    }
}
