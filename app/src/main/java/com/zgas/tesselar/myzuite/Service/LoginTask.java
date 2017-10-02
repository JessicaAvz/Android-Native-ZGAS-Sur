package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Model.Login;
import com.zgas.tesselar.myzuite.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by jarvizu on 25/09/2017.
 */

public class LoginTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "LoginTask";
    private static final String LOGIN_CONTROLLER = "services/oauth2/token";
    private static final String JSON_OBJECT_USER = "user";
    private static final String JSON_OBJECT_ID = "id";
    private static final String JSON_OBJECT_EMAIL = "email";
    private static final String JSON_OBJECT_API_TOKEN = "api_token";
    private static final String JSON_OBJECT_ERROR = "error";

    private Context context;
    private JSONObject params;
    private LoginTaskListener loginTaskListener;
    private boolean isError = false;

    public LoginTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        JSONObject jsonObject = null;

        try {
            URL url = new URL("https://test.salesforce.com/services/oauth2/token");
            ConnectionController connection = new ConnectionController(url, "POST", params);
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
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        Login login = null;

        try {
            if (jsonObject == null) {
                loginTaskListener.loginErrorResponse(context.getResources().getString(R.string.login_error));
                isError = true;

            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                Log.d(DEBUG_TAG, "Error " + jsonObject.getString(JSON_OBJECT_ERROR));
                loginTaskListener.loginErrorResponse(jsonObject.getString(JSON_OBJECT_ERROR));
                isError = true;

            } else if (jsonObject.has(JSON_OBJECT_USER)) {

                login = new Login();

                jsonObject = jsonObject.getJSONObject(JSON_OBJECT_USER);
                login.setLoginId(Integer.parseInt(jsonObject.getString(JSON_OBJECT_ID)));
                login.setLoginEmail(jsonObject.getString(JSON_OBJECT_EMAIL));
                login.setLoginApiToken(jsonObject.getString(JSON_OBJECT_API_TOKEN));

                isError = false;
            }

            if (isError == false) {
                loginTaskListener.loginSuccessResponse(login);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        loginTaskListener.loginErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setLoginTaskListener(LoginTaskListener loginTaskListener) {
        this.loginTaskListener = loginTaskListener;
    }

    public interface LoginTaskListener {
        void loginErrorResponse(String error);

        void loginSuccessResponse(Login login);
    }
}
