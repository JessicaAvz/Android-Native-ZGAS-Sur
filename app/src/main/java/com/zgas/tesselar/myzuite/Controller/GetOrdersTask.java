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
 * Created by jarvizu on 20/09/2017.
 */

public class GetOrdersTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "GetOrdersTask";
    private static final String CASES_ARRAY = "Orders";
    private static final String DETAIL_ARRAY = "Detail";
    private static final String METHOD = "GET";
    private static final String USER_ID = "Id";
    private static final String JSON_OBJECT_ID = "Id";
    private static final String JSON_OBJECT_ERROR = "errorCode";

    private Context context;
    private JSONObject params;
    private UserPreferences userPreferences;
    private OrderTaskListener orderTaskListener;
    private ProgressDialog progressDialog;
    private boolean isError = false;
    private String adminToken;
    private Order aOrder;
    private List<Order> casesList;

    public GetOrdersTask(Context context, JSONObject params) {
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
            String format = formatter.format(UrlHelper.GET_CASES_URL, params.get(USER_ID)).toString();
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

        casesList = new ArrayList<>();
        JSONArray casesArray;

        try {
            if (jsonObject == null) {
                orderTaskListener.getCasesErrorResponse(jsonObject.getString(JSON_OBJECT_ERROR));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("400")) {
                    orderTaskListener.getCasesErrorResponse(context.getResources().getString(R.string.cases_data_error));
                    isError = true;
                }
            } else if (jsonObject.has(CASES_ARRAY)) {
                Log.d(DEBUG_TAG, "HOLA, SI SIRVE");
                casesArray = jsonObject.getJSONArray(CASES_ARRAY);

                for (int i = 0; i < casesArray.length(); i++) {
                    JSONObject caseObject = casesArray.getJSONObject(i);
                    aOrder = new Order();
                    aOrder.setCaseId(caseObject.get(JSON_OBJECT_ID).toString());
                    Log.d(DEBUG_TAG, "Id del caso: " + aOrder.getCaseId());
                    aOrder.setCaseUserId(userPreferences.getUserObject().getUserId());
                    Log.d(DEBUG_TAG, "Id del operador: " + aOrder.getCaseUserId());
                    aOrder.setCaseTimeScheduled(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED));
                    Log.d(DEBUG_TAG, "Hora programada: " + aOrder.getCaseTimeScheduled());
                    //aOrder.setCaseTimeSeen(caseObject.getString(CASE_TIME_SEEN));
                    aOrder.setCaseTimeAssignment(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    Log.d(DEBUG_TAG, "Hora de asignación: " + caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    aOrder.setCaseAccountName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME));
                    Log.d(DEBUG_TAG, "Cliente: " + aOrder.getCaseAccountName());
                    aOrder.setCaseAddress(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Dirección: " + aOrder.getCaseAddress());
                    aOrder.setCaseContactName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_CONTACT_NAME));
                    Log.d(DEBUG_TAG, "Cuenta: " + aOrder.getCaseAccountName());
                    aOrder.setCaseServiceType(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE));
                    Log.d(DEBUG_TAG, "Tipo de servicio: " + aOrder.getCaseServiceType());

                    String caseType = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_TYPE).toString();
                    String caseStatus = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_STATUS).toString();
                    String casePriority = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY).toString();

                    if (caseType.equals(Order.caseTypes.ORDER.toString())) {
                        aOrder.setCaseType(Order.caseTypes.ORDER);
                    } else if (caseType.equals(Order.caseTypes.CUT.toString())) {
                        aOrder.setCaseType(Order.caseTypes.CUT);
                    } else if (caseType.equals(Order.caseTypes.RECONNECTION.toString())) {
                        aOrder.setCaseType(Order.caseTypes.RECONNECTION);
                    }
                    Log.d(DEBUG_TAG, "Tipo de caso: " + aOrder.getCaseType());

                    if (caseStatus.equals(Order.caseStatus.INPROGRESS.toString())) {
                        aOrder.setCaseStatus(Order.caseStatus.INPROGRESS);
                    } else if (caseStatus.equals(Order.caseStatus.CANCELLED.toString())) {
                        aOrder.setCaseStatus(Order.caseStatus.CANCELLED);
                    } else if (caseStatus.equals(Order.caseStatus.FINISHED.toString())) {
                        aOrder.setCaseStatus(Order.caseStatus.FINISHED);
                    } else if (caseStatus.equals(Order.caseStatus.NEW.toString())) {
                        aOrder.setCaseStatus(Order.caseStatus.NEW);
                    } else if (caseStatus.equals(Order.caseStatus.ASSIGNED.toString())) {
                        aOrder.setCaseStatus(Order.caseStatus.ASSIGNED);
                    } else if (caseStatus.equals(Order.caseStatus.ACCEPTED.toString())) {
                        aOrder.setCaseStatus(Order.caseStatus.ACCEPTED);
                    } else if (caseStatus.equals(Order.caseStatus.RETIRED.toString())) {
                        aOrder.setCaseStatus(Order.caseStatus.RETIRED);
                    } else if (caseStatus.equals(Order.caseStatus.CLOSED)) {
                        aOrder.setCaseStatus(Order.caseStatus.CLOSED);
                    }
                    Log.d(DEBUG_TAG, "Status del caso : " + aOrder.getCaseStatus());

                    if (casePriority.equals(Order.casePriority.HIGH.toString())) {
                        aOrder.setCasePriority(Order.casePriority.HIGH);
                    } else if (casePriority.equals(Order.casePriority.LOW.toString())) {
                        aOrder.setCasePriority(Order.casePriority.LOW);
                    } else if (casePriority.equals(Order.casePriority.MEDIUM.toString())) {
                        aOrder.setCasePriority(Order.casePriority.MEDIUM);
                    }
                    Log.d(DEBUG_TAG, "Prioridad del caso: " + aOrder.getCasePriority());

                    isError = false;
                    casesList.add(aOrder);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (orderTaskListener != null) {
                orderTaskListener.getCasesSuccessResponse(casesList);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        orderTaskListener.getCasesErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setOrderTaskListener(OrderTaskListener getOrderTaskListener) {
        this.orderTaskListener = getOrderTaskListener;
    }

    public interface OrderTaskListener {
        void getCasesErrorResponse(String error);

        void getCasesSuccessResponse(List<Order> orderList);
    }
}
