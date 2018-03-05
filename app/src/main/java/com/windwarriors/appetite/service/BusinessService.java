package com.windwarriors.appetite.service;

import android.content.Context;
import android.widget.Toast;

import com.windwarriors.appetite.YelpService.YelpService;
import com.windwarriors.appetite.model.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessService {

    private Context context;
    private YelpService yelpService;

    public BusinessService(Context context) {
        this.yelpService = new YelpService();
        this.context = context;
    }

    public List<Business> search() {
        ArrayList<Business> businessList = new ArrayList<>();

        // TODO: Use Yelp logic to get search result and save it into businessList result

        return businessList;
    }

    public Business getBusinessDetails( int businessId){
        Business result = new Business();

        //TODO: Use Yelp logic to get whole data for a specific business

        return result;
    }

    public Business getBusinessList( int businessId, int range){
        Business result = new Business();

        //TODO: Use Yelp logic to get list of restaurant, providing only data for listing purposes

        return result;
    }

    public ArrayList<Business> fetchBusinessesFromYelp() {
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
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "Unable to retrieve businesses: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return businessList;
    }

    public ArrayList mockBusinesses() {
        ArrayList<Business> list = new ArrayList<>();

        String mockImageLink = "http://del.h-cdn.co/assets/15/37/640x552/gallery-1441895894-weeknight-dinner-squash-salad.jpg";
        String mockImageLink2 = "https://images.unsplash.com/photo-1503764654157-72d979d9af2f?ixlib=rb-0.3.5&s=004ac76e65f0b5708b0f04523ea9c6de&auto=format&fit=crop&w=1953&q=80";
        String mockImageLink3 = "https://images.unsplash.com/photo-1485963631004-f2f00b1d6606?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=a98ac47048f530b6d587279d52c13ab7&auto=format&fit=crop&w=1868&q=80";
        String mockImageLink4 = "https://static.independent.co.uk/s3fs-public/styles/story_large/public/thumbnails/image/2018/01/12/12/healthy-avo-food.jpg";

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

        return list;
    }

    public void destroy(){
        yelpService.onDestroy();
    }
}
