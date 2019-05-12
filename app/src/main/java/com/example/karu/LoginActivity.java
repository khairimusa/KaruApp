package com.example.karu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karu.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private TextView registerLink,forgotPasswordLink;


    private String parentDbName = "Users";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        emailInput = (EditText) findViewById(R.id.login_email_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);
        loginButton = (Button) findViewById(R.id.login_btn);
        registerLink = (TextView) findViewById(R.id.register_link);
        forgotPasswordLink = (TextView) findViewById(R.id.forget_password_link);



        loadingBar = new ProgressDialog(this);
        //Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

    }


    private void loginUser() {

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // validate these inputs
        if(email.isEmpty())
        {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        // check if email is valid or not
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailInput.setError("Please enter a valid Email");
            emailInput.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    // if successfull navigate to different activites
                    loadingBar.setTitle("Login into Account");
                    loadingBar.setMessage("Please wait, while we are checking the credentials.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    
                    Log.d(TAG, "signInWithEmail:success");

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    // add flags to clear all activities on top of the stack and open new activities
                    startActivity(intent);
                    finish();

                }
                else
                {
                    Toast.makeText(LoginActivity.this, ""+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }// loginUser()

}
