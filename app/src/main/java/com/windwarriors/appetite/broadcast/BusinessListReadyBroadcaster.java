package com.windwarriors.appetite.broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.utils.Constants;

import java.util.ArrayList;

public class BusinessListReadyBroadcaster {
    private Context context;

    public BusinessListReadyBroadcaster(Context context){
        this.context = context;
    }

    public void sendBroadcastBusinessListReady(ArrayList<Business> businessList) {
        //Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_SESSION_TIMEOUT");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_BUSINESS_LIST_READY);

        Bundle data = new Bundle();
        data.putParcelableArrayList(Constants.BUSINESS_LIST, businessList);

        intent.putExtras(data);

        context.sendBroadcast(intent);
    }
}
