package com.windwarriors.appetite.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
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

public class BusinessService extends Service { //implements LocationListener {

    private final String TAG = "AppetiteBusinessService";

    //private Context context;
    private YelpService yelpService;
    ArrayList<Business> businessList;
    private SharedPreferencesService spService;
    private BusinessListReadyBroadcaster businessListReadyBroadcaster;
    private BusinessReadyBroadcaster businessReadyBroadcaster;
    //private LocationManager locationManager;

    /*
    public BusinessService(Context context, ArrayList<Business> businessList) {
        this.yelpService = new YelpService();
        //this.context = context;
        this.businessList = businessList;
        this.spService = new SharedPreferencesService(context);
        this.businessListReadyBroadcaster = new BusinessListReadyBroadcaster(context);
        this.businessReadyBroadcaster = new BusinessReadyBroadcaster(context);
    }

    public BusinessService() {
    }
    */

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
        super.onCreate();

        this.yelpService = new YelpService();
        //this.context = context;
        this.businessList = new ArrayList<>();
        this.spService = new SharedPreferencesService(this);
        this.businessListReadyBroadcaster = new BusinessListReadyBroadcaster(this);
        this.businessReadyBroadcaster = new BusinessReadyBroadcaster(this);

        //handleLocationPermissions();

        //notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //NotificationDecorator notificationDecorator = new NotificationDecorator(this, notificationMgr);
        //ChatEventHandler chatEventHandler = new ChatEventHandler(new BroadcastSender(this),
        //        new ChatMessageStore(this), notificationDecorator);
        //connectionManager = new ConnectionManager(this, chatEventHandler);

        //PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        //wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Bundle data = intent.getExtras();
            handleData(data);
            //if (!wakeLock.isHeld()) {
            //    Log.v(TAG, "acquiring wake lock");
            //    wakeLock.acquire();
            //}
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
        //notificationMgr.cancelAll();
        //Log.v(TAG, "releasing wake lock");
        //wakeLock.release();
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
        /*
            if(connectionManager.isConnected()){ // reconnect if already connected
                connectionManager.disconnectFromServer();
            }
            connectionManager.connectToServer(serverUri, myName);
        } else if (command == CMD_LEAVE_CHAT) {
            connectionManager.leaveChat(myName);
            connectionManager.disconnectFromServer();
            stopSelf();
        } else if (command == CMD_SEND_MESSAGE) {
            String message = (String) data.get(KEY_MESSAGE_TEXT);
            connectionManager.attemptSend(myName, message);
        */
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
        } else if (command == Constants.BROADCAST_DESTROY_BUSINESS_SERVICE) {
            stopSelf();
        } else {
            Log.w(TAG, "Ignoring Unknown Command! cmd=" + command);
        }
    }

    public void loadBusinessList() {
        //yelpService.mockParameters();
        //this.latitude(latitude);
        //this.longitude(longitude);
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
                Toast.makeText(getApplicationContext(), "Unable to retrieve businesses: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Location Listener related functions

    /*
    public void handleLocationPermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.MY_PERMISSIONS_ACCESS_FINE_LOCATION);

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.MY_PERMISSIONS_ACCESS_COARSE_LOCATION);

            handleLocationPermissions();

        } else {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates("gps", 1000, 1, this);

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        //Toast.makeText(this,  location.getLatitude()+",\n"+location.getLongitude(),
        //        Toast.LENGTH_SHORT).show();

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
    */

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

    public void offset(String offset) {
        yelpService.offset(offset);
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
