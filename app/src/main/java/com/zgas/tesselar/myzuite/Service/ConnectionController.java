package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.util.Log;

import com.zgas.tesselar.myzuite.R;

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
 * Created by jarvizu on 19/09/2017.
 */

public class ConnectionController {

    private static final String DEBUG_TAG = "ConnectionController";
    private static final int TIMEOUT = 10000;

    private Context context;
    private URL url;
    private String method;
    private JSONObject params;
    private JSONObject jsonObject;
    private HttpURLConnection httpURLConnection;

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
            //httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setConnectTimeout(TIMEOUT);
            httpURLConnection.setReadTimeout(TIMEOUT);
            httpURLConnection.connect();
            Log.d(DEBUG_TAG, "Connected");

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

                String line = null;
                if (bufferedReader != null) {
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                }

                Object json = new JSONTokener(stringBuffer.toString()).nextValue();

                if (json instanceof JSONObject) {
                    jsonObject = new JSONObject(stringBuffer.toString());

                } else if (json instanceof JSONArray) {
                    jsonObject = new JSONObject();
                    jsonObject.put("jsonArray", new JSONArray(stringBuffer.toString()));
                }

            } else if (status <= 500) {
                jsonObject = new JSONObject();
                jsonObject.put(context.getResources().getString(R.string.error), context.getResources().getString(R.string.server_error));
                Log.d(DEBUG_TAG, context.getResources().getString(R.string.server_error));
            } else {
                jsonObject = new JSONObject();
                jsonObject.put(context.getResources().getString(R.string.error), status);
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

