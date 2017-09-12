package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zgas.tesselar.myzuite.Controller.Activity.LoginActivity;
import com.zgas.tesselar.myzuite.Model.User;

/**
 * Created by jarvizu on 01/09/2017.
 */

public class UserPreferences {

    private static final String USER = "USER_INFO";
    private static final String IS_LOGGED = "IsLogged";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    /**
     * @param context
     */
    public UserPreferences(Context context) {
        this.context = context;
        mSharedPreferences = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public User getUser() {
        Gson gson = new Gson();
        return gson.fromJson(mSharedPreferences.getString(USER, "Null"), User.class);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER, user.toJson());
        editor.apply();
    }
}
