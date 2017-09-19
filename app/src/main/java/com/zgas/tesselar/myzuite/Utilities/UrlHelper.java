package com.zgas.tesselar.myzuite.Utilities;

import java.util.Formatter;

/**
 * Created by jarvizu on 19/09/2017.
 */

public class UrlHelper {

    public static final String URL = "0.0.0.0";
    private static final String API_URL = "api/v1/mobile";

    public static String getUrl(String controller) {
        Formatter formatter = new Formatter();

        return formatter.format(URL, API_URL, controller).toString();
    }

}
