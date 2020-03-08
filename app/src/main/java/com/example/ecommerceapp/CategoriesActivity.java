package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceapp.Fragments.BedRoom;
import com.example.ecommerceapp.Fragments.Chairs;
import com.example.ecommerceapp.Fragments.Mirror;
import com.example.ecommerceapp.Fragments.Sofa;
import com.example.ecommerceapp.Fragments.Tables;
import com.example.ecommerceapp.Fragments.kitchen;
import com.example.ecommerceapp.Models.ItemModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity
{
    ImageView add_item,list_recycle,grid_recycler;
    TabLayout tabLayout;
    FragmentPagerAdapter fragmentPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        toolbar();

        add_item = findViewById(R.id.add_item);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        add_item.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            Fragment[] fragments = new Fragment[]
                    { new Chairs(),new Tables(),new BedRoom(),new Mirror(),new Sofa(),new kitchen()

                    };
            String[] names = new String[]
                    {
                            "Chairs","Tables","BedRoom","Mirror","Sofa","Kitchen"

                    };
            @Override
            public Fragment getItem(int position)
            {
                return fragments[position];
            }

            @Override
            public int getCount()
            {
                return fragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position)
            {
              return names[position];
            }
        };
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void toolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //true or false?
        getSupportActionBar().setHomeButtonEnabled(true);// from omninotes
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
                return true;

            case R.id.wishlist:
                startActivity(new Intent(getApplicationContext(),WishListActivity.class));
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
