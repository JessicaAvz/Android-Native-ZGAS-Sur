package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Controller.ConnectionController;
import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UrlHelper;
import com.zgas.tesselar.myzuite.Utilities.UserPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Class that communicates with the service and will push the result to the Order model list.
 *
 * @author jarvizu on 03/01/2018
 * @version 2018.0.9
 * @see AsyncTask
 * @see Order
 * @see JSONObject
 * @see UserPreferences
 * @see ServiceTaskListener
 */
public class GetServiceTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private final String DEBUG_TAG = getClass().getSimpleName();
    private static final String SERVICES_ARRAY = "MeasuredOrders";
    private static final String METHOD = "GET";
    private static final String USER_ID = "Id";
    private static final String JSON_OBJECT_ID = "Id";
    private static final String JSON_OBJECT_ERROR = "errorCode";

    private ServiceTaskListener serviceTaskListener;
    private Context context;
    private ProgressDialog progressDialog;
    private JSONObject params;
    private boolean isError = false;
    private UserPreferences userPreferences;

    /**
     * Constructor for the GetServiceTask. Additionally, we have an UserPreferences class reference
     * so we can obtain the user data.
     *
     * @param context Current context of the application.
     * @param params  Parameters that will be sent to the service.
     */
    public GetServiceTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.wait_cases_message), false);
    }

    /**
     * @param urls
     * @return
     */
    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;
        try {
            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.GET_SERVICE_URL, params.get(USER_ID)).toString();
            String adminToken = userPreferences.getAdminToken();
            Log.d(DEBUG_TAG, "Url del usuario: " + format);
            Log.d(DEBUG_TAG, "Token del admin: " + adminToken);

            URL url = new URL(format);
            ConnectionController connection = new ConnectionController(adminToken, url, METHOD, null, context);
            jsonObject = connection.execute();

            if (jsonObject == null) {
                cancel(true);
            }

        } catch (MalformedURLException | SocketTimeoutException e) {
            e.printStackTrace();
            cancel(true);
        } catch (FileNotFoundException e) {
            cancel(true);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * @param jsonObject
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        progressDialog.dismiss();

        List<Order> servicesList = new ArrayList<>();
        JSONArray servicesArray;
        try {
            if (jsonObject == null) {
                serviceTaskListener.getServicesError(jsonObject.getString(JSON_OBJECT_ERROR));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("400")) {
                    serviceTaskListener.getServicesError(context.getResources().getString(R.string.cases_data_error));
                    isError = true;
                }
            } else if (jsonObject.has(SERVICES_ARRAY)) {
                Log.d(DEBUG_TAG, "HOLA, SI SIRVE");
                servicesArray = jsonObject.getJSONArray(SERVICES_ARRAY);

                for (int i = 0; i < servicesArray.length(); i++) {
                    JSONObject caseObject = servicesArray.getJSONObject(i);
                    Order aService = new Order();
                    aService.setOrderId(caseObject.get(JSON_OBJECT_ID).toString());
                    Log.d(DEBUG_TAG, "Id del caso: " + aService.getOrderId());
                    aService.setOrderUserId(userPreferences.getUserObject().getUserId());
                    Log.d(DEBUG_TAG, "Id del operador: " + aService.getOrderUserId());
                    aService.setOrderTimeScheduled(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED));
                    Log.d(DEBUG_TAG, "Hora programada: " + aService.getOrderTimeScheduled());
                    //aService.setOrderTimeSeen(caseObject.getString(CASE_TIME_SEEN));
                    aService.setOrderTimeAssignment(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    Log.d(DEBUG_TAG, "Hora de asignación: " + caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    aService.setOrderAccountName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME));
                    Log.d(DEBUG_TAG, "Cliente: " + aService.getOrderAccountName());
                    aService.setOrderAddress(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Dirección: " + aService.getOrderAddress());
                    aService.setOrderContactName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_CONTACT_NAME));
                    Log.d(DEBUG_TAG, "Cuenta: " + aService.getOrderAccountName());
                    aService.setOrderServiceType(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE));
                    Log.d(DEBUG_TAG, "Tipo de servicio: " + aService.getOrderServiceType());
                    aService.setOrderPaymentMethod(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD));
                    Log.d(DEBUG_TAG, "Método de pago: " + aService.getOrderPaymentMethod());
                    aService.setOrderNotice(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_NOTICE));
                    Log.d(DEBUG_TAG, "Aviso: " + aService.getOrderNotice());

                    aService.setOrderType(caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_TYPE).toString());
                    aService.setOrderStatus(caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_STATUS).toString());
                    aService.setOrderPriority(caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY).toString());

                    if (aService.getOrderType().equals(String.valueOf(R.string.order_type_order))) {
                        aService.setOrderType(String.valueOf(R.string.order_type_order));
                    } else if (aService.getOrderType().equals(String.valueOf(R.string.order_type_cut))) {
                        aService.setOrderType(String.valueOf(R.string.order_type_cut));
                    } else if (aService.getOrderType().equals(String.valueOf(R.string.order_type_reconnection))) {
                        aService.setOrderType(String.valueOf(R.string.order_type_reconnection));
                    }
                    Log.d(DEBUG_TAG, "Tipo de caso: " + aService.getOrderType());

                    if (aService.getOrderStatus().equals(String.valueOf(R.string.order_status_in_progress))) {
                        aService.setOrderStatus(String.valueOf(R.string.order_status_in_progress));
                    } else if (aService.getOrderStatus().equals(String.valueOf(R.string.order_status_canceled))) {
                        aService.setOrderStatus(String.valueOf(R.string.order_status_canceled));
                    } else if (aService.getOrderStatus().equals(String.valueOf(R.string.order_status_finished))) {
                        aService.setOrderStatus(String.valueOf(R.string.order_status_finished));
                    } else if (aService.getOrderStatus().equals(String.valueOf(R.string.order_status_new))) {
                        aService.setOrderStatus(String.valueOf(R.string.order_status_new));
                    } else if (aService.getOrderStatus().equals(String.valueOf(R.string.order_status_assigned))) {
                        aService.setOrderStatus(String.valueOf(R.string.order_status_assigned));
                    } else if (aService.getOrderStatus().equals(String.valueOf(R.string.order_status_accepted))) {
                        aService.setOrderStatus(String.valueOf(R.string.order_status_accepted));
                    } else if (aService.getOrderStatus().equals(String.valueOf(R.string.order_status_retired))) {
                        aService.setOrderStatus(String.valueOf(R.string.order_status_retired));
                    } else if (aService.getOrderStatus().equals(String.valueOf(R.string.order_status_closed))) {
                        aService.setOrderStatus(String.valueOf(R.string.order_status_closed));
                    }
                    Log.d(DEBUG_TAG, "Status del caso : " + aService.getOrderStatus());

                    if (aService.getOrderPriority().equals(String.valueOf(R.string.order_priority_high))) {
                        aService.setOrderPriority(String.valueOf(R.string.order_priority_high));
                    } else if (aService.getOrderPriority().equals(String.valueOf(R.string.order_priority_medium))) {
                        aService.setOrderPriority(String.valueOf(R.string.order_priority_medium));
                    } else if (aService.getOrderPriority().equals(String.valueOf(R.string.order_priority_low))) {
                        aService.setOrderPriority(String.valueOf(R.string.order_priority_low));
                    }
                    Log.d(DEBUG_TAG, "Prioridad del caso: " + aService.getOrderPriority());

                    isError = false;
                    servicesList.add(aService);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (serviceTaskListener != null) {
                if (serviceTaskListener != null) {
                    serviceTaskListener.getServicesSuccessResponse(servicesList);
                }
            }
        }
    }

    /**
     *
     */

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        serviceTaskListener.getServicesError(context.getResources().getString(R.string.connection_error));
    }

    /**
     * @param getServiceTaskListener
     */
    public void setServiceTaskListener(ServiceTaskListener getServiceTaskListener) {
        this.serviceTaskListener = getServiceTaskListener;
    }

    /**
     *
     */
    public interface ServiceTaskListener {
        void getServicesError(String error);

        void getServicesSuccessResponse(List<Order> serviceList);

    }
}
