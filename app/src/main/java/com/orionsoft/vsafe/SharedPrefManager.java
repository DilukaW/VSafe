package com.orionsoft.vsafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.orionsoft.vsafe.model.User;

public class SharedPrefManager {

    // The constants
    private static final String sharedPref = "UserSharedPref";
    private static final String nic = "keyNIC";
    private static final String firstName = "keyFName";
    private static final String lastName = "keyLName";
    private static final String mobNumber = "keyMobNumber";
    private static final String email = "keyEmail";

    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    // This method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(nic, user.getNICNumber());
        editor.putString(firstName, user.getFirstName());
        editor.putString(lastName, user.getLastName());
        editor.putString(mobNumber, user.getMobNumber());
        editor.putString(email, user.getEmailAddress());
        editor.apply();
    }

    // This method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(nic, null) != null;
    }

    // This method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(nic, null),
                sharedPreferences.getString(firstName, null),
                sharedPreferences.getString(lastName, null),
                sharedPreferences.getString(mobNumber, null),
                sharedPreferences.getString(email, null)
        );
    }

    // This method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginWithMobActivity.class));
    }

}
