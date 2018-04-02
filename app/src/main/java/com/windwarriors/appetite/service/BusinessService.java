package com.windwarriors.appetite.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.windwarriors.appetite.R;
import com.windwarriors.appetite.broadcast.BusinessListReadyBroadcaster;
import com.windwarriors.appetite.broadcast.BusinessReadyBroadcaster;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.utils.Constants;
import com.yelp.fusion.client.models.SearchResponse;

import java.util.ArrayList;
import java.util.Arrays;

import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_FILTER_PRICE;
import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_SORTBY;

public class BusinessService {

    private Context context;
    private YelpService yelpService;
    ArrayList<Business> businessList;
    private SharedPreferencesService spService;
    private BusinessListReadyBroadcaster businessListReadyBroadcaster;
    private BusinessReadyBroadcaster businessReadyBroadcaster;

    public BusinessService(Context context, ArrayList<Business> businessList) {
        this.yelpService = new YelpService();
        this.context = context;
        this.businessList = businessList;
        this.spService = new SharedPreferencesService(context);
        this.businessListReadyBroadcaster = new BusinessListReadyBroadcaster(context);
        this.businessReadyBroadcaster = new BusinessReadyBroadcaster(context);
    }

    public void loadBusinessList(double latitude, double longitude) {
        //yelpService.mockParameters();
        this.latitude(latitude);
        this.longitude(longitude);
        this.radius();
        this.price();
        this.sort_by();
        this.search();
    }

    public void loadBusiness(String id) {
        yelpService.getBusiness(id, new YelpService.Callback<Business>() {
            @Override
            public void onResponse(Business business) {
                businessReadyBroadcaster.sendBroadcastBusinessReady(business);
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Toast.makeText(context.getApplicationContext(), "Unable to retrieve business: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void search() {
        yelpService.search(new YelpService.Callback<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse response) {
                businessList.clear();
                businessList.addAll(yelpService.getSearchResults());
                businessListReadyBroadcaster.sendBroadcastBusinessListReady(businessList);
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                String errorMessage = t.getMessage();
                Toast.makeText(context.getApplicationContext(), "Unable to retrieve businesses: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void clearParameters() {
        yelpService.clear();
    }

    public void destroy(){
        yelpService.onDestroy();
    }

    public void term(String term) {
        yelpService.term(term);
    }

    public void location( String location) {
        yelpService.location(location);
    }

    public void latitude(Double latitude) {
        yelpService.latitude(latitude);
    }

    public void longitude(Double longitude) {
        yelpService.longitude(longitude);
    }

    private void radius() {
        String spRange = spService.getFromSharedPreferences(Constants.SHARED_PREFERENCES_SEARCH_RANGE);
        if (!spRange.equals("")) {
            yelpService.radius(Integer.valueOf(spRange));
        }
    }

    public void categories(String categories) {
        yelpService.categories(categories);
    }

    public void locale(String locale) {
        yelpService.locale(locale);
    }

    public void limit(String limit) {
        yelpService.limit(limit);
    }

    public void offset(String offset) {
        yelpService.offset(offset);
    }

    private void sort_by() {
        String sortBy = spService.getFromSharedPreferences(SHARED_PREFERENCES_SORTBY);

        if (!sortBy.equals("")) {
            String[] appetiteSortBy = context.getResources().getStringArray(R.array.sortby_options_array);
            String[] yelpSortBy = context.getResources().getStringArray(R.array.sortby_options_array_yelp);
            Integer idx = Arrays.asList(appetiteSortBy).indexOf(sortBy);
            String yelp_sort_by = yelpSortBy[idx];

            yelpService.sort_by(yelp_sort_by);
        }
    }

    private void price() {
        String price = spService.getFromSharedPreferences(SHARED_PREFERENCES_FILTER_PRICE);

        if (!price.equals("")) {
            yelpService.price(price);
        }
    }

    public void open_now(Boolean open_now) {
        yelpService.open_now(open_now);
    }

    public void open_at(String open_at) {
        yelpService.open_at(open_at);
    }

    public void attributes(String attributes) {
        yelpService.attributes(attributes);
    }

    /*
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
    }*/
}
