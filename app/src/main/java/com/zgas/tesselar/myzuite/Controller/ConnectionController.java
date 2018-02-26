package com.zgas.tesselar.myzuite.Controller;

import android.util.Log;

import com.zgas.tesselar.myzuite.Utilities.UrlHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Class that manages the connection between services, database and the asynchronous tasks used
 * within this project.
 *
 * @author jarvizu on 24/10/2017
 * @version 2018.0.9
 * @see JSONObject
 * @see HttpURLConnection
 * @see URL
 * @see android.os.AsyncTask
 */
public class ConnectionController {

    private static final String DEBUG_TAG = "ConnectionController";

    private URL url;
    private String method;
    private String adminToken;
    private JSONObject params;
    private JSONObject jsonObject;
    private HttpURLConnection httpURLConnection;

    private static final int TIMEOUT = 10000;

    /**
     * Constructor for the Connection Controller class.
     *
     * @param adminToken The token that will be used for all the petitions within this project.
     * @param url        The service url that will be accessed.
     * @param method     The method type that will be used for accesing the data (POST, GET, PUT...).
     */
    public ConnectionController(String adminToken, URL url, String method) {
        this.adminToken = adminToken;
        this.url = url;
        this.method = method;
    }

    /**
     * Constructor for the Connection Controller class, but that will need parameters in order to
     * make the connection with the services.
     *
     * @param adminToken The token that will be used for all the petitions within this project.
     * @param url        The service url that will be accessed.
     * @param method     The method type that will be used for accesing the data (POST, GET, PUT...).
     * @param params     JsonObject parameters that will be needed to make the conection
     *                   (email, password, etc...).
     */
    public ConnectionController(String adminToken, URL url, String method, JSONObject params) {
        this.adminToken = adminToken;
        this.url = url;
        this.method = method;
        this.params = params;
    }

    /**
     * This method has been abstracted as much as possible. This method is used for receiving
     * generic data, it doesn't ask what or how; just checks which method and parameters are
     * received and send them directly to the server. It's a class to use inside a thread
     * so we don't have to copy/paste this on all asyncTasks.
     *
     * @return The received JsonObject
     * @throws FileNotFoundException  Will be thrown when a file with the specified pathname
     *                                does not exist.
     * @throws SocketTimeoutException Will be thrown when a timeout has occurred on a socket
     *                                read or accept.
     */
    public JSONObject execute() throws FileNotFoundException, SocketTimeoutException {

        try {
            url = new URL(url.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method);
            String encodedAuth = UrlHelper.AUTH_KEY + adminToken;
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            if (httpURLConnection.getRequestMethod().equals("POST")) {
                //Set it to true if you want to send (output) a request body.
                httpURLConnection.setDoOutput(true);
            } else if (httpURLConnection.getRequestMethod().equals("GET")) {
                httpURLConnection.setDoOutput(false);
                httpURLConnection.setRequestProperty("Authorization", encodedAuth);
            } else if (httpURLConnection.getRequestMethod().equals("PUT")) {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Authorization", encodedAuth);
                httpURLConnection.setRequestProperty("X-HTTP-Method-Override", "PUT");
            }

            httpURLConnection.setConnectTimeout(TIMEOUT);
            httpURLConnection.setReadTimeout(TIMEOUT);
            httpURLConnection.connect();

            Log.d(DEBUG_TAG, "Url is connected.");

            if (params != null) {
                OutputStreamWriter os = new OutputStreamWriter(httpURLConnection.getOutputStream());
                os.write(params.toString());
                os.flush();
            }

            int status = httpURLConnection.getResponseCode();
            if (status >= 200 && status < 300) {

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();

                String line;
                if (bufferedReader != null) {
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                }
                bufferedReader.close();

                Object json = new JSONTokener(stringBuffer.toString()).nextValue();

                if (json instanceof JSONObject) {
                    jsonObject = new JSONObject(stringBuffer.toString());

                } else if (json instanceof JSONArray) {
                    jsonObject = new JSONObject();
                    jsonObject.put("jsonArray", new JSONArray(stringBuffer.toString()));
                }

            } else if (status >= 500) {
                jsonObject = new JSONObject();
                jsonObject.put("error", "Error en el servidor.");
                Log.d(DEBUG_TAG, "Server Error");
            } else {
                jsonObject = new JSONObject();
                jsonObject.put("error", status);
                Log.d(DEBUG_TAG, "Status: " + status);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            httpURLConnection.disconnect();
        } catch (JSONException e) {
            e.printStackTrace();
            httpURLConnection.disconnect();
        }

        httpURLConnection.disconnect();
        return jsonObject;
    }
}
