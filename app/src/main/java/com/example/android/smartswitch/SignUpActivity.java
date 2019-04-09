package com.example.android.smartswitch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    Button mSignUpButton;
    EditText mEmail;
    EditText mPassword;
    TextView mLoginTextView;

    ProgressDialog mProgressDialog;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mLoginTextView = findViewById(R.id.tv_link_login);
        mLoginTextView.setOnClickListener(this);

        mSignUpButton = findViewById(R.id.btn_signup);
        mSignUpButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == mSignUpButton){
            registerUser();
        } else if (v == mLoginTextView){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }


    private void registerUser(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            //email is empty
            mEmail.setError("Enter a valid email address");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6){
            //password is empty or has less than 6 characters
            mPassword.setError("Enter a password with more than 6");
            return;
        }

        mProgressDialog.setMessage("Registering User...");
        mProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            mProgressDialog.hide();
                            sendVerificationEmail();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            mProgressDialog.hide();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void sendVerificationEmail(){

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(SignUpActivity.this, "Verification email sent.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
