package com.windwarriors.appetite;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.windwarriors.appetite.adapter.BusinessAdapter;
import com.windwarriors.appetite.adapter.SimpleDividerItemDecoration;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.broadcast.BusinessListReadyReceiver;
import com.windwarriors.appetite.service.BusinessService;
import com.windwarriors.appetite.service.SharedPreferencesService;
import com.windwarriors.appetite.utils.Constants;

import java.util.ArrayList;

import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_SEARCH_RANGE;
import static com.windwarriors.appetite.utils.Helper.OpenFilterDialog;
import static com.windwarriors.appetite.utils.Helper.OpenRangeDialog;
import static com.windwarriors.appetite.utils.Helper.setUserGreetingTextView;


public class BusinessListActivity extends AppCompatActivity implements LocationListener {
    private RecyclerView businessRecyclerView;
    private RecyclerView.Adapter businessAdapter;
    private RecyclerView.LayoutManager businessLayoutManager;

    private final String TAG = "Appetite.BusinessListActivity";
    private ArrayList<Business> businessList = new ArrayList<>();
    private BusinessService businessService;

    private BusinessListReadyReceiver businessListReadyReceiver;
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
        businessAdapter = new BusinessAdapter(this, businessList);
        businessRecyclerView.setAdapter(businessAdapter);
        businessRecyclerView.setLayoutManager(businessLayoutManager);
        businessRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        businessListReadyReceiver = new BusinessListReadyReceiver(new BusinessListReadyReceiver.OnReceive() {
            @Override
            public void onReceive(ArrayList<Business> businessList) {
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

        businessService = new BusinessService(this, businessList);
        businessService.loadBusinessList(currentLat, currentLong);
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

        } else {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates("gps", 1000, 1, this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.business_list_menu, menu);
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
        businessService.destroy();
        super.onDestroy();
        unregisterReceiver(businessListReadyReceiver);
    }

    @Override
    public void onLocationChanged(Location location) {

        //Toast.makeText(this,  location.getLatitude()+",\n"+location.getLongitude(),
        //        Toast.LENGTH_SHORT).show();

        currentLat = location.getLatitude();
        currentLong = location.getLongitude();

        BusinessService businessService = new BusinessService(this, businessList);
        businessService.loadBusinessList(currentLat, currentLong);

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
}