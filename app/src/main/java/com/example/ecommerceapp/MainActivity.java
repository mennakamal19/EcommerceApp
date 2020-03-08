package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity
{
    String email,password;
    EditText emailfield,passwordfield;
    Switch aSwitch;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailfield = findViewById(R.id.email);
        passwordfield = findViewById(R.id.password);
        aSwitch = findViewById(R.id.switchbtn);
        inSwitch();

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null)
        {
            Intent intent = new Intent(getApplicationContext(),CategoriesActivity.class);
            startActivity(intent);
            finish();
       }
    }

    private void inSwitch()
    {
        final Switch s = new Switch(this);
        s.setChecked(true);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s.isChecked())
                {
                    // The toggle is enabled
                }
                else {
                    // The toggle is disabled
                    // move to forget password activity
                }
            }
        });
    }

    public void signup(View view)
    {
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));

    }

    public void login(View view)
    {
        email = emailfield.getText().toString();
        password = passwordfield.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(), "enter your email", Toast.LENGTH_SHORT).show();
            emailfield.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "enter your password", Toast.LENGTH_SHORT).show();
            passwordfield.requestFocus();
            return;
        }
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(MainActivity.this,CategoriesActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}