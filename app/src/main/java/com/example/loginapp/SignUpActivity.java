package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signUpEmail, signUpPassword;
    private Button signUpButton;
    private TextView signInTextView;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("SignUp");

        signUpEmail = findViewById(R.id.SignUpEmailTextID);
        signUpPassword = findViewById(R.id.SignUpPasswordId);
        signUpButton = findViewById(R.id.signUpButtonID);
        signInTextView = findViewById(R.id.signInTextID);
        progressBar = findViewById(R.id.progressBar2ID);

        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUpButtonID:
                userRegister();
                break;
            case R.id.signInTextID:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userRegister() {
        String email = signUpEmail.getText().toString().trim();
        String password = signUpPassword.getText().toString().trim();

        //checking the validity of email ID
        if(email.isEmpty()){
            signUpEmail.setError("Enter an email address");
            signUpEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signUpEmail.setError("Enter a valid email address");
            signUpEmail.requestFocus();
            return;
        }

        //checking the validity of password
        if(password.isEmpty()){
            signUpPassword.setError("Enter an password");
            signUpPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            signUpPassword.setError("Your password less than 6 character ");
            signUpPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){

                    Intent intent = new Intent(SignUpActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Register is successful ",Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    //if user already register of this email
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"User already registered ", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Register is failed", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }
}
