package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.Fragments.Chairs;
import com.example.ecommerceapp.Models.ItemModel;
import com.example.ecommerceapp.Models.photolist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity
{
    TextView namefield,pricefield;
    ImageView photo,like;
    String name,photoll;
    double price;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<photolist>photolistList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        views();
        toolbar();

        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        photolistList = new ArrayList<>();
        maylikeadapter adapter = new maylikeadapter(photolistList);
        recyclerView.setAdapter(adapter);

    }

    private void views()
    {
        namefield = findViewById(R.id.product_name);
        pricefield = findViewById(R.id.product_price);
        photo = findViewById(R.id.product_image);
        name = getIntent().getStringExtra("Name");
        price = getIntent().getDoubleExtra("Price",0.0);
        photoll =getIntent().getStringExtra("Image");
        namefield.setText(name);
        pricefield.setText(String.valueOf(price));
        Picasso.get().load(photoll).into(photo);

        like = findViewById(R.id.favoritel);
        like.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                like.setBackgroundResource(R.drawable.ic_like_red);
            }
        });
    }

    private void toolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
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
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                finish();
                break;
            case R.id.wishlist:
                startActivity(new Intent(getApplicationContext(),WishListActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addToBag(View view)
    {
        Intent intent = new Intent(getApplicationContext(),MyBagActivity.class);
        intent.putExtra("Name",name);
        intent.putExtra("price",price);
        intent.putExtra("photo",photoll);
        String key = databaseReference.child("MyBag").push().getKey();
        ItemModel itemModel = new ItemModel(name,photoll,price);
        databaseReference.child("Mybag").child(key).setValue(itemModel);
    }

    public class maylikeadapter extends RecyclerView.Adapter<maylikeadapter.maylikevh>
    {
        List<photolist>list;

        public maylikeadapter(List<photolist> list)
        {
            this.list = list;
        }

        @NonNull
        @Override
        public maylikevh onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.maylikitem, null);
            return new maylikeadapter.maylikevh(view);
        }

        @Override
        public void onBindViewHolder(@NonNull maylikevh holder, int position)
        {
            photolist photolist = list.get(position);
            Picasso.get().load(photolist.getPhoto()).into(holder.image);

        }

        @Override
        public int getItemCount()
        {
            return list.size();
        }

        public class maylikevh extends RecyclerView.ViewHolder
        {
            ImageView image;

            public maylikevh(@NonNull View itemView)
            {
                super(itemView);
                image = itemView.findViewById(R.id.likeitem);
            }
        }
    }

}
