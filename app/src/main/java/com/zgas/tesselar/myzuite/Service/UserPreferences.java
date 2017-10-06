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
    private static final String SHARED_PREFERENCES = "userPreferences";
    private static final String LOGIN_EMAIL = "loginEmail";
    private static final String LOGIN_TOKEN = "loginToken";
    private static final String API_TOKEN = "apiToken";
    private static final String LOGIN_DATA = "loginData";
    private static final String USER_DATA = "userData";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserPreferences(Context context) {
        this.context = context;
    }

    public String getUserData() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_DATA, null);
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

    public User getUserObject() {
        Gson gson = new Gson();
        User user = gson.fromJson(getUserData(), User.class);
        return user;
    }

    public String getLoginData() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LOGIN_DATA, null);
    }

    public void createLoginSession(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED, true);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
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

    public Login getLoginObject() {
        Gson gson = new Gson();
        Login login = gson.fromJson(getLoginData(), Login.class);
        return login;
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
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit().clear().apply();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        Log.d(DEBUG_TAG, "User has logged out.");
    }

    public boolean isLoggedIn() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGGED, false);
    }

    public void setAdminToken(String token) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(API_TOKEN, token);
        editor.apply();
    }

    public String getAdminToken() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(API_TOKEN, null);
    }

}

