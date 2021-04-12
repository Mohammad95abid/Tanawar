package com.tanawar.tanawarandroidapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import apis.FirebaseConstants;
import de.hdodenhof.circleimageview.CircleImageView;
import interfaces.InitializeComponents;
/**
 *
 */
public class GoogleLogin extends AppCompatActivity implements InitializeComponents {
    // the layout components
    private CircleImageView google_prof_pic;
    private TextView google_profile_name;
    private TextView google_profile_email;
    private SignInButton login_google_button;
    private Button login_google_continue_buttonID;
    private Button logout_google_button;
    // the google auth components.
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    // a parameter used in the sign in intent.
    private final static int RC_SIGN_IN = 1;
    // used to save a user data after sign in when the continue button pressed.
    private HashMap<String,String> profileParamsToPass;
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_login);
        initializeActivityComponents();
        initializeComponentsListeners();
    }
    /**
     *
     */
    @Override
    public void initializeActivityComponents() {
        profileParamsToPass = new HashMap<>();
        // the layout components
        google_prof_pic = findViewById(R.id.google_prof_pic);
        google_profile_name = findViewById(R.id.google_profile_name);
        google_profile_email = findViewById(R.id.google_profile_email);
        login_google_button = findViewById(R.id.login_google_button);
        // Set the dimensions of the sign-in button.
        login_google_button.setSize(SignInButton.SIZE_STANDARD);
        login_google_continue_buttonID = findViewById(R.id.login_google_continue_buttonID);
        // logout button
        logout_google_button = findViewById(R.id.logout_google_buttonID);
        // the google auth components.
        //Configure Google Sign In.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail().build();
        mAuth = FirebaseAuth.getInstance();
        // Build a GoogleSignInClient with the options specified by gos.
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }
    /**
     *
     */
    @Override
    public void initializeComponentsListeners() {
        login_google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        login_google_continue_buttonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                Intent i = new Intent(GoogleLogin.this , Homepage.class);
                i.putExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME, profileParamsToPass.get(FirebaseConstants.Collections.Users.DISPLAY_NAME));
                i.putExtra(FirebaseConstants.Collections.Users.EMAIL, profileParamsToPass.get(FirebaseConstants.Collections.Users.EMAIL));
                i.putExtra(FirebaseConstants.Collections.Users.PROFILE_IMAGE_URI,
                        profileParamsToPass.get(FirebaseConstants.Collections.Users.PROFILE_IMAGE_URI));
                startActivity(i);
                finish();
            }
        });
        logout_google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                updateUI(null);
            }
        });
    }
    /**
     * A method to sign in using google-firebase - auth.
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    /**
     *
     * @param completedTask
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            FirebaseGoogleAuth(account);
        } catch (ApiException e) {
            FirebaseGoogleAuth(null);
        }
    }
    /**
     *
     * @param account
     */
    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(),
                null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else{
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
            google_profile_email.setText(email);
            google_profile_name.setText(name);
            Picasso.get().load(currentUser.getPhotoUrl()).into(google_prof_pic);
            login_google_continue_buttonID.setVisibility(View.VISIBLE);
            insertProfileDataIntoMap(name,email,currentUser.getPhotoUrl());
            // visible the sign out button, invisivle the sign in button .
            login_google_button.setVisibility(View.INVISIBLE);
            logout_google_button.setVisibility(View.VISIBLE);
        }else{
            google_profile_name.setText(getResources().getString(R.string.profile_name));
            google_profile_email.setText(getResources().getString(R.string.email));
            google_prof_pic.setImageResource(R.drawable.profile_pic);
            login_google_continue_buttonID.setVisibility(View.INVISIBLE);
            // visible the sign out button, invisivle the sign in button .
            login_google_button.setVisibility(View.VISIBLE);
            logout_google_button.setVisibility(View.INVISIBLE);
        }
    }
    /**
     *
     */
    private void logout() {
        mAuth.signOut();
    }
    /**
     * A method to collect a profile details in one collection to use and get it fast and easy.
     * @param display_name the user first name.
     * @param email the user email.
     * @param photoUrl the facebook profile's image path - uri .
     */
    private void insertProfileDataIntoMap(String display_name, String email, Uri photoUrl) {
        profileParamsToPass.put(FirebaseConstants.Collections.Users.DISPLAY_NAME, display_name);
        profileParamsToPass.put(FirebaseConstants.Collections.Users.EMAIL, email);
        profileParamsToPass.put(FirebaseConstants.Collections.Users.PROFILE_IMAGE_URI, photoUrl.toString());
    }
}
