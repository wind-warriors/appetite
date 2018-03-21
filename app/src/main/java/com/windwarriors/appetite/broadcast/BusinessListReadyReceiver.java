package com.windwarriors.appetite.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.utils.Constants;

import java.util.ArrayList;

public class BusinessListReadyReceiver extends BroadcastReceiver {
    private String TAG = "Appetite.BusinessListReadyReceiver";
    private OnReceive customOnReceive;

    public interface OnReceive {
        void onReceive(ArrayList<Business> businessList);
    }

    public BusinessListReadyReceiver(){
    }

    public BusinessListReadyReceiver( OnReceive customOnReceive ){
        this.customOnReceive = customOnReceive;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle data = intent.getExtras();
        ArrayList<Business> businessList;

        assert data != null;
        businessList = data.getParcelableArrayList(Constants.BUSINESS_LIST);
        assert businessList != null;
        if (Constants.BROADCAST_BUSINESS_LIST_READY.equals(action)) {
            System.out.println(TAG + " onReceive:" + businessList.size());
            //businessAdapter.notifyDataSetChanged();
            if (this.customOnReceive != null) {
                this.customOnReceive.onReceive(businessList);
            }

        } else {
            System.out.println(TAG + " Nothing to do for action " + action);
        }
    }

    public IntentFilter getIntentFilter() {
        System.out.println(TAG + " registering...");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_BUSINESS_LIST_READY);
        return intentFilter;
    }
}
