package com.guy.securedauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class Activity_Splash extends AppCompatActivity {

    private Button splash_BTN_start;
    private Button splash_BTN_read;

    private static final String TAG = "MainActivity";
    private final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_BTN_start = findViewById(R.id.splash_BTN_start);
        splash_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //mAuth.signOut();



    }

    private void start() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Toast.makeText(this, "User: " + currentUser.getUid(), Toast.LENGTH_SHORT).show();
            startActivity(Activity_Panel.createIntent(this));
            finish();
        } else {
            Toast.makeText(this, "No user", Toast.LENGTH_SHORT).show();
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                                    new AuthUI.IdpConfig.AnonymousBuilder().build()))
                            .setTosAndPrivacyPolicyUrls("https://sites.google.com/view/privacy-policy-secure-notes",
                                    "https://sites.google.com/view/privacy-policy-secure-notes")
                            .build(),
                    RC_SIGN_IN);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                createNewUserInDB();

                startActivity(Activity_Panel.createIntent(this));
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar("sign_in_cancelled");
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar("no_internet_connection");
                    return;
                }

                showSnackbar("unknown_error");
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    private void createNewUserInDB() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        MyUser myUser = new MyUser();
        myUser.setUid(currentUser.getUid());
        myUser.setName("Alon");
        myUser.setName("");
        myUser.setEmail("lana@gmail.com");
        myUser.setPhone(currentUser.getPhoneNumber());
        myUser.getCourses().add("Android");
        myUser.getCourses().add("Swift");
        myUser.getCourses().add("C#");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(currentUser.getUid())
                .set(myUser);
    }

    private void showSnackbar(String message) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }

}
