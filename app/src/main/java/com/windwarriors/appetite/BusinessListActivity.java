package com.windwarriors.appetite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.windwarriors.appetite.adapter.BusinessAdapter;
import com.windwarriors.appetite.adapter.SimpleDividerItemDecoration;
import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.broadcast.BusinessListReadyReceiver;
import com.windwarriors.appetite.service.BusinessService;

import java.util.ArrayList;

import static com.windwarriors.appetite.utils.Helper.setUserGreetingTextView;


public class BusinessListActivity extends AppCompatActivity {
    private RecyclerView businessRecyclerView;
    private RecyclerView.Adapter businessAdapter;
    private RecyclerView.LayoutManager businessLayoutManager;

    private final String TAG = "BusinessListActivity";
    private ArrayList<Business> businessList = new ArrayList<>();
    private BusinessService businessService;

    private BusinessListReadyReceiver businessListReadyReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);

        // Set greeting for logged in user
        setUserGreetingTextView(this, R.id.greeting);

        businessRecyclerView = findViewById(R.id.recycler_view_business_list);
        businessRecyclerView.setHasFixedSize(true);
        businessLayoutManager = new LinearLayoutManager(getApplicationContext());
        businessAdapter = new BusinessAdapter(this, businessList);
        businessRecyclerView.setAdapter(businessAdapter);
        businessRecyclerView.setLayoutManager(businessLayoutManager);
        businessRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        businessListReadyReceiver = new BusinessListReadyReceiver(businessAdapter);
        registerBusinessListReadyBroadcastReceiver();

        businessService = new BusinessService(this, businessList);
        businessService.loadBusinessList();
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
                break;
            case R.id.id_map:
                next = new Intent( BusinessListActivity.this, MapsActivity.class);
                startActivity(next);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void OpenRangeDialog() {
        BusinessListRangeDialog rangeDialog = new BusinessListRangeDialog();
        rangeDialog.show(getSupportFragmentManager(), "Range Dialog");
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
}