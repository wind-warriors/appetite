package com.windwarriors.appetite;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.windwarriors.appetite.adapter.BusinessAdapter;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.utils.Constants;
import com.windwarriors.appetite.utils.DownloadImageTask;
import com.windwarriors.appetite.utils.Helper;

import static com.windwarriors.appetite.utils.Constants.BUSINESS_ID;
import static com.windwarriors.appetite.utils.Constants.MOCK_DETAIL_LATITUDE;
import static com.windwarriors.appetite.utils.Constants.MOCK_DETAIL_LONGITUDE;

public class BusinessDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{

    MapView mMapView;
    Business currentBusiness;

    TextView businessName;
    TextView foodCategory;
    TextView rating;
    TextView reviews;
    TextView address;
    TextView distance;
    ImageView foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        Bundle data = getIntent().getExtras();
        if( data != null ){
            String businessId = data.getString(BUSINESS_ID);

            // TODO: load data for the corresponding Business using service
            currentBusiness = mockBusinessDetails(businessId);

            mMapView = (MapView)findViewById(R.id.businness_map);
            if( mMapView != null){
                mMapView.onCreate(null);
                mMapView.onResume();
                mMapView.getMapAsync(this);
            }

            businessName = findViewById(R.id.business_name);
            businessName.setText(currentBusiness.getName());

            foodCategory = findViewById(R.id.food_category);
            foodCategory.setText(currentBusiness.getFoodCategory());

            rating = findViewById(R.id.rating);
            rating.setText(String.valueOf(currentBusiness.getRating()));

            reviews = findViewById(R.id.total_reviews);
            reviews.setText(currentBusiness.getTotalReviews());

            address = findViewById(R.id.address);
            address.setText(currentBusiness.getAddress());

            distance = findViewById(R.id.distance);
            distance.setText(currentBusiness.getDistance());

            foodImage = (ImageView)findViewById(R.id.image);
            new DownloadImageTask(foodImage).execute(currentBusiness.getImageLink());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMinZoomPreference(14.0f);

        LatLng mockLatLng = new LatLng(Constants.MOCK_DETAIL_LATITUDE, Constants.MOCK_DETAIL_LONGITUDE);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mockLatLng));

        LatLng latLng = new LatLng(currentBusiness.getLatitude(), currentBusiness.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position( latLng )
                .title(currentBusiness.getName())
                .snippet(currentBusiness.getName())
                .anchor(0.5f, 0.5f);

        googleMap.addMarker(markerOptions);

    }


    // Method to Mock data from a specific business
    private Business mockBusinessDetails(String businessId) {
        Business mock = new Business();
        mock.setName("The Real McCoy Burgers and Pizza!");
        mock.setDistance("11 Km");
        mock.setAddress("11033 Markham Road, Scarborough, ON M1H 2Y5, Canada");
        mock.setFoodCategory("Chinese, Noodles");
        mock.setRating(4.3);
        mock.setImageLink("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/healthy-snacks-1520621791.jpg?crop=1.00xw:1.00xh;0,0&resize=980:*");
        mock.setLatitude(MOCK_DETAIL_LATITUDE);
        mock.setLongitude(MOCK_DETAIL_LONGITUDE);

        return mock;
    }

}
