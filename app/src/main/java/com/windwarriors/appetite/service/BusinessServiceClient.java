package com.windwarriors.appetite.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.windwarriors.appetite.utils.Constants;

public class BusinessServiceClient {

    private Context context;

    public BusinessServiceClient(Context context) {
        this.context = context;
    }

    public void refreshBusinessList() {
        Bundle data = new Bundle();
        data.putInt(Constants.BROADCAST_BUSINESS_SERVICE, Constants.BROADCAST_REFRESH_BUSINESS_LIST);
        Intent intent = new Intent(context, BusinessService.class);
        intent.putExtras(data);
        context.startService(intent);
    }

    /*
    public void updateBusinessList() {
        businessService.loadBusinessList(currentLat, currentLong);
    }
    */

    public void updateTerm(String term) {
        Bundle data = new Bundle();
        data.putInt(Constants.BROADCAST_BUSINESS_SERVICE, Constants.BROADCAST_UPDATE_TERM);
        data.putString(Constants.BROADCAST_TERM, term);
        Intent intent = new Intent(context, BusinessService.class);
        intent.putExtras(data);
        context.startService(intent);
    }

    //TODO: BusinessService should update location on its own ( delete this function )
    public void updateLocation(Double latitude, Double longitude) {
        Bundle data = new Bundle();
        data.putInt(Constants.BROADCAST_BUSINESS_SERVICE, Constants.BROADCAST_UPDATE_LOCATION);
        data.putDouble(Constants.BROADCAST_LATITUDE, latitude);
        data.putDouble(Constants.BROADCAST_LONGITUDE, longitude);
        Intent intent = new Intent(context, BusinessService.class);
        intent.putExtras(data);
        context.startService(intent);
    }

    public void loadBusiness(String id) {
        Bundle data = new Bundle();
        data.putInt(Constants.BROADCAST_BUSINESS_SERVICE, Constants.BROADCAST_LOAD_BUSINESS);
        data.putString(Constants.BROADCAST_ID, id);
        Intent intent = new Intent(context, BusinessService.class);
        intent.putExtras(data);
        context.startService(intent);
    }

    public void updateRange(int range) {
        Bundle data = new Bundle();
        data.putInt(Constants.BROADCAST_BUSINESS_SERVICE, Constants.BROADCAST_UPDATE_RANGE);
        data.putInt(Constants.BROADCAST_RANGE_UPDATE, range);
        Intent intent = new Intent(context, BusinessService.class);
        intent.putExtras(data);
        context.startService(intent);
    }

    public void destroy() {
        Bundle data = new Bundle();
        data.putInt(Constants.BROADCAST_BUSINESS_SERVICE, Constants.BROADCAST_DESTROY_BUSINESS_SERVICE);
        Intent intent = new Intent(context, BusinessService.class);
        intent.putExtras(data);
        context.startService(intent);
    }
}
