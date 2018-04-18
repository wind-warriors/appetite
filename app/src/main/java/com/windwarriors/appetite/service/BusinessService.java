package com.windwarriors.appetite.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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

public class BusinessService extends Service implements LocationListener {

    private final String TAG = "AppetiteBusinessService";

    private YelpService yelpService;
    ArrayList<Business> businessList;
    private SharedPreferencesService spService;
    private BusinessListReadyBroadcaster businessListReadyBroadcaster;
    private BusinessReadyBroadcaster businessReadyBroadcaster;
    private int page = 0;

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
        super.onCreate();

        this.yelpService = new YelpService();
        this.businessList = new ArrayList<>();
        this.spService = new SharedPreferencesService(this);
        this.businessListReadyBroadcaster = new BusinessListReadyBroadcaster(this);
        this.businessReadyBroadcaster = new BusinessReadyBroadcaster(this);
        this.page = 0;
        handleLocationPermissions();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Bundle data = intent.getExtras();
            handleData(data);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int getResponseCode() {
        return 0;
    }

    public class BusinessServiceBinder extends Binder {
        public BusinessService getService() {
            return BusinessService.this;
        }
    }


    private void handleData(Bundle data) {
        int command = data.getInt(Constants.BROADCAST_BUSINESS_SERVICE);
        Log.d(TAG, "command=" + command);
        if (command == Constants.BROADCAST_REFRESH_BUSINESS_LIST) {
            loadBusinessList();
        } else if (command == Constants.BROADCAST_UPDATE_TERM) {
            String term = (String) data.get(Constants.BROADCAST_TERM);
            this.term(term);
        } else if (command == Constants.BROADCAST_UPDATE_LOCATION) {
            Double lat = (Double) data.get(Constants.BROADCAST_LATITUDE);
            Double lng = (Double) data.get(Constants.BROADCAST_LONGITUDE);
            this.latitude(lat);
            this.longitude(lng);
            this.search();
        } else if (command == Constants.BROADCAST_LOAD_BUSINESS) {
            String id = (String) data.get(Constants.BROADCAST_ID);
            this.loadBusiness(id);
        } else if (command == Constants.BROADCAST_NEXT_PAGE) {
            this.nextPage();
        } else if (command == Constants.BROADCAST_DESTROY_BUSINESS_SERVICE) {
            stopSelf();
        } else {
            Log.w(TAG, "Ignoring Unknown Command! cmd=" + command);
        }
    }

    private void nextPage() {
        this.page++;
        this.offset( this.page * Constants.PAGE_SIZE );
        this.search();
    }

    private void resetPage() {
        this.page = 0;
        this.offset( 0 );
    }

    private void loadBusinessList() {
        //yelpService.mockParameters();
        //this.latitude(latitude);
        //this.longitude(longitude);
        this.resetPage();
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
                Toast.makeText(getApplicationContext(), "Unable to retrieve business: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void search() {
        final String sortBy = spService.getFromSharedPreferences(SHARED_PREFERENCES_SORTBY);
        yelpService.search(new YelpService.Callback<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse response) {
                businessList.clear();
                businessList.addAll(yelpService.getSearchResults());
                sort(sortBy);
                businessListReadyBroadcaster.sendBroadcastBusinessListReady(businessList);
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                String errorMessage = t.getMessage();
                Toast.makeText(getApplicationContext(), "Unable to retrieve businesses: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Location Listener related functions

    public void handleLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, " ERROR: Locations permission not set. Please set them on the first/main activity");
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            locationManager.requestLocationUpdates("gps", 1000, 1, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "onLocationChanged [" + location.getLatitude() + ", " + location.getLongitude() + "]");
        this.latitude(location.getLatitude());
        this.longitude(location.getLongitude());
        this.search();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    // YelpService-related functions

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

    public void offset(int offset) {
        yelpService.offset(String.valueOf(offset));
    }

    private void sort_by() {
        String sortBy = spService.getFromSharedPreferences(SHARED_PREFERENCES_SORTBY);

        if (!sortBy.equals("")) {
            String[] appetiteSortBy  = getResources().getStringArray(R.array.sortby_options_array);
            String[] yelpSortBy = getResources().getStringArray(R.array.sortby_options_array_yelp);
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

    private void sort(String sortBy) {
        if (!sortBy.equals("")) {
            if (sortBy.toLowerCase().contains("review")) Business.sortByMostReviewed(businessList);
            else if (sortBy.toLowerCase().contains("rating")) Business.sortByRating(businessList);
            else if (sortBy.toLowerCase().contains("distance")) Business.sortByDistance(businessList);
            else Log.w(TAG, "unknown sorting method. Skipping sorting!");
        }
    }

}