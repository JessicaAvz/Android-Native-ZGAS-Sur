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

    private static final String EXTRA_USER = "UserInfo";
    private static final String IS_LOGGED = "IsLoggedIn";
    private static final String LOGIN_DATA = "loginData";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASS = "Password";
    private static final String SHARED_PREFERENCES = "sharedPreferences";
    private static final String API_TOKEN = "apiToken";

    private static final String USER_DATA = "SetUserData";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public UserPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(EXTRA_USER, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public User getUser() {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(EXTRA_USER, "Null"), User.class);
    }

    public void setUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
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

    public void setLoginData(Login login) {
        if (login != null) {
            Gson gson = new Gson();
            String loginString = gson.toJson(login);

            sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LOGIN_DATA, loginString);
            editor.apply();

            Log.d(USER_DATA, "Object user has been saved successfully");

        } else {
            Log.d(USER_DATA, "Object user is null");
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
        return sharedPreferences.getBoolean(IS_LOGGED, false);
    }

    public void setToken(String apiKey) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(API_TOKEN, apiKey);
        editor.apply();
    }

    public String getToken() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getString(API_TOKEN, null);
    }
}
