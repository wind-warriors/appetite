package com.windwarriors.appetite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.windwarriors.appetite.adapter.BusinessAdapter;
import com.windwarriors.appetite.model.Business;

import java.util.ArrayList;

import static com.windwarriors.appetite.utils.Helper.setUserGreetingTextView;


public class BusinessListActivity extends AppCompatActivity {
    private RecyclerView categoriesRecyclerView;
    private RecyclerView.Adapter categoryAdapter;
    private RecyclerView.LayoutManager categoryLayoutManager;
    //private YelpService yelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);

        //yelp = new YelpService();
        //ArrayList<Business> list = yelp.getBusiness();

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);

        String mockImageLink = "http://del.h-cdn.co/assets/15/37/640x552/gallery-1441895894-weeknight-dinner-squash-salad.jpg";
        String mockImageLink2 = "https://images.unsplash.com/photo-1503764654157-72d979d9af2f?ixlib=rb-0.3.5&s=004ac76e65f0b5708b0f04523ea9c6de&auto=format&fit=crop&w=1953&q=80";
        String mockImageLink3 = "https://images.unsplash.com/photo-1485963631004-f2f00b1d6606?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=a98ac47048f530b6d587279d52c13ab7&auto=format&fit=crop&w=1868&q=80";
        String mockImageLink4 = "https://static.independent.co.uk/s3fs-public/styles/story_large/public/thumbnails/image/2018/01/12/12/healthy-avo-food.jpg";

        ArrayList<Business> list = new ArrayList<>();
        list.add(new Business(1, "Hey Noodles", "20 Reviews", "Noodles, Chinese",
                "5306 Yonge Street, Willowdale", "12", mockImageLink));
        list.add(new Business(2, "Scott Carribean", "43 Reviews", "Caribbean",
                "1943 Avenue Road, Toronto", "23", mockImageLink2));
        list.add(new Business(3, "Fat Ninja Bite", "51 Reviews", "Japanese, Burgers, Korean",
                "3517 Kennedy Road, Milliken", "18", mockImageLink3));
        list.add(new Business(4, "Saravanaa Bhavan", "26 Reviews", "Vegetarian, Indian",
                "1571 Sandhurst Circle, Scarborough", "2", mockImageLink4));
        list.add(new Business(1, "Hey Noodles", "20 Reviews", "Noodles, Chinese",
                "5306 Yonge Street, Willowdale", "12", mockImageLink));
        list.add(new Business(2, "Scott Carribean", "43 Reviews", "Caribbean",
                "1943 Avenue Road, Toronto", "23", mockImageLink2));
        list.add(new Business(3, "Fat Ninja Bite", "51 Reviews", "Japanese, Burgers, Korean",
                "3517 Kennedy Road, Milliken", "18", mockImageLink3));
        list.add(new Business(4, "Saravanaa Bhavan", "26 Reviews", "Vegetarian, Indian",
                "1571 Sandhurst Circle, Scarborough", "2", mockImageLink4));


        categoriesRecyclerView = findViewById(R.id.recycler_view_business_list);
        categoriesRecyclerView.setHasFixedSize(true);
        categoryLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        categoryAdapter = new BusinessAdapter(this, list);

        categoriesRecyclerView.setLayoutManager(categoryLayoutManager);
        categoriesRecyclerView.setAdapter(categoryAdapter);

    }

}