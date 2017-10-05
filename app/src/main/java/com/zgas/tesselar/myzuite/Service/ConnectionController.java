package com.zgas.tesselar.myzuite.Service;

import android.util.Log;

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

public class ConnectionController {

    private static final String DEBUG_TAG = "ConnectionController";
    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String AUTH_KEY = "Bearer ";
    private static final String AUTH_BODY = "00D0x000000CmVa!ARgAQK_q2ur710MF65SsciLYIPZiUiFGaRaRkhOQDhQtTE72W0D531OIwPAx.4vwncmACZDCiWr.DVO47vq7mHcRAOyPQQMe";

    private URL url;
    private String method;
    private String basicAuth;
    private JSONObject params;

    private JSONObject jsonObject;

    private HttpURLConnection httpURLConnection;

    private static final int TIMEOUT = 10000;

    public ConnectionController(URL url, String method) {
        this.url = url;
        this.method = method;
    }

    public ConnectionController(URL url, String method, JSONObject params) {
        this.url = url;
        this.method = method;
        this.params = params;
    }

    public JSONObject execute() throws FileNotFoundException, SocketTimeoutException {

        try {
            url = new URL(url.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setRequestProperty(CONTENT_TYPE, "application/json; charset=utf-8");
            String encodedAuth = AUTH_KEY + AUTH_BODY;
            httpURLConnection.setRequestProperty(AUTHORIZATION, encodedAuth);
            if (httpURLConnection.getRequestMethod().equals("POST")) {
                httpURLConnection.setDoOutput(true);
            } else if (httpURLConnection.getRequestMethod().equals("GET")) {
                httpURLConnection.setDoOutput(false);
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
            Log.d(DEBUG_TAG, "Estatuuuuus: " + String.valueOf(status));
            Log.d(DEBUG_TAG, encodedAuth);
            Log.d(DEBUG_TAG, "Request: " + httpURLConnection.getRequestProperty(AUTHORIZATION));
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
