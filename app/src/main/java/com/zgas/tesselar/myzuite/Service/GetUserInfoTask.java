package com.zgas.tesselar.myzuite.Service;

import android.app.ProgressDialog;
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
import java.util.Formatter;

/**
 * Created by jarvizu on 19/09/2017.
 */

public class GetUserInfoTask extends AsyncTask<URL, JSONObject, JSONObject> {

    private static final String DEBUG_TAG = "GetUserInfoTask";

    private static final String JSON_OBJECT_ID = "Id";
    private static final String JSON_OBJECT_NAME = "Name";
    private static final String PARAMS_EMAIL = "email";
    private static final String JSON_OBJECT_STATUS = "Status";
    private static final String JSON_OBJECT_POSITION = "Position";
    private static final String JSON_OBJECT_ERROR = "errorCode";
    private static final String JSON_OBJECT_ZONE = "Zone";
    private static final String JSON_OBJECT_EMAIL = "Email";
    private static final String JSON_OBJECT_ROUTE = "Route";

    private Context context;
    private JSONObject params;
    private UserInfoListener userInfoListener;
    private ProgressDialog progressDialog;
    private boolean isError = false;
    private User user;

    public GetUserInfoTask(Context context, JSONObject params) {
        this.context = context;
        this.params = params;
    }


    /** progress dialog to show user that the backup is processing. */
    /**
     * application context.
     */
    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, context.getResources().getString(R.string.wait_message), false);
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {

        JSONObject jsonObject = null;

        try {
            Formatter formatter = new Formatter();
            String format = formatter.format(UrlHelper.GET_USER_DATA_URL, params.get(PARAMS_EMAIL)).toString();
            Log.d(DEBUG_TAG, "Url del usuario: " + format);

            URL url = new URL(format);
            ConnectionController connection = new ConnectionController(url, "GET");
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
        user = null;

        try {
            if (jsonObject == null) {
                userInfoListener.userInfoErrorResponse(jsonObject.getString(JSON_OBJECT_ERROR));
                isError = true;
            } else if (jsonObject.has(JSON_OBJECT_ERROR)) {
                Log.d(DEBUG_TAG, "Error: " + jsonObject.get(JSON_OBJECT_ERROR).toString());
                if (jsonObject.get(JSON_OBJECT_ERROR).toString().equals("400")) {
                    userInfoListener.userInfoErrorResponse(context.getResources().getString(R.string.user_data_error));
                    isError = true;
                }
            } else if (jsonObject.has(JSON_OBJECT_ID)) {
                Log.d(DEBUG_TAG, "HOLA, SI SIRVE");
                user = new User();
                String userStatus = jsonObject.get(JSON_OBJECT_STATUS).toString();
                String userType = jsonObject.get(JSON_OBJECT_POSITION).toString();
                user.setUserName(jsonObject.get(JSON_OBJECT_NAME).toString());
                user.setUserId(jsonObject.get(JSON_OBJECT_ID).toString());
                user.setUserRoute(jsonObject.get(JSON_OBJECT_ROUTE).toString());
                user.setUserZone(jsonObject.get(JSON_OBJECT_ZONE).toString());
                user.setUserEmail(jsonObject.get(JSON_OBJECT_EMAIL).toString());
                if (userType.equals(User.userType.OPERATOR.toString())) {
                    user.setUserType(User.userType.OPERATOR);
                } else if (userType.equals(User.userType.SUPERVISOR.toString())) {
                    user.setUserType(User.userType.SUPERVISOR);
                } else if (userType.equals(User.userType.SERVICE.toString())) {
                    user.setUserType(User.userType.SERVICE);
                } else if (userType.equals(User.userType.LEAKAGE.toString())) {
                    user.setUserType(User.userType.LEAKAGE);
                }

                if (userStatus.equals(User.userStatus.ACTIVE.toString())) {
                    user.setUserstatus(User.userStatus.ACTIVE);
                } else if (userStatus.equals(User.userStatus.NOTACTIVE.toString())) {
                    user.setUserstatus(User.userStatus.NOTACTIVE);
                }
                isError = false;

                Log.d(DEBUG_TAG, "Id del usuario: " + user.getUserId());
                Log.d(DEBUG_TAG, "Nombre de usuario: " + user.getUserName());
                Log.d(DEBUG_TAG, "Tipo de usuario: " + user.getUserType());
                Log.d(DEBUG_TAG, "Email del usuario: " + user.getUserEmail());
                Log.d(DEBUG_TAG, "Zona del usuario: " + user.getUserZone());
                Log.d(DEBUG_TAG, "Ruta del usuario: " + user.getUserRoute());
                Log.d(DEBUG_TAG, "Estatus del usuario: " + user.getUserstatus());
            }

            if (isError == false) {
                userInfoListener.userInfoSuccessResponse(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressDialog.dismiss();
        userInfoListener.userInfoErrorResponse(context.getResources().getString(R.string.connection_error));
    }

    public void setUserInfoListener(UserInfoListener userInfoListener) {
        this.userInfoListener = userInfoListener;
    }

    public interface UserInfoListener {
        void userInfoErrorResponse(String error);

        void userInfoSuccessResponse(User user);
    }
}
