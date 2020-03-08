package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity
{
    Toolbar toolbar;
    CircleImageView circleImageView;
    TextView usernamefield,emailfield;
    String username,email;
    Uri photo;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar();
        usernamefield = findViewById(R.id.username);
        emailfield = findViewById(R.id.useremail);
        circleImageView = findViewById(R.id.userimg);
        aSwitch = findViewById(R.id.switch_not);
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        emailfield.setText(email);

        intimg();
        inSwitch();

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
                    }
                }
            });
        }

    private void intimg()
    {
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1,1)
                        .start(ProfileActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);//  uri to crop image.activity result to take it
            if (resultCode == Activity.RESULT_OK) // i have to ensure result is return
            {
                if(result!=null)
                {
                    photo = result.getUri();
                    Picasso.get()
                            .load(photo)
                            .placeholder(R.drawable.ic_user_white)
                            .error(R.drawable.ic_user_white)
                            .into(circleImageView);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toolbar()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbarmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.profile:
                onBackPressed();
                break;

            case R.id.wishlist:
                startActivity(new Intent(getApplicationContext(),WishListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void myBag(View view)
    {
        startActivity(new Intent(getApplicationContext(),MyBagActivity.class));
    }


}
