package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Model.Order;
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
 * Class that communicates with the service and will push the result to the Leak model list.
 *
 * @author jarvizu on 26/02/2018.
 * @version 2018.0.9
 * @see AsyncTask
 * @see Order
 * @see JSONObject
 * @see UserPreferences
 * @see PutReviewOrderTask
 */
public class PutNewOrderTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "PutNewOrderTask";
    private static final String METHOD = "PUT";
    private static final String JSON_OBJECT_ERROR = "StatusCode";

    private Context context;
    private PutNewOrderTask.NewOrderTaskListener newOrderTaskListener;
    private JSONObject params;
    private UserPreferences userPreferences;
    private String adminToken;
    private boolean isError = false;

    /**
     * Constructor for the PutReviewOrderTask. Additionally, we have an UserPreferences class reference
     * so we can obtain the user data.
     *
     * @param context Current context of the application
     * @param params  Parameters that will be sent to the service.
     */
    public PutNewOrderTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;
        try {
            adminToken = userPreferences.getAdminToken();
            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.PUT_EXTRA_ORDER, params.get(ExtrasHelper.ORDER_JSON_OBJECT_ID)).toString();
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

    /**
     * Method that will show the task result on the user interface. It will receive the jsonObject
     * obtained on doInBackground method, and it will check if the jsonObject has an error or is
     * correct.
     * If an error occurs, the StatusLeakListener will manage it.
     * Else, the json data will be mapped with our Leak object and a pop up will be shown
     * on the user interface.
     *
     * @param jsonObject The user object that will be received.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        Log.d(DEBUG_TAG, jsonObject.toString());
        Order order = null;

        try {
            if (jsonObject == null) {
                newOrderTaskListener.newOrderErrorResponse(context.getResources().getString(R.string.cases_status_error));
                isError = true;
            } else if (jsonObject.get(ExtrasHelper.ORDER_JSON_OBJECT_ID).toString().equals(null)) {
                newOrderTaskListener.newOrderErrorResponse(context.getResources().getString(R.string.cases_status_error));
                isError = true;
            } else if (jsonObject.has(ExtrasHelper.ORDER_JSON_OBJECT_ID)) {
                order = new Order();
                jsonObject.put(ExtrasHelper.ORDER_JSON_OBJECT_ID, params.get(ExtrasHelper.ORDER_JSON_OBJECT_ID));
                jsonObject.put(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_NAME, params.get(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_NAME));
                jsonObject.put(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_PHONE, params.get(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_PHONE));
                Log.d(DEBUG_TAG, jsonObject.get(ExtrasHelper.ORDER_JSON_OBJECT_ID).toString());
                Log.d(DEBUG_TAG, jsonObject.get(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_NAME).toString());
                Log.d(DEBUG_TAG, jsonObject.get(ExtrasHelper.ORDER_JSON_EXTRA_ORDER_PHONE).toString());

                isError = false;
            }

            if (isError == false) {
                newOrderTaskListener.newOrderSuccessResponse(order);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * If the AsyncTask is cancelled, it will show an error response.
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        newOrderTaskListener.newOrderErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setNewOrderTaskListener(NewOrderTaskListener newOrderTaskListener) {
        this.newOrderTaskListener = newOrderTaskListener;
    }

    /**
     * Interface for managing the different outputs of the AsyncTask
     */
    public interface NewOrderTaskListener {
        void newOrderErrorResponse(String error);

        void newOrderSuccessResponse(Order order);
    }
}
