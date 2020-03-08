package com.example.ecommerceapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.CategoriesActivity;
import com.example.ecommerceapp.Models.ItemModel;
import com.example.ecommerceapp.ProductActivity;
import com.example.ecommerceapp.R;
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

public class Chairs extends Fragment
{
    String name , photo;
    double price;

    RecyclerView recyclerView;
    List<ItemModel>itemModelList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
         View view=  inflater.inflate(R.layout.chairesfragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        itemModelList = new ArrayList<>();

        inItFirebase();
        getproducts();
        addToDB();
    }

    private void inItFirebase()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    private void addToDB()
    {
        String key = databaseReference.child("Chairs").push().getKey();
        ItemModel itemModel = new ItemModel(name,photo,price);
        databaseReference.child("Chairs").child(key).setValue(itemModel);
    }

    private void getproducts()
    {
        databaseReference.child("Chairs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                itemModelList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    ItemModel itemModel = dataSnapshot1.getValue(ItemModel.class);
                    itemModelList.add(itemModel);
                }
                Listadapter adapter = new Listadapter(itemModelList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    public class Listadapter extends RecyclerView.Adapter<Listadapter.Listvh>
    {
        List<ItemModel> itemModels;

        public Listadapter(List<ItemModel> itemModels) {
            this.itemModels = itemModels;
        }

        @NonNull
        @Override
        public Listadapter.Listvh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.productitemlist, null);
            return new Listadapter.Listvh(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Listadapter.Listvh holder, int position)
        {
            final ItemModel itemModel = itemModels.get(position);
            final String name = itemModel.getName();
            holder.name.setText(name);
            final double price = itemModel.getPrice();
            holder.pricel.setText(String.valueOf(price));
            final String img = itemModel.getPhoto();
            Picasso.get().load(img).into(holder.image);

            holder.like.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    holder.like.setBackgroundResource(R.drawable.ic_like_red);

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Intent intent = new Intent(getContext(), ProductActivity.class);
                    //intent.putParcelableArrayListExtra("itemmodel",itemModel);
                    intent.putExtra("Name",name);
                    intent.putExtra("Price",price);
                    intent.putExtra("Image",img);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount()
        {
            return itemModels.size();
        }

        public class Listvh extends RecyclerView.ViewHolder
        {
            ImageView image,like;
            TextView name, pricel;

            public Listvh(@NonNull View itemView) {
                super(itemView);
                like = itemView.findViewById(R.id.favorite);
                image = itemView.findViewById(R.id.productimage);
                name = itemView.findViewById(R.id.productname);
                pricel = itemView.findViewById(R.id.productprice);
            }
        }
    }
}
