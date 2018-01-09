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

    /**
     * @param context
     * @param params
     */
    public GetOrdersTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
        userPreferences = new UserPreferences(context);
    }

    /**
     *
     */
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

    /**
     * @param jsonObject
     */
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
                    aOrder.setOrderId(caseObject.get(JSON_OBJECT_ID).toString());
                    Log.d(DEBUG_TAG, "Id del caso: " + aOrder.getOrderId());
                    aOrder.setOrderUserId(userPreferences.getUserObject().getUserId());
                    Log.d(DEBUG_TAG, "Id del operador: " + aOrder.getOrderUserId());
                    aOrder.setOrderTimeScheduled(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_SCHEDULED));
                    Log.d(DEBUG_TAG, "Hora programada: " + aOrder.getOrderTimeScheduled());
                    //aOrder.setOrderTimeSeen(caseObject.getString(CASE_TIME_SEEN));
                    aOrder.setOrderTimeAssignment(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    Log.d(DEBUG_TAG, "Hora de asignación: " + caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_TIME_ASSIGNMENT));
                    aOrder.setOrderAccountName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ACCOUNT_NAME));
                    Log.d(DEBUG_TAG, "Cliente: " + aOrder.getOrderAccountName());
                    aOrder.setOrderAddress(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_ADDRESS));
                    Log.d(DEBUG_TAG, "Dirección: " + aOrder.getOrderAddress());
                    aOrder.setOrderContactName(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_CONTACT_NAME));
                    Log.d(DEBUG_TAG, "Cuenta: " + aOrder.getOrderAccountName());
                    aOrder.setOrderServiceType(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_SERVICE_TYPE));
                    Log.d(DEBUG_TAG, "Tipo de servicio: " + aOrder.getOrderServiceType());
                    aOrder.setOrderPaymentMethod(caseObject.getString(ExtrasHelper.ORDER_JSON_OBJECT_PAYMENT_METHOD));
                    Log.d(DEBUG_TAG, "Tipo de pago: " + aOrder.getOrderPaymentMethod());

                    String caseType = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_TYPE).toString();
                    String caseStatus = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_STATUS).toString();
                    String casePriority = caseObject.get(ExtrasHelper.ORDER_JSON_OBJECT_PRIORITY).toString();

                    if (caseType.equals(Order.caseTypes.ORDER.toString())) {
                        aOrder.setOrderType(Order.caseTypes.ORDER);
                    } else if (caseType.equals(Order.caseTypes.CUT.toString())) {
                        aOrder.setOrderType(Order.caseTypes.CUT);
                    } else if (caseType.equals(Order.caseTypes.RECONNECTION.toString())) {
                        aOrder.setOrderType(Order.caseTypes.RECONNECTION);
                    }
                    Log.d(DEBUG_TAG, "Tipo de caso: " + aOrder.getOrderType());

                    if (caseStatus.equals(Order.caseStatus.INPROGRESS.toString())) {
                        aOrder.setOrderStatus(Order.caseStatus.INPROGRESS);
                    } else if (caseStatus.equals(Order.caseStatus.CANCELLED.toString())) {
                        aOrder.setOrderStatus(Order.caseStatus.CANCELLED);
                    } else if (caseStatus.equals(Order.caseStatus.FINISHED.toString())) {
                        aOrder.setOrderStatus(Order.caseStatus.FINISHED);
                    } else if (caseStatus.equals(Order.caseStatus.NEW.toString())) {
                        aOrder.setOrderStatus(Order.caseStatus.NEW);
                    } else if (caseStatus.equals(Order.caseStatus.ASSIGNED.toString())) {
                        aOrder.setOrderStatus(Order.caseStatus.ASSIGNED);
                    } else if (caseStatus.equals(Order.caseStatus.ACCEPTED.toString())) {
                        aOrder.setOrderStatus(Order.caseStatus.ACCEPTED);
                    } else if (caseStatus.equals(Order.caseStatus.RETIRED.toString())) {
                        aOrder.setOrderStatus(Order.caseStatus.RETIRED);
                    } else if (caseStatus.equals(Order.caseStatus.CLOSED)) {
                        aOrder.setOrderStatus(Order.caseStatus.CLOSED);
                    }
                    Log.d(DEBUG_TAG, "Status del caso : " + aOrder.getOrderStatus());

                    if (casePriority.equals(Order.casePriority.HIGH.toString())) {
                        aOrder.setOrderPriority(Order.casePriority.HIGH);
                    } else if (casePriority.equals(Order.casePriority.LOW.toString())) {
                        aOrder.setOrderPriority(Order.casePriority.LOW);
                    } else if (casePriority.equals(Order.casePriority.MEDIUM.toString())) {
                        aOrder.setOrderPriority(Order.casePriority.MEDIUM);
                    }
                    Log.d(DEBUG_TAG, "Prioridad del caso: " + aOrder.getOrderPriority());

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

    /**
     *
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        orderTaskListener.getCasesErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    /**
     * @param getOrderTaskListener
     */
    public void setOrderTaskListener(OrderTaskListener getOrderTaskListener) {
        this.orderTaskListener = getOrderTaskListener;
    }

    /**
     *
     */
    public interface OrderTaskListener {
        void getCasesErrorResponse(String error);

        void getCasesSuccessResponse(List<Order> orderList);
    }
}
