package com.dohuukhang.coolmate;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class CoolmateApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance("https://coolmate-578b6-default-rtdb.asia-southeast1.firebasedatabase.app").setPersistenceEnabled(true);
    }
}
