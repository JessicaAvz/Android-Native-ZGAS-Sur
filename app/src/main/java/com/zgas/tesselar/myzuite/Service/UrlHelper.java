package com.zgas.tesselar.myzuite.Service;

import android.content.Context;

import java.util.Formatter;

/**
 * Created by jarvizu on 25/09/2017.
 */

public class UrlHelper {

    public static final String URL = "https://test.salesforce.com/services/oauth2/token";
    public static final String TOKEN_URL = "https://test.salesforce.com/services/oauth2/token";
    public static final String API_URL = "";

    public static String getUrlToken(String controller, Context context) {
        UserPreferences usersPreferences = new UserPreferences(context);
        Formatter formatter = new Formatter();
        return formatter.format(TOKEN_URL, controller, usersPreferences.getToken()).toString();
    }

    public static String getUrl(String controller) {
        Formatter formatter = new Formatter();
        return formatter.format(URL, controller).toString();
    }

}
