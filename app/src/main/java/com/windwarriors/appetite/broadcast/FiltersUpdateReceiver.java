package com.windwarriors.appetite.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.windwarriors.appetite.utils.Constants;

public class FiltersUpdateReceiver extends BroadcastReceiver {

    private String TAG = "Appetite.FiltersUpdateReceiver";
    private FiltersUpdateReceiver.OnReceive customOnReceive;

    public interface OnReceive {
        void onReceive();
    }

    public FiltersUpdateReceiver( FiltersUpdateReceiver.OnReceive customOnReceive ){
        this.customOnReceive = customOnReceive;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle data = intent.getExtras();

        if (Constants.BROADCAST_FILTER_UPDATE.equals(action)) {
            if (this.customOnReceive != null) {
                this.customOnReceive.onReceive();
            }
        } else {
            System.out.println(TAG + " Nothing to do for action " + action);
        }
    }

    public IntentFilter getIntentFilter() {
        System.out.println(TAG + " registering service state change receiver...");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_FILTER_UPDATE);
        return intentFilter;
    }
}
