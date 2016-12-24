package com.kpk.appkios.AppConfig;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kpk.appkios.Model.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

/**
 * Created by AnonymousX on 04/12/2016.
 */

public class AppController extends Application {

    private static AppController ourInstance = new AppController();

    public static AppController getInstance() {
        return ourInstance;
    }

    private static Context mContext;

    public String key;

    public String jwtKey;

    public static Context getContext() {
        return mContext;
    }

    public User currentUser = null;

    public AppController() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        MultiDex.install(this);
//        Fabric.with(this, new Crashlytics());
        mContext = getApplicationContext();
        RealmConfiguration config = new RealmConfiguration.Builder(mContext).name("default")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        Realm theRealm = Realm.getDefaultInstance();
        RealmQuery<User> query = theRealm.where(User.class);
        User user = query.findFirst();
        if(user != null){
            currentUser = user;
        }
//        FacebookSdk.sdkInitialize(getApplicationContext());
        //crashlytic force crash
        //throw new RuntimeException("This is a crash");
    }
    public void setKey(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("key", key);
        editor.commit();
    }

    public String getKey(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getContext());
        return prefs.getString("key", "");
    }
}
