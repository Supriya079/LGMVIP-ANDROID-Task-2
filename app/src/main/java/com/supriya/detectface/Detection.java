package com.supriya.detectface;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class Detection extends Application {
    public final static String result= "Result_Text";
    public final static String resultDialog= "Result_Dailog";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
