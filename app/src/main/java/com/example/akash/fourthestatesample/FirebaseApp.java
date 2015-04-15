package com.example.akash.fourthestatesample;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;

/**
 * Created by akash on 8/4/15.
 */
public class FirebaseApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context=getApplicationContext();
        Firebase.setAndroidContext(this);
    }


    public static  Context getContext() {
        return context;
    }
}
