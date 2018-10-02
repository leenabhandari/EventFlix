package com.example.leena.mypills;

import android.app.Application;

import com.mmi.LicenceManager;

public class Map_APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LicenceManager.getInstance().setRestAPIKey("v29*********************qz");
        LicenceManager.getInstance().setMapSDKKey("bbjd**********************2iyc");

    }

}
