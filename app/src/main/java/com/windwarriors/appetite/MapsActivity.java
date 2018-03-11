package com.windwarriors.appetite;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int DIALOG_REQUEST = 9001;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (mapServicesAvailable() ) {
            initMap();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(14.0f);

        // Mock data that should come from Yelp Service
        mockRestaurantCoordinates(mMap);

        // Add an initial marker in downtown Toronto and move camera to that point
        LatLng yongeDundasSquare = new LatLng(43.6626165, -79.3901206);
        mMap.addMarker(new MarkerOptions().position(yongeDundasSquare).title("Yonge Dundas Square"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yongeDundasSquare));
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
                OpenRangeDialog();
                break;
            case R.id.id_list:
                next = new Intent( MapsActivity.this, BusinessListActivity.class);
                startActivity(next);
                break;
            case R.id.id_map:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // PRIVATE METHODS
    public boolean mapServicesAvailable() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result, this, DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, getString(R.string.error_connect_to_services), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap() {
        if (mMap == null) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        return (mMap != null);
    }

    public void OpenRangeDialog() {
        BusinessListRangeDialog rangeDialog = new BusinessListRangeDialog();
        rangeDialog.show(getSupportFragmentManager(), "Range Dialog");
    }

    private void mockRestaurantCoordinates(GoogleMap map) {
        List<Double> latitudes = Arrays.asList(43.6782714, 43.6782714, 43.6782714, 43.6726372, 43.6726372, 43.6704898);
        List<Double> longitudes = Arrays.asList(-79.3923463, -79.3923463, -79.3923463, -79.3880788, -79.3880788, -79.3952154 );
        List<String> names = Arrays.asList("Palace Restaurant", "Globe Bistro", "Peartree Restaurant", "Matisse Restaurant", "Blu Ristorante", "Trattoria Nervosa");

        int size = names.size();
        for (int i = 0; i < size; ++i) {
            LatLng coordinate = new LatLng(latitudes.get(i), longitudes.get(i));

            map.addMarker(new MarkerOptions()
                    .position(coordinate)
                    .title(names.get(i))
                    .snippet(names.get(i))
                    .anchor(0.5f, 0.5f));
        }
    }
}