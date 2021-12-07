package com.example.shopsmart.Until;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CheckConnected {
    public static boolean haveNetworkConnection(Context context) {
        boolean wifi = false;
        boolean mobile = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo networkInfo : networkInfos) {
            if (networkInfo.getTypeName().equals("WIFI")) {
                if (networkInfo.isConnected()) {
                    wifi = true;
                }
            }
            if (networkInfo.getTypeName().equals("MOBILE")) {
                if (networkInfo.isConnected()) {
                    mobile = true;
                }
            }
        }
        return wifi | mobile;
    }
    public static void ShowToastLong(Context context,String notify){
        Toast.makeText(context,notify,Toast.LENGTH_SHORT).show();
    }
}
