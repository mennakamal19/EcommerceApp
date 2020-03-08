package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.Models.ItemModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyBagActivity extends AppCompatActivity
{
    Toolbar toolbar;
    TextView totalfield;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<ItemModel>itemModelList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bag);
        totalfield = findViewById(R.id.total);
        toolbar();

        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),RecyclerView.VERTICAL);
        itemModelList = new ArrayList<>();

        getbag();
    }

    private void getbag()
    {
        databaseReference.child("Mybag").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    itemModelList.clear();
                    ItemModel itemModel1 = dataSnapshot1.getValue(ItemModel.class);
                    itemModelList.add(itemModel1);
                }
                mybagadapter adapter = new mybagadapter(itemModelList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void toolbar()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("");
    }

    public void chechOut(View view)
    {
        startActivity(new Intent(getApplicationContext(),Shipping2Activity.class));
        String key =databaseReference.child("Orders").push().getKey();
        ItemModel itemModel = new ItemModel();
        databaseReference.child("Orders").child(getUId()).child(key).setValue(itemModel);
    }


    public class mybagadapter extends RecyclerView.Adapter<mybagadapter.bagvh>
    {
        List<ItemModel>itemModels;

        public mybagadapter(List<ItemModel> itemModels)
        {
            this.itemModels = itemModels;
        }

        @NonNull
        @Override
        public bagvh onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bag_item, null);
            return new mybagadapter.bagvh(view);
        }

        @Override
        public void onBindViewHolder(@NonNull bagvh holder, int position)
        {
            final ItemModel itemModel = itemModels.get(position);
            String name = getIntent().getStringExtra("Name");
            double price = getIntent().getDoubleExtra("price",0.0);
            String img = getIntent().getStringExtra("photo");
            holder.name.setText(name);
            holder.price.setText(String.valueOf(price));
            Picasso.get().load(img).into(holder.img);

            String key = databaseReference.child("Orders").push().getKey();
            ItemModel itemModel1 = new ItemModel(name,img,price);
            databaseReference.child("Orders").child(getUId()).child(key).setValue(itemModel1);
        }

        @Override
        public int getItemCount()
        {
            return itemModels.size();
        }

        public class bagvh extends RecyclerView.ViewHolder
        {
            ImageView img;
            TextView name,price;
            FloatingActionButton addfab,minsfad;
            EditText count;

            public bagvh(@NonNull View itemView)
            {
                super(itemView);
                img = itemView.findViewById(R.id.bag_image);
                name = itemView.findViewById(R.id.bag_name);
                price = itemView.findViewById(R.id.bag_price);
                count = itemView.findViewById(R.id.count);
                addfab = itemView.findViewById(R.id.addfab);
                minsfad = itemView.findViewById(R.id.minsfab);
            }
        }
     }
    private String getUId()
    {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }
}
