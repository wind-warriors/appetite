package com.windwarriors.appetite;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.windwarriors.appetite.adapter.BusinessAdapter;
import com.windwarriors.appetite.adapter.SimpleDividerItemDecoration;
import com.windwarriors.appetite.broadcast.BusinessListReadyReceiver;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.service.BusinessServiceClient;
import com.windwarriors.appetite.service.SharedPreferencesService;
import com.windwarriors.appetite.utils.Constants;

import java.util.ArrayList;

import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_SEARCH_RANGE;
import static com.windwarriors.appetite.utils.Helper.OpenFilterDialog;
import static com.windwarriors.appetite.utils.Helper.OpenRangeDialog;


public class BusinessListActivity extends AppCompatActivity {
    private RecyclerView businessRecyclerView;
    private RecyclerView.Adapter businessAdapter;
    private RecyclerView.LayoutManager businessLayoutManager;

    private final String TAG = "Appetite.ListActivity";

    //private ArrayList<Business> businessList;
    private BusinessListReadyReceiver businessListReadyReceiver;
    private BusinessServiceClient businessServiceClient;

    private LocationManager locationManager;

    private double currentLong = Constants.CENTENNIAL_LONGITUDE;
    private double currentLat = Constants.CENTENNIAL_LATITUDE;
    private SharedPreferencesService sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);

        sharedPreferences = new SharedPreferencesService(this);

        // Set greeting for logged in user
        //setUserGreetingTextView(this, R.id.greeting);

        businessRecyclerView = findViewById(R.id.recycler_view_business_list);
        businessRecyclerView.setHasFixedSize(true);
        businessLayoutManager = new LinearLayoutManager(getApplicationContext());

        businessRecyclerView.setLayoutManager(businessLayoutManager);
        businessRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        businessServiceClient = new BusinessServiceClient(this);
        businessListReadyReceiver = new BusinessListReadyReceiver(new BusinessListReadyReceiver.OnReceive() {
            @Override
            public void onReceive(ArrayList<Business> updatedBusinessList) {
                Log.v(TAG, "ListReadyReceiver" + updatedBusinessList.size() + " " + businessAdapter);
                if (businessAdapter == null) {
                    //businessList = updatedBusinessList;
                    businessAdapter = new BusinessAdapter(BusinessListActivity.this, updatedBusinessList);
                    businessRecyclerView.setAdapter(businessAdapter);
                }
                businessAdapter.notifyDataSetChanged();
            }
        });

//
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(myToolbar);

        registerBusinessListReadyBroadcastReceiver();

        handleLocationPermissions();


        if (sharedPreferences.getFromSharedPreferences(SHARED_PREFERENCES_SEARCH_RANGE).equals("")){
            // Set default range to 10 KM
            sharedPreferences.saveToSharedPreferences(SHARED_PREFERENCES_SEARCH_RANGE, "10");
        }

        businessServiceClient.updateLocation(Constants.CENTENNIAL_LATITUDE, Constants.CENTENNIAL_LONGITUDE);
        businessServiceClient.refreshBusinessList();
    }

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

        }
        /*
        else {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates("gps", 1000, 1, this);

        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.business_list_menu, menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.business_list_menu, menu);

        MenuItem item = menu.findItem(R.id.menu_search);  //findViewById(R.id.menu_search);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String term) {
                //Toast.makeText(getApplicationContext(), "onQueryTextSubmit: "+s, Toast.LENGTH_SHORT).show();

                businessServiceClient.updateTerm(term);
                businessServiceClient.refreshBusinessList();
                //mockBusinessServiceloadSearchByTermResult(term);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String term) {
                //Toast.makeText(getApplicationContext(), "onQueryTextChange: "+s, Toast.LENGTH_SHORT).show();

                businessServiceClient.updateTerm(term);
                businessServiceClient.refreshBusinessList();
                //mockBusinessServiceloadSearchByTermResult(term);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent next;

        switch (id){
            case R.id.id_range:
                OpenRangeDialog(this);
                break;
            case R.id.id_list:
                break;
            case R.id.id_map:
                next = new Intent( BusinessListActivity.this, MapsActivity.class);
                startActivity(next);
                break;
            case R.id.action_filter:
                OpenFilterDialog(this);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void registerBusinessListReadyBroadcastReceiver() {
        registerReceiver(businessListReadyReceiver, businessListReadyReceiver.getIntentFilter());
    }

    @Override
    protected void onDestroy() {
        businessServiceClient.destroy();
        super.onDestroy();
        unregisterReceiver(businessListReadyReceiver);
    }

    /*
    private void mockBusinessServiceloadSearchByTermResult(String term) {
        businessList.clear();
        businessList.add(mockBusinessDetails());
        businessAdapter.notifyDataSetChanged();
    }

    private Business mockBusinessDetails() {
        Business mock = new Business();
        mock.setName("The Real McCoy Burgers and Pizza!");
        mock.setDistance("11 Km");
        mock.setAddress("11033 Markham Road, Scarborough, ON M1H 2Y5, Canada");
        mock.setFoodCategory(new String[]{"Chinese, Noodles"});
        mock.setRating(4.3);
        mock.setImageLink("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/healthy-snacks-1520621791.jpg?crop=1.00xw:1.00xh;0,0&resize=980:*");
        mock.setLatitude(MOCK_DETAIL_LATITUDE);
        mock.setLongitude(MOCK_DETAIL_LONGITUDE);

        return mock;
    }
    */
}