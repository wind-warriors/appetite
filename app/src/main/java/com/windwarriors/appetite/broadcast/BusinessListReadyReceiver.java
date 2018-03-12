package com.windwarriors.appetite.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;

import com.windwarriors.appetite.utils.Constants;

public class BusinessListReadyReceiver extends BroadcastReceiver {
    private String TAG = "Appetite.BusinessListReadyReceiver";
    RecyclerView.Adapter businessAdapter;

    public BusinessListReadyReceiver(RecyclerView.Adapter businessAdapter){
        this.businessAdapter = businessAdapter;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //Bundle data = intent.getExtras();

        if (Constants.BROADCAST_BUSINESS_LIST_READY.equals(action)) {
            businessAdapter.notifyDataSetChanged();
            //} else if (Constants.BROADCAST_BUSINESS_READY.equals(action)) {

        } else {
            System.out.println(TAG + " Nothing to do for action " + action);
        }
    }

    public IntentFilter getIntentFilter() {
        System.out.println(TAG + " registering service state change receiver...");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_BUSINESS_LIST_READY);
        return intentFilter;
    }
}
