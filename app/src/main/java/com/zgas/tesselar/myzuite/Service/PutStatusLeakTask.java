package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Model.Leak;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UrlHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Formatter;

/**
 * Created by jarvizu on 27/01/2018.
 */

public class PutStatusLeakTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "PutStatusLeakTask";
    private static final String METHOD = "PUT";
    private static final String JSON_OBJECT_ERROR = "StatusCode";

    private Context context;
    private StatusLeakTaskListener statusLeakTaskListener;
    private JSONObject params;
    private UserPreferences userPreferences;
    private String adminToken;
    private boolean isError = false;

    public PutStatusLeakTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;
        try {
            adminToken = userPreferences.getAdminToken();
            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.PUT_LEAK_STATUS, params.get(ExtrasHelper.LEAK_JSON_OBJECT_ID)).toString();
            Log.d(DEBUG_TAG, format);
            URL url = new URL(format);
            ConnectionController connectionController = new ConnectionController(adminToken, url, METHOD, params);
            Log.d(DEBUG_TAG, METHOD);
            jsonObject = connectionController.execute();

            if (jsonObject == null) {
                cancel(true);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.d(DEBUG_TAG, jsonObject.toString());
        Leak leak = null;

        try {
            if (jsonObject == null) {
                statusLeakTaskListener.statusErrorResponse(context.getResources().getString(R.string.cases_status_error));
                isError = true;
            } else if (jsonObject.get(ExtrasHelper.LEAK_JSON_OBJECT_ID).toString().equals(null)) {
                statusLeakTaskListener.statusErrorResponse(context.getResources().getString(R.string.cases_status_error));
                isError = true;
            } else if (jsonObject.has(ExtrasHelper.LEAK_JSON_OBJECT_ID)) {
                leak = new Leak();
                jsonObject.put(ExtrasHelper.LEAK_JSON_OBJECT_STATUS, params.get(ExtrasHelper.LEAK_JSON_OBJECT_STATUS));
                leak.setLeakStatus((Leak.leakStatus) jsonObject.get(ExtrasHelper.LEAK_JSON_OBJECT_STATUS));
                Log.d(DEBUG_TAG, jsonObject.get(ExtrasHelper.LEAK_JSON_OBJECT_STATUS_UPDATE).toString());
                Log.d(DEBUG_TAG, leak.getLeakStatus().toString());
                isError = false;
            }

            if (isError == false) {
                statusLeakTaskListener.statusSuccessResponse(leak);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        statusLeakTaskListener.statusErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setStatusLeakTaskListener(StatusLeakTaskListener statusLeakTaskListener) {
        this.statusLeakTaskListener = statusLeakTaskListener;
    }

    public interface StatusLeakTaskListener {
        void statusErrorResponse(String error);

        void statusSuccessResponse(Leak leak);
    }
}
