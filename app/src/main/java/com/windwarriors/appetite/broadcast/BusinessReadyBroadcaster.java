package com.windwarriors.appetite.broadcast;

import android.content.Context;
import android.content.Intent;

import com.windwarriors.appetite.utils.Constants;

public class BusinessReadyBroadcaster {
    private Context context;

    public BusinessReadyBroadcaster(Context context) {
        this.context = context;
    }

    public void sendBroadcastBusinessReady() {
        //Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_SESSION_TIMEOUT");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_BUSINESS_READY);

        //Bundle data = new Bundle();
        //data.putString(Constants.TIMEOUT_MESSAGE, timeoutMessage);
        //intent.putExtras(data);

        context.sendBroadcast(intent);
    }
}
