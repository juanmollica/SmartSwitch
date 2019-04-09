package com.example.android.smartswitch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
//    private static final int REQUEST_SIGNUP = 0;

    EditText mEmailEditText;
    EditText mPasswordEditText;
    Button mLoginButton;
    TextView mSignUpTextView;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        mEmailEditText = findViewById(R.id.et_email);
        mPasswordEditText = findViewById(R.id.et_password);
        mLoginButton = findViewById(R.id.btn_login);
        mSignUpTextView = findViewById(R.id.link_signup);

        mAuth = FirebaseAuth.getInstance();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }


    private void signUp(){
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }


    private void login(){


        if(!validate()){
            loginFailed();
            return;
        }

        mLoginButton.setEnabled(false);

        final ProgressDialog progressDialog= new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Authenticating ...");
        progressDialog.show();

        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (user.isEmailVerified()) {
                                    loginSuccess();
                                } else {
                                    Toast.makeText(LoginActivity.this,"Please verify account",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else{
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "onLogin: failure", task.getException());
                            loginFailed();
                        }

                    }
                });
    }


    private boolean validate(){

        boolean valid = true;

        String username = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if(username.isEmpty()){

            mEmailEditText.setError("Enter a valid email address");
            valid = false;

        } else {

            mEmailEditText.setError(null);
        }

        if(password.isEmpty() || password.length() < 4 || password.length() > 10) {

            mPasswordEditText.setError("Enter a password between 4 and 10");
            valid = false;

        } else {

            mPasswordEditText.setError(null);

        }

        return valid;


    }

    private void loginSuccess(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);
    }

    private void loginFailed(){
        Toast.makeText(this,"Login Failed.",Toast.LENGTH_SHORT).show();
        mLoginButton.setEnabled(true);
    }

}

