package com.windwarriors.appetite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.windwarriors.appetite.YelpService.YelpService;
import com.windwarriors.appetite.adapter.BusinessAdapter;
import com.windwarriors.appetite.adapter.SimpleDividerItemDecoration;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.service.BusinessService;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.windwarriors.appetite.utils.Helper.setUserGreetingTextView;


public class BusinessListActivity extends AppCompatActivity {
    private RecyclerView businessRecyclerView;
    private RecyclerView.Adapter businessAdapter;
    private RecyclerView.LayoutManager businessLayoutManager;

    //private BusinessService businessService;
    private BusinessService businessService = new BusinessService(this);
    private YelpService yelpService = new YelpService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);


        //ArrayList<Business> list = businessService.fetchBusinessesFromYelp();
        ArrayList<Business> list = fetchBusinessesFromYelp();

        businessRecyclerView = findViewById(R.id.recycler_view_business_list);
        businessRecyclerView.setHasFixedSize(true);
        businessLayoutManager = new LinearLayoutManager(getApplicationContext());
        businessAdapter = new BusinessAdapter(this, list);
        businessAdapter.notifyDataSetChanged();


        businessRecyclerView.setLayoutManager(businessLayoutManager);
        businessRecyclerView.setAdapter(businessAdapter);
        businessRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

    }

    private ArrayList<Business> fetchBusinessesFromYelp() {
        final ArrayList<Business> businessList = new ArrayList<>();

        // TODO: apply user filters
        // using, for example, yelpService.put("radius", 1000);
        // parameters available at
        // https://www.yelp.com/developers/documentation/v3/business_search
        yelpService.mockParameters();

        yelpService.search(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                businessList.addAll(yelpService.getSearchResults());
                businessAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Unable to retrieve businesses: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return businessList;
    }
    @Override
    protected void onDestroy() {
        businessService.destroy();
        yelpService.onDestroy();
        super.onDestroy();
    }
}