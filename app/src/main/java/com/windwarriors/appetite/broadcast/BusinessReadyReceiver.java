package com.windwarriors.appetite.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.windwarriors.appetite.model.Business;
import com.windwarriors.appetite.utils.Constants;

import java.util.ArrayList;

public class BusinessReadyReceiver extends BroadcastReceiver {
    private String TAG = "Appetite.BusinessListReadyReceiver";
    private BusinessReadyReceiver.OnReceive customOnReceive;

    public interface OnReceive {
        void onReceive( Business business);
    }

    public BusinessReadyReceiver(){
    }

    public BusinessReadyReceiver( BusinessReadyReceiver.OnReceive customOnReceive ){
        this.customOnReceive = customOnReceive;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle data = intent.getExtras();
        Business business;

        assert data != null;
        business = data.getParcelable(Constants.BUSINESS);
        assert business != null;
        if (Constants.BROADCAST_BUSINESS_READY.equals(action)) {
            System.out.println(TAG + ".onReceive:" + business);
            if (this.customOnReceive != null) {
                this.customOnReceive.onReceive(business);
            }

        } else {
            System.out.println(TAG + " Nothing to do for action " + action);
        }
    }

    public IntentFilter getIntentFilter() {
        System.out.println(TAG + " registering service state change receiver...");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_BUSINESS_READY);
        return intentFilter;
    }
}
