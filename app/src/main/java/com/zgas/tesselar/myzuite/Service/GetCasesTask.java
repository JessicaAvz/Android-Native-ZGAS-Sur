package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by jarvizu on 20/09/2017.
 */

public class GetCasesTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "GetCasesTask";
    private static final String CASE = "case";
    private static final String CASE_ERROR = "error";
    private static final String CASE_STATUS = "caseStatus";
    private static final String CASE_TYPE = "caseType";

    private Context context;
    private JSONObject params;
    private GetCasesTaskListener GetCasesTaskListener;
    private ProgressDialog progressDialog;
    private boolean isError = false;

    public GetCasesTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
    }

    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.wait_cases_message), false);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {

        JSONObject jsonObject = null;

        try {
            URL url = new URL("https://my-json-server.typicode.com/JessicaAvz/jsons/get_cases");
            ConnectionController connection = new ConnectionController(url, "GET", params);
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
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObjectResult) {
        super.onPostExecute(jsonObjectResult);
        progressDialog.dismiss();
        Gson gson = new Gson();
        Case aCase = null;

        try {
            if (jsonObjectResult == null) {
                GetCasesTaskListener.getCasesErrorResponse(jsonObjectResult.getString(CASE_ERROR));
                isError = true;
            } else if (jsonObjectResult.has(CASE_ERROR)) {
                Log.d(DEBUG_TAG, "Error " + jsonObjectResult.getString(CASE_ERROR));
                GetCasesTaskListener.getCasesErrorResponse(jsonObjectResult.getString(CASE_ERROR));
                isError = true;
            } else if (jsonObjectResult.has(CASE)) {
                aCase = gson.fromJson(jsonObjectResult.getJSONObject(CASE).toString(), Case.class);
                String caseType = jsonObjectResult.getJSONObject(CASE).get(CASE_TYPE).toString();
                String caseStatus = jsonObjectResult.getJSONObject(CASE).get(CASE_STATUS).toString();
                if (caseType.equals(Case.caseTypes.ORDER.toString())) {
                    aCase.setCaseType(Case.caseTypes.ORDER);
                } else if (caseType.equals(Case.caseTypes.CANCELLATION.toString())) {
                    aCase.setCaseType(Case.caseTypes.CANCELLATION);
                } else if (caseType.equals(Case.caseTypes.LEAKAGE.toString())) {
                    aCase.setCaseType(Case.caseTypes.LEAKAGE);
                } else if (caseType.equals(Case.caseTypes.CUT.toString())) {
                    aCase.setCaseType(Case.caseTypes.CUT);
                } else if (caseType.equals(Case.caseTypes.RECONNECTION.toString())) {
                    aCase.setCaseType(Case.caseTypes.RECONNECTION);
                } else if (caseType.equals(Case.caseTypes.CUSTOM_SERVICE.toString())) {
                    aCase.setCaseType(Case.caseTypes.CUSTOM_SERVICE);
                }

                if (caseStatus.equals(Case.caseStatus.INPROGRESS.toString())) {
                    aCase.setCaseStatus(Case.caseStatus.INPROGRESS);
                } else if (caseStatus.equals(Case.caseStatus.CANCELLED.toString())) {
                    aCase.setCaseStatus(Case.caseStatus.CANCELLED);
                } else if (caseStatus.equals(Case.caseStatus.FINISHED.toString())) {
                    aCase.setCaseStatus(Case.caseStatus.FINISHED);
                }
                GetCasesTaskListener.getCasesSuccessResponse(aCase);
                isError = false;

                Log.d(DEBUG_TAG, "Id del caso: " + aCase.getCaseId());
                Log.d(DEBUG_TAG, "Id del encargado del caso: " + aCase.getCaseUserId());
                Log.d(DEBUG_TAG, "Tiempo de llegada del caso: " + aCase.getCaseTimeIn());
                Log.d(DEBUG_TAG, "Tiempo de visto del caso: " + aCase.getCaseTimeSeen());
                Log.d(DEBUG_TAG, "Tiempo de atención del caso: " + aCase.getCaseTimeArrival());
                Log.d(DEBUG_TAG, "Tiempo de programación del caso: " + aCase.getCaseTimeProgrammed());
                Log.d(DEBUG_TAG, "Estatus del caso: " + aCase.getCaseStatus());
                Log.d(DEBUG_TAG, "Prioridad del caso: " + aCase.getCasePriority());
                Log.d(DEBUG_TAG, "Nombre del cliente del caso: " + aCase.getCaseClientName());
                Log.d(DEBUG_TAG, "Apellido del cliente del caso: " + aCase.getCaseClientLastname());
                Log.d(DEBUG_TAG, "Dirección del caso: " + aCase.getCaseAddress());
                Log.d(DEBUG_TAG, "Tipo del caso: " + aCase.getCaseType());
            }

            if (isError == false) {
                GetCasesTaskListener.getCasesSuccessResponse(aCase);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        GetCasesTaskListener.getCasesErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setCasesTaskListener(GetCasesTaskListener GetCasesTaskListener) {
        this.GetCasesTaskListener = GetCasesTaskListener;
    }

    public interface GetCasesTaskListener {
        void getCasesErrorResponse(String error);

        void getCasesSuccessResponse(Case cases);
    }
}
