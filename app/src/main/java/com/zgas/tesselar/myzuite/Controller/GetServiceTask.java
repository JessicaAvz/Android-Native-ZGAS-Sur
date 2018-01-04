package com.zgas.tesselar.myzuite.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Model.Order;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.ExtrasHelper;
import com.zgas.tesselar.myzuite.Utilities.UrlHelper;

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
 * Created by jarvizu on 03/01/2018.
 */

public class GetServiceTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "GetServiceTask";
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
    private String adminToken;
    private Order aService;
    private List<Order> servicesList;
    private UserPreferences userPreferences;

    public GetServiceTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.wait_cases_message), false);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;
        try {
            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.GET_SERVICE_URL, params.get(USER_ID)).toString();
            adminToken = userPreferences.getAdminToken();
            Log.d(DEBUG_TAG, "Url del usuario: " + format);
            Log.d(DEBUG_TAG, "Token del admin: " + adminToken);

            URL url = new URL(format);
            ConnectionController connection = new ConnectionController(adminToken, url, METHOD);
            jsonObject = connection.execute();

            if (jsonObject == null) {
                cancel(true);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            cancel(true);
        } catch (FileNotFoundException e) {
            cancel(true);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            cancel(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        progressDialog.dismiss();

        servicesList = new ArrayList<>();
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
                    aService = new Order();
                    aService.setCaseId(caseObject.get(JSON_OBJECT_ID).toString());
                    Log.d(DEBUG_TAG, "Id del caso: " + aService.getCaseId());
                    aService.setCaseUserId(userPreferences.getUserObject().getUserId());
                    Log.d(DEBUG_TAG, "Id del operador: " + aService.getCaseUserId());
                    aService.setCaseTimeScheduled(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED));
                    Log.d(DEBUG_TAG, "Hora programada: " + aService.getCaseTimeScheduled());
                    //aService.setCaseTimeSeen(caseObject.getString(CASE_TIME_SEEN));
                    aService.setCaseTimeAssignment(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    Log.d(DEBUG_TAG, "Hora de asignación: " + caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    aService.setCaseAccountName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME));
                    Log.d(DEBUG_TAG, "Cliente: " + aService.getCaseAccountName());
                    aService.setCaseAddress(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Dirección: " + aService.getCaseAddress());
                    aService.setCaseContactName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_CONTACT_NAME));
                    Log.d(DEBUG_TAG, "Cuenta: " + aService.getCaseAccountName());
                    aService.setCaseServiceType(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE));
                    Log.d(DEBUG_TAG, "Tipo de servicio: " + aService.getCaseServiceType());
                    aService.setCasePaymentMethod(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD));
                    Log.d(DEBUG_TAG, "Método de pago: " + aService.getCasePaymentMethod());

                    String caseType = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_TYPE).toString();
                    String caseStatus = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_STATUS).toString();
                    String casePriority = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY).toString();

                    if (caseType.equals(Order.caseTypes.ORDER.toString())) {
                        aService.setCaseType(Order.caseTypes.ORDER);
                    } else if (caseType.equals(Order.caseTypes.CUT.toString())) {
                        aService.setCaseType(Order.caseTypes.CUT);
                    } else if (caseType.equals(Order.caseTypes.RECONNECTION.toString())) {
                        aService.setCaseType(Order.caseTypes.RECONNECTION);
                    }
                    Log.d(DEBUG_TAG, "Tipo de caso: " + aService.getCaseType());

                    if (caseStatus.equals(Order.caseStatus.INPROGRESS.toString())) {
                        aService.setCaseStatus(Order.caseStatus.INPROGRESS);
                    } else if (caseStatus.equals(Order.caseStatus.CANCELLED.toString())) {
                        aService.setCaseStatus(Order.caseStatus.CANCELLED);
                    } else if (caseStatus.equals(Order.caseStatus.FINISHED.toString())) {
                        aService.setCaseStatus(Order.caseStatus.FINISHED);
                    } else if (caseStatus.equals(Order.caseStatus.NEW.toString())) {
                        aService.setCaseStatus(Order.caseStatus.NEW);
                    } else if (caseStatus.equals(Order.caseStatus.ASSIGNED.toString())) {
                        aService.setCaseStatus(Order.caseStatus.ASSIGNED);
                    } else if (caseStatus.equals(Order.caseStatus.ACCEPTED.toString())) {
                        aService.setCaseStatus(Order.caseStatus.ACCEPTED);
                    } else if (caseStatus.equals(Order.caseStatus.RETIRED.toString())) {
                        aService.setCaseStatus(Order.caseStatus.RETIRED);
                    } else if (caseStatus.equals(Order.caseStatus.CLOSED)) {
                        aService.setCaseStatus(Order.caseStatus.CLOSED);
                    }
                    Log.d(DEBUG_TAG, "Status del caso : " + aService.getCaseStatus());

                    if (casePriority.equals(Order.casePriority.HIGH.toString())) {
                        aService.setCasePriority(Order.casePriority.HIGH);
                    } else if (casePriority.equals(Order.casePriority.LOW.toString())) {
                        aService.setCasePriority(Order.casePriority.LOW);
                    } else if (casePriority.equals(Order.casePriority.MEDIUM.toString())) {
                        aService.setCasePriority(Order.casePriority.MEDIUM);
                    }
                    Log.d(DEBUG_TAG, "Prioridad del caso: " + aService.getCasePriority());

                    isError = false;
                    servicesList.add(aService);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (serviceTaskListener != null) {
                serviceTaskListener.getServicesSuccessResponse(servicesList);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        serviceTaskListener.getServicesError(context.getResources().getString(R.string.connection_error));
    }

    public void setServiceTaskListener(ServiceTaskListener getServiceTaskListener) {
        this.serviceTaskListener = getServiceTaskListener;
    }

    public interface ServiceTaskListener {
        void getServicesError(String error);

        void getServicesSuccessResponse(List<Order> serviceList);

    }
}
