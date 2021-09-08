package com.cristiancollazos.paybird.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = NotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context objContext, Intent objIntent) {
        Log.i(TAG, "Jello!");
    }

}


