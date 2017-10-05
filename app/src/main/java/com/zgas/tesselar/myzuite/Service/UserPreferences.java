package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.zgas.tesselar.myzuite.Controller.Activity.LoginActivity;
import com.zgas.tesselar.myzuite.Model.Login;
import com.zgas.tesselar.myzuite.Model.User;

/**
 * Created by jarvizu on 01/09/2017.
 */

public class UserPreferences {

    private static final String DEBUG_TAG = "UserPreferences";
    private static final String IS_LOGGED = "IsLoggedIn";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASS = "Password";
    private static final String SHARED_PREFERENCES = "sharedPreferences";
    private static final String LOGIN_EMAIL = "loginEmail";
    private static final String LOGIN_TOKEN = "loginToken";
    private static final String LOGIN_DATA = "loginData";
    private static final String USER_DATA = "userData";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserPreferences(Context context) {
        this.context = context;
    }

    public User getUserData() {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(USER_DATA, null), User.class);
    }

    public void setUserData(User user) {
        if (user != null) {
            Gson gson = new Gson();
            String userString = gson.toJson(user);

            sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_DATA, userString);
            editor.apply();

            Log.d(DEBUG_TAG, "Object user has been saved successfully");
        } else {
            Log.d(DEBUG_TAG, "Object user is null");
        }
    }

    public void createLoginSession(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public void setLoginData(Login login) {
        if (login != null) {
            Gson gson = new Gson();
            String loginString = gson.toJson(login);

            sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LOGIN_DATA, loginString);
            editor.apply();

            Log.d(DEBUG_TAG, "Object login has been saved successfully");

        } else {
            Log.d(DEBUG_TAG, "Object login is null");
        }
    }

    public Login getLoginData() {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(LOGIN_DATA, null), Login.class);
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
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGGED, false);
    }

    public String getLoginToken() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LOGIN_TOKEN, null);
    }

    public String getLoginEmail() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LOGIN_EMAIL, null);
    }
}

