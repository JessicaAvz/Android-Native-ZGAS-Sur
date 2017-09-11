package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.zgas.tesselar.myzuite.Model.User;

/**
 * Created by jarvizu on 01/09/2017.
 */

public class UserPreference {

    private static final String DEBUG_TAG = "UserPreference";
    private static final String MY_PREFERENCES = "MyPrefs";
    private static final String USER_DATA = "userData";
    private static final String AUTHENTICATED = "Authenticated";

    private Context context;
    private SharedPreferences sharedPreferences;

    public UserPreference(Context context) {
        this.context = context;
    }

    public void setUserData(User user) {
        if (user != null) {
            Gson gson = new Gson();
            String userString = gson.toJson(user);

            sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_DATA, userString);
            editor.apply();

            Log.d("SetUserData", "Object LoginModel has been saved successfully");
        } else {
            Log.d("SetUserData", "Object LoginModel is null");
        }
    }

    public String getUserData() {
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getString(USER_DATA, null);
    }
}
