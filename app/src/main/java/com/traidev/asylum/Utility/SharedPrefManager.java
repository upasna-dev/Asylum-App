package com.traidev.asylum.Utility;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "mySharedPreffXationAgra";

    private static SharedPrefManager mInstance;
    private Context ctx;

    public SharedPrefManager(Context ctx)
    {
        this.ctx = ctx;
    }


    public static synchronized SharedPrefManager getInstance(Context ctx)
    {
        if(mInstance == null)
        {
            mInstance = new SharedPrefManager(ctx);
        }
        return mInstance;
    }


    public void saveUser(String name, String mobile,String Uid,String City)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name",name);
        editor.putString("mobile",mobile);
        editor.putString("uid",Uid);
        editor.putString("city",Uid);

        editor.apply();
    }
    public void setCity(String city)
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("city",city);
        editor.apply();

    }


    public User getsUser()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("mobile",null),
                sharedPreferences.getString("uid",null),
                sharedPreferences.getString("city",null)
        );

    }

    public void logoutUser()
    {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }

}
