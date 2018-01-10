package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Formatter;

/**
 * Created by jarvizu on 09/01/2018.
 */

public class PatchStatusOrderTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "PatchStatusOrderTask";
    private static final String METHOD = "PATCH";
    private static final String JSON_OBJECT_ERROR = "StatusCode";

    private Context context;
    private StatusOrderTaskListener statusOrderTaskListener;
    private JSONObject params;
    private UserPreferences userPreferences;
    private String adminToken;
    private boolean isError = false;

    public PatchStatusOrderTask(Context context, JSONObject params) {
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
            String format = formatter.format(UrlHelper.PATCH_ORDER_STATUS, params.get(ExtrasHelper.ORDER_JSON_OBJECT_ID)).toString();
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.d(DEBUG_TAG, jsonObject.toString());
        Order order = null;

        try {
            if (jsonObject == null) {
                statusOrderTaskListener.statusErrorResponse(context.getResources().getString(R.string.cases_status_error));
                isError = true;
            } else if (jsonObject.get(ExtrasHelper.ORDER_JSON_OBJECT_ID).toString().equals(null)) {
                statusOrderTaskListener.statusErrorResponse(context.getResources().getString(R.string.cases_status_error));
                isError = true;
            } else if (jsonObject.has(ExtrasHelper.LOGIN_JSON_OBJECT_TOKEN)) {
                order = new Order();
                order.setOrderStatus((Order.caseStatus) params.get(ExtrasHelper.ORDER_JSON_OBJECT_STATUS));
                Log.d(DEBUG_TAG, order.getOrderStatus().toString());
                isError = false;
            }

            if (isError == false) {
                statusOrderTaskListener.statusSuccessResponse(order);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        statusOrderTaskListener.statusErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setStatusOrderTaskListener(StatusOrderTaskListener statusOrderTaskListener) {
        this.statusOrderTaskListener = statusOrderTaskListener;
    }

    public interface StatusOrderTaskListener {
        void statusErrorResponse(String error);

        void statusSuccessResponse(Order order);
    }
}
