package com.example.leena.mypills;

import android.app.Application;

import com.mmi.LicenceManager;

public class Map_APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LicenceManager.getInstance().setRestAPIKey("v29j3asaqw97i6l6gvk4mnppon8fsaqz");
        LicenceManager.getInstance().setMapSDKKey("bbjdknymv7seoqvglfkje9928ggz2iyc");

    }

}
