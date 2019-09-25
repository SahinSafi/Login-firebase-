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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signInEmail, signInPassword;
    private TextView signUpTextView;
    private Button signInButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Loin");

        signInEmail = findViewById(R.id.EmailTextID);
        signInPassword = findViewById(R.id.PasswordTextID);
        signInButton = findViewById(R.id.signInButtonID);
        signUpTextView = findViewById(R.id.signUpTextID);
        progressBar = findViewById(R.id.progressBarID);

        mAuth = FirebaseAuth.getInstance();

        signUpTextView.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInButtonID:
                userLogin();
                break;
            case R.id.signUpTextID:
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void userLogin() {
        String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();

        //checking the validity of email ID
        if(email.isEmpty()){
            signInEmail.setError("Enter an email address");
            signInEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signInEmail.setError("Enter a valid email address");
            signInEmail.requestFocus();
            return;
        }

        //checking the validity of password
        if(password.isEmpty()){
            signInPassword.setError("Enter an password");
            signInPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            signInPassword.setError("Your password less than 6 character ");
            signInPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this,"Incorrect account", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
