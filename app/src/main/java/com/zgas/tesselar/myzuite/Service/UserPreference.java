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

    /**
     * Save all user data
     *
     * @param user
     */
    public void setUserData(User user) {
        if (user != null) {
            Gson gson = new Gson();
            String userString = gson.toJson(user);

            sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_DATA, userString);
            editor.apply();

            Log.d(DEBUG_TAG, "Object usuario ha sido guardado.");
        } else {
            Log.d(DEBUG_TAG, "Objeto User es null.");
        }
    }

    /**
     * @return User data
     */
    public String getUserData() {
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_DATA, null);
    }

    /**
     * @return User object
     */
    public User getUserObject() {
        Gson gson = new Gson();
        User user = gson.fromJson(getUserData(), User.class);
        return user;
    }


    /**
     * This value is to keep the session
     *
     * @return boolean is authenticated on the app
     */
    public boolean isAuthenticated() {
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(AUTHENTICATED, false);
    }

    /**
     * Save the value to keep session
     *
     * @param authenticated
     */
    public void setAuthenticated(boolean authenticated) {
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(AUTHENTICATED, authenticated);
        editor.apply();
    }

    /**
     * Clear all data in sharedPreferences
     */
    public void closeSession() {
        context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE)
                .edit().clear().apply();

    }
}
