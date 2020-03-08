package com.example.ecommerceapp.Models;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ItemModel extends ArrayList<Parcelable> {
    private String name,photo;
    private double price;

    public ItemModel() {

    }

    public ItemModel( String name, String photo, double price)
    {

        this.name = name;
        this.photo = photo;
        this.price = price;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NonNull
    @Override
    public Stream<Parcelable> stream() {
        return null;
    }
}
