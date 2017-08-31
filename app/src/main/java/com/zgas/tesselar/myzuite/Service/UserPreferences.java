package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jarvizu on 31/08/2017.
 */

public class UserPreferences {

  /*  private static final String DEBUG_TAG = "UserPreferences";
    private static final String SHARED_PREFERENCES = "com.zgas.tesselar.myzuite.USER_PREFERENCES";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    private int PRIVATE_MODE = 0;
    private SharedPreferences.Editor editor;
    private Context context;
    private SharedPreferences sharedPreferences;

    public UserPreferences(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
    }*/

    public static void setUserID(Context context, int userID) {

        SharedPreferences mySharedPreferences =

                context.getSharedPreferences("USER", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = mySharedPreferences.edit();

        editor.putString("USER_ID", String.valueOf(userID));

        editor.apply();

    }

    public static String getUserID(Context context) {

        SharedPreferences mySharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);

        return mySharedPreferences.getString("USER_ID", null);

    }


}
