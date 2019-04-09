package com.example.android.smartswitch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            intent = new Intent(this,MainActivity.class);
        } else {
            intent = new Intent(this,LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
