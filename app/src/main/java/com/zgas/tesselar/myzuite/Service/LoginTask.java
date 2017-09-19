package com.zgas.tesselar.myzuite.Service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.zgas.tesselar.myzuite.Model.User;
import com.zgas.tesselar.myzuite.R;
import com.zgas.tesselar.myzuite.Utilities.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by jarvizu on 19/09/2017.
 */

public class LoginTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "LoginTask";
    private static final String LOGIN_CONTROLLER = "user";
    private static final String ERROR_JSON_OBJECT = "error";
    private static final String USER_JSON_OBJECT = "user";

    private static final String JSON_OBJECT_ID = "id";
    private static final String JSON_OBJECT_EMAIL = "email";
    private static final String JSON_OBJECT_PASS = "password";

    private Context context;
    private JSONObject params;
    private LoginTaskListener loginTaskListener;
    private boolean isError = true;

    public LoginTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {

        JSONObject jsonObject = null;

        try {
            URL url = new URL(UrlHelper.getUrl(LOGIN_CONTROLLER));
            ConnectionController connection = new ConnectionController(url, context.getResources().getString(R.string.post), params);
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

        User user = null;

        try {

            if (jsonObjectResult == null) {
                loginTaskListener.loginErrorResponse(context.getResources().getString(R.string.login_error));
                isError = true;

            } else if (jsonObjectResult.has(ERROR_JSON_OBJECT)) {
                Log.d(DEBUG_TAG, "Error " + jsonObjectResult.getString(ERROR_JSON_OBJECT));
                loginTaskListener.loginErrorResponse(jsonObjectResult.getString(ERROR_JSON_OBJECT));
                isError = true;

            } else if (jsonObjectResult.has(USER_JSON_OBJECT)) {

                user = new User();

                jsonObjectResult = jsonObjectResult.getJSONObject(USER_JSON_OBJECT);

                user.setUserId(Integer.parseInt(jsonObjectResult.getString(JSON_OBJECT_ID)));
                user.setUserEmail(jsonObjectResult.getString(JSON_OBJECT_EMAIL));
                user.setUserPassword(jsonObjectResult.getString(JSON_OBJECT_PASS));

                isError = false;
            }

            if (isError == false) {
                loginTaskListener.loginSuccessResponse(user);
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

        void loginSuccessResponse(User user);
    }
}
