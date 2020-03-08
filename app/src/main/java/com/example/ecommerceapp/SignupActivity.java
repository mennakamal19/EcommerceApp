package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerceapp.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignupActivity extends AppCompatActivity
{
    String email,password;
    EditText email_field,password_field;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        email_field = findViewById(R.id.useremail);
        password_field = findViewById(R.id.userpassword);

        inItFirebase();
        printHash();
    }

    private void inItFirebase()
    {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public void log(View view)
    {
        onBackPressed();
    }

    public void sign(View view)
    {
        email = email_field.getText().toString();
        password = password_field.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(), "enter email", Toast.LENGTH_SHORT).show();
            email_field.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            Toast.makeText(getApplicationContext(), "password too short", Toast.LENGTH_SHORT).show();
            email_field.requestFocus();
            return;
        }
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setMessage("Wait..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER) ;
            progressDialog.setCancelable(false);
            progressDialog.show();
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        String id = task.getResult().getUser().getUid();
                        addtoDb(id,email);
                    }else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }

    private void addtoDb(String id, String email)
    {
        UserModel userModel = new UserModel(email);
        databaseReference.child("Users").child(id).setValue(userModel);
        progressDialog.dismiss();
        startActivity(new Intent(SignupActivity.this,CategoriesActivity.class));
        finish();
    }

    public void continuewithfacebook(View view)
    {
    }
    private void printHash()
    {
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.example.ecommerceapp", PackageManager.GET_SIGNATURES);
            for(Signature signature : packageInfo.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash",Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException| NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
}
