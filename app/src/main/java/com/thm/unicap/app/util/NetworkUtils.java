package com.thm.unicap.app.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }
}
