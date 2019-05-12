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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountBtn;
    private EditText nameInput,emailInput,passInput;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        createAccountBtn = (Button) findViewById(R.id.register_btn);
        //nameInput = (EditText) findViewById(R.id.register_name_input);
        emailInput = (EditText) findViewById(R.id.register_email_input);
        passInput = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        // instance of firebase auth
        mAuth = FirebaseAuth.getInstance();

        /*createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });*/

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser() {

        String email = emailInput.getText().toString().trim();
        String password = passInput.getText().toString().trim();

        if(!(email.isEmpty()))
        {
            validatEmail(email);
        }


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
            passInput.setError("Password is required");
            passInput.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            passInput.setError("Minimum length of password should be 6");
            passInput.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // this method will be called after the registration is finished
                if (task.isSuccessful())
                {
                    /*if (task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(RegisterActivity.this, "User with this email already exist.", Toast.LENGTH_SHORT).show();
                    }*/
                    Toast.makeText(RegisterActivity.this, "Accoount Has Been Created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(RegisterActivity.this, "You are already registered", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                /*else
                {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Network Error: Please try another time", Toast.LENGTH_SHORT).show();
                    Log.i("Response","Failed to create user:"+task.getException().getMessage());
                }*/
            }
        });// mAuth

    }// registerUser()

    private void validatEmail(String email) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


    }



   /* private void createAccount(){
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passInput.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please write your name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please write your email address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write your password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateEmail(name,email, password);
        }

    }// create account

    private void ValidateEmail(final String name, final String email, final String password)
    {
       final DatabaseReference RootRef;
       RootRef = FirebaseDatabase.getInstance().getReference();

       RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot)
           {
               if(!(dataSnapshot.child("Users").child(email).exists()))
               {
                   HashMap<String, Object> userdataMap = new HashMap<>();
                   userdataMap.put("name", name);
                   userdataMap.put("email", email);
                   userdataMap.put("password", password);

                   RootRef.child("Users").child(email).updateChildren(userdataMap)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task)
                               {
                                   if(task.isSuccessful())
                                   {
                                       Toast.makeText(RegisterActivity.this, "Yout Account has been created", Toast.LENGTH_SHORT).show();
                                       loadingBar.dismiss();

                                       Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                       startActivity(intent);
                                   }
                                   else
                                   {
                                       loadingBar.dismiss();
                                       Toast.makeText(RegisterActivity.this, "Network Error: Please try another time", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });


               }
               else
               {
                   Toast.makeText(RegisterActivity.this, "This " + email +" email already exist", Toast.LENGTH_SHORT).show();
                   loadingBar.dismiss();

                   Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                   startActivity(intent);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError)
           {

           }
       });
    }// validate email*/

}// RegisterActivity
