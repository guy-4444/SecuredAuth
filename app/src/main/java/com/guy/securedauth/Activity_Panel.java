package com.guy.securedauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Activity_Panel extends AppCompatActivity {

    public static Intent createIntent(Context packageContext) {
        return new Intent(packageContext, Activity_Panel.class);
    }

    private FirebaseAuth mAuth;

    private TextView panel_LBL_info;
    private Button panel_LBL_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        panel_LBL_info = findViewById(R.id.panel_LBL_info);
        panel_LBL_read = findViewById(R.id.panel_LBL_read);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //currentUser.updateEmail("guy@gmail.com");
        //UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName("Ofer").setPhotoUri(Uri.parse("image_link.")).build();
        //currentUser.updateProfile(userProfileChangeRequest);


        String str = "";
        str += "\n" + currentUser.getUid();
        str += "\n" + currentUser.getEmail();
        str += "\n" + currentUser.getPhoneNumber();
        str += "\n" + currentUser.getDisplayName();
        str += "\n" + currentUser.getProviderId();
        str += "\n" + currentUser.getPhotoUrl();
        panel_LBL_info.setText(str);


        panel_LBL_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read();
            }
        });
    }

    private void read() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        MyUser myUser = task.getResult().toObject(MyUser.class);
                        String str = "";
                        str += "\n" + myUser.getUid();
                        str += "\n" + myUser.getEmail();
                        str += "\n" + myUser.getName();
                        str += "\n" + myUser.getCourses().size();
                        str += "\n" + myUser.getPhone();
                        panel_LBL_info.setText(str);
                    }
                });

    }

    private void read2() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                MyUser myUser = document.toObject(MyUser.class);
                                String str = "";
                                str += "\n" + myUser.getUid();
                                str += "\n" + myUser.getEmail();
                                str += "\n" + myUser.getName();
                                str += "\n" + myUser.getCourses().size();
                                str += "\n" + myUser.getPhone();
                                panel_LBL_info.setText(str);
                                Log.d("pttt", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("pttt", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
