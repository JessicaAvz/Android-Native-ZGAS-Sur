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

    private static final String EXTRA_USER = "UserInfo";
    private static final String IS_LOGGED = "IsLoggedIn";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASS = "Password";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserPreferences(Context context) {
        this.context = context;
        mSharedPreferences = context.getSharedPreferences(EXTRA_USER, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public User getUser() {
        Gson gson = new Gson();
        return gson.fromJson(mSharedPreferences.getString(EXTRA_USER, "Null"), User.class);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(EXTRA_USER, user.toJson());
        editor.apply();
    }

    public void createLoginSession(String email, String pass) {
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASS, pass);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(IS_LOGGED, false);
    }
}