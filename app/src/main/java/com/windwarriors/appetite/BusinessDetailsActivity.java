package com.windwarriors.appetite;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.windwarriors.appetite.adapter.PhotoAdapter;
import com.windwarriors.appetite.broadcast.BusinessReadyReceiver;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.service.BusinessServiceClient;
import com.windwarriors.appetite.utils.DownloadImageTask;

import java.util.Arrays;
import java.util.List;

import static com.windwarriors.appetite.utils.Constants.BUSINESS_DISTANCE;
import static com.windwarriors.appetite.utils.Constants.BUSINESS_ID;
import static com.windwarriors.appetite.utils.Constants.MOCK_DETAIL_LATITUDE;
import static com.windwarriors.appetite.utils.Constants.MOCK_DETAIL_LONGITUDE;
import static com.windwarriors.appetite.utils.Helper.OpenRangeDialog;

public class BusinessDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{

    MapView mMapView;
    BusinessReadyReceiver businessReadyReceiver;
    BusinessServiceClient businessServiceClient;
    Business currentBusiness;
    String businessDistance;

    TextView businessName;
    TextView foodCategory;
//    TextView rating;
    TextView reviews;

    TextView address;
    TextView distance;
    ImageView foodImage;
    ImageView ratingStar;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        // Set action bar and status bar transparent
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Bundle data = getIntent().getExtras();
        progressBar = findViewById(R.id.loadingProgress);

        if( data != null ){
            String businessId = data.getString(BUSINESS_ID);
            businessDistance = data.getString(BUSINESS_DISTANCE);

            businessServiceClient = new BusinessServiceClient(this);
            businessReadyReceiver = new BusinessReadyReceiver(new BusinessReadyReceiver.OnReceive() {
                @Override
                public void onReceive(Business business) {
                    displayBusiness(business);
                }
            });
            registerReceiver(businessReadyReceiver, businessReadyReceiver.getIntentFilter());

            businessServiceClient.loadBusiness(businessId);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(businessReadyReceiver);
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMinZoomPreference(14.0f);

        LatLng latLng = new LatLng(currentBusiness.getLatitude(), currentBusiness.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


        MarkerOptions markerOptions = new MarkerOptions()
                .position( latLng )
                .title(currentBusiness.getName())
                .snippet(currentBusiness.getName())
                .anchor(0.5f, 0.5f);

        googleMap.addMarker(markerOptions);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.business_list_details_menu, menu);
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
                next = new Intent( BusinessDetailsActivity.this, BusinessListActivity.class);
                startActivity(next);
                break;
            case R.id.id_map:
                next = new Intent( BusinessDetailsActivity.this, MapsActivity.class);
                startActivity(next);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void loadMap() {
        mMapView = findViewById(R.id.businness_map);
        if( mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    private void displayBusiness(Business business) {
        currentBusiness = business;
        loadMap();
        ratingStar =  findViewById(R.id.details_imageStar);

        businessName = findViewById(R.id.details_business_name);
        businessName.setText(currentBusiness.getName());

        foodCategory = findViewById(R.id.details_food_category);
        String priceAndCategory;
        if (currentBusiness.getPrice() == null)
        {
            priceAndCategory = currentBusiness.listFoodCategories();
        }
        else
        {
            priceAndCategory = currentBusiness.getPrice() + "  -  " + currentBusiness.listFoodCategories();
        }

        foodCategory.setText(priceAndCategory);

//        rating = findViewById(R.id.details_rating);
//        rating.setText(String.valueOf(currentBusiness.getRating()));

        reviews = findViewById(R.id.details_total_reviews);
        reviews.setText( String.valueOf(currentBusiness.getTotalReviews()) + " reviews");

        address = findViewById(R.id.details_address);
        address.setText(currentBusiness.getAddress());

        distance = findViewById(R.id.details_distance);
        distance.setText(businessDistance);


        if (currentBusiness.getRating().equals(5.0)) {
            ratingStar.setImageResource(R.drawable.stars_regular_5);
        } else if (currentBusiness.getRating().equals(4.5)) {
            ratingStar.setImageResource(R.drawable.stars_regular_4_half);
        } else if (currentBusiness.getRating().equals(4.0)) {
            ratingStar.setImageResource(R.drawable.stars_regular_4);
        } else if (currentBusiness.getRating().equals(3.5)) {
            ratingStar.setImageResource(R.drawable.stars_regular_3_half);
        } else if (currentBusiness.getRating().equals(3.0)) {
            ratingStar.setImageResource(R.drawable.stars_regular_3);
        } else if (currentBusiness.getRating().equals(2.5)) {
            ratingStar.setImageResource(R.drawable.stars_regular_2_half);
        } else if (currentBusiness.getRating().equals(2.0)) {
            ratingStar.setImageResource(R.drawable.stars_regular_2);
        } else if (currentBusiness.getRating().equals(1.5)) {
            ratingStar.setImageResource(R.drawable.stars_regular_1_half);
        } else if (currentBusiness.getRating().equals(1.0)) {
            ratingStar.setImageResource(R.drawable.stars_regular_1);
        } else {
            ratingStar.setImageResource(R.drawable.stars_regular_0);}

        // Set photos from Photo adapter to the viewPager so we can scroll horizontally photos
        ViewPager viewPager = findViewById(R.id.photos_viewpager);
        PhotoAdapter adapter = new PhotoAdapter(this, Arrays.asList(currentBusiness.getPhotos()) );//mockBusinessPhotos());
        viewPager.setAdapter(adapter);

        foodImage = findViewById(R.id.details_image);
        new DownloadImageTask(foodImage, progressBar).execute(currentBusiness.getImageLink());
    }

    // Method to Mock data from a specific business
    private Business mockBusinessDetails(String businessId) {
        Business mock = new Business();
        mock.setName("The Real McCoy Burgers and Pizza!");
        //mock.setDistance("11 Km");
        mock.setAddress("11033 Markham Road, Scarborough, ON M1H 2Y5, Canada");
        mock.setFoodCategory(new String[]{"Chinese, Noodles"});
        mock.setRating(4.3);
        mock.setImageLink("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/healthy-snacks-1520621791.jpg?crop=1.00xw:1.00xh;0,0&resize=980:*");
        mock.setLatitude(MOCK_DETAIL_LATITUDE);
        mock.setLongitude(MOCK_DETAIL_LONGITUDE);

        return mock;
    }
    private List<String> mockBusinessPhotos(){
        return Arrays.asList(
                "https://images.pexels.com/photos/239975/pexels-photo-239975.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "http://s3.amazonaws.com/Boomeon/posts/heros/000/007/570/original/Bobby_Gujral.jpg?1495688026",
                "https://www.mtl.org/sites/default/files/2017-07/20671H.jpg",
                "https://www.stclouds.com/wp-content/uploads/2017/11/cropped-night4.jpg",
                "https://www.prague.eu/object/1693/10608592-724315724290937-1028515969827308394-o.jpg",
                "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/lead-restaurants-1508259029.jpg?crop=1.00xw:1.00xh;0,0&resize=1600:*",
                "http://www.appareilarchitecture.com/wp-content/uploads/z_APPAREILarchitecture_Restaurant-BATTUTO_Qu%C3%A9bec_2016_%C2%A9F%C3%A9lix-Michaud_HR_006-2cover.jpg");
    }

}
