package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Model.Case;
import com.zgas.tesselar.myzuite.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarvizu on 20/09/2017.
 */

public class GetCasesTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "GetCasesTask";
    private static final String CASE = "case";
    private static final String CASES_ARRAY = "cases";
    private static final String CASE_ERROR = "error";
    private static final String CASE_ID = "caseId";
    private static final String CASE_USER_ID = "caseUserId";
    private static final String CASE_TIME_IN = "caseTimeIn";
    private static final String CASE_TIME_SEEN = "caseTimeSeen";
    private static final String CASE_TIME_ARRIVAL = "caseTimeArrival";
    private static final String CASE_TIME_PROGRAMMED = "caseTimeProgrammed";
    private static final String CASE_PRIORITY = "casePriority";
    private static final String CASE_CIENT_NAME = "caseClientName";
    private static final String CASE_CLIENT_LASTNAME = "caseClientLastname";
    private static final String CASE_ADDRESS = "caseAddress";
    private static final String CASE_STATUS = "caseStatus";
    private static final String CASE_TYPE = "caseType";
    private static final String URL = "https://my-json-server.typicode.com/JessicaAvz/jsons/get_cases";

    private Context context;
    private JSONObject params;
    private CasesTaskListener casesTaskListener;
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
            URL url = new URL(URL);
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

        List<Case> casesList = new ArrayList<>();
        JSONArray casesArray;

        Case aCase;

        try {
            casesArray = jsonObjectResult.getJSONArray(CASES_ARRAY);

            for (int i = 0; i < casesArray.length(); i++) {
                JSONObject caseObject = casesArray.getJSONObject(i);
                aCase = new Case();
                aCase.setCaseId(caseObject.getInt(CASE_ID));
                aCase.setCaseUserId(caseObject.getInt(CASE_USER_ID));
                aCase.setCaseTimeArrival(caseObject.getString(CASE_TIME_SEEN));
                aCase.setCaseTimeProgrammed(caseObject.getString(CASE_TIME_PROGRAMMED));
                aCase.setCaseTimeSeen(caseObject.getString(CASE_TIME_SEEN));
                aCase.setCaseTimeIn(caseObject.getString(CASE_TIME_IN));
                aCase.setCaseClientName(caseObject.getString(CASE_CIENT_NAME));
                aCase.setCaseClientLastname(caseObject.getString(CASE_CLIENT_LASTNAME));
                aCase.setCaseAddress(caseObject.getString(CASE_ADDRESS));

                String caseType = caseObject.get(CASE_TYPE).toString();
                String caseStatus = caseObject.get(CASE_STATUS).toString();
                String casePriority = caseObject.get(CASE_PRIORITY).toString();
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

                if (casePriority.equals(Case.casePriority.HIGH.toString())) {
                    aCase.setCasePriority(Case.casePriority.HIGH);
                } else if (casePriority.equals(Case.casePriority.LOW.toString())) {
                    aCase.setCasePriority(Case.casePriority.LOW);
                } else if (casePriority.equals(Case.casePriority.MEDIUM.toString())) {
                    aCase.setCasePriority(Case.casePriority.MEDIUM);
                }

                isError = false;
                casesList.add(aCase);

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
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (casesTaskListener != null) {
                casesTaskListener.getCasesSuccessResponse(casesList);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        casesTaskListener.getCasesErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setCasesTaskListener(CasesTaskListener GetCasesTaskListener) {
        this.casesTaskListener = GetCasesTaskListener;
    }

    public interface CasesTaskListener {
        void getCasesErrorResponse(String error);

        void getCasesSuccessResponse(List<Case> caseList);
    }
}
