package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zgas.tesselar.myzuite.Model.User;

/**
 * Created by jarvizu on 01/09/2017.
 */

public class UserPreference {

    private static final String USER = "USER_INFO";
    private SharedPreferences sharedPreferences;
    private Context context;

    /**
     * @param context
     */
    public UserPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
    }

    public User getUser() {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(USER, "null"), User.class);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER, user.toJson());
        editor.apply();
    }
}
