package com.windwarriors.appetite.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.windwarriors.appetite.utils.Constants;

public class RangeUpdateReceiver extends BroadcastReceiver {
    private String TAG = "Appetite.RangeUpdateReceiver";
    private RangeUpdateReceiver.OnReceive customOnReceive;

    public interface OnReceive {
        void onReceive(int range);
    }

    public RangeUpdateReceiver( RangeUpdateReceiver.OnReceive customOnReceive ){
        this.customOnReceive = customOnReceive;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle data = intent.getExtras();

        int range = data.getInt(Constants.FILTER_RANGE);
        if (Constants.BROADCAST_RANGE_UPDATE.equals(action)) {
            System.out.println(TAG + ".onReceive:" + String.valueOf(range));
            if (this.customOnReceive != null) {
                this.customOnReceive.onReceive(range);
            }

        } else {
            System.out.println(TAG + " Nothing to do for action " + action);
        }

    }

    public IntentFilter getIntentFilter() {
        System.out.println(TAG + " registering service state change receiver...");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_RANGE_UPDATE);
        return intentFilter;
    }
}
