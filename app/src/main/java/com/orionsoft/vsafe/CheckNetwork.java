package com.orionsoft.vsafe;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckNetwork {

//  Check to make sure it is connected to a network
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
