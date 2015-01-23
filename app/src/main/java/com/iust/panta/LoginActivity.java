package com.iust.panta;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class LoginActivity extends Activity {

    SqliteController controller = new SqliteController(this);
    private EditText mEmailView;
    private EditText mPasswordView;
    private ProgressBar mProgressView;
    private Button mButtonView;


    private GoogleCloudMessaging gcm;
    private String regID;
    private String SENDER_ID = "653195473278";
    TextView mDisplay;
    private Context context;

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    static final String TAG = "Pantaa";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mButtonView = (Button) findViewById(R.id.email_sign_in_button);
        context = getApplicationContext();

      //  context.deleteDatabase("androidsqlite.db");
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }

    public void Login(View view) {

        //hide Keyboard

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);


        // Reset errors.


        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("گذرواژه را وارد کنید");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("پست الکترونیکی را وارد کنید");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("پست الکترونیکی نامعتبر است");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mButtonView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            RequestParams params = new RequestParams();
            params.put("username", email);
            params.put("password", password);
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://104.236.33.128:8800/login/", params, new AsyncHttpResponseHandler() {
                boolean successfull = false; // it s for function

                @Override
                public void onStart() {
                    System.out.println("Start");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    mProgressView.setVisibility(View.GONE);
                    mButtonView.setVisibility(View.VISIBLE);
                    try {
                        Log.d("RESPONSE", new String(response));
                        JSONObject s_response = new JSONObject(new String(response));
                        if (s_response.getBoolean("successful")) {

                            JSONObject user = new JSONObject(s_response.getJSONObject("user_info").toString());
                            controller.insertMe(user.getString("username"), user.getString("name"));
                            JSONObject hi = controller.getMe();
                            Log.d("hi", hi.toString());
                            //   displayeNotification();
                            Intent intent = new Intent(LoginActivity.this, Home.class);

                            successfull = true;
                            finish(); //TODO what is it ! ?
                            startActivity(intent);

                        } else {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(LoginActivity.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("خطا! پست الکترونیکی یا گذرواژه نادرست است");
                            dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dlg.create().show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    mProgressView.setVisibility(View.GONE);
                    mButtonView.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("خطا! دوباره وارد شوید");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                @Override
                public void onFinish() {

                    if (successfull) {
                        if (checkPlayServices()) {
                            gcm = GoogleCloudMessaging.getInstance(context);
                            regID = getRegistrationId(context);
                            Log.d("regId", regID);
                            if (regID.isEmpty())// create new
                            {
                                Log.d("isEmpty", "is");
                                registerInBackground();
                            } else
                                sendRegistrationIdToBackend();

                                // controller.deleteRegID();
                                // controller.deleteRegID();

                               //  controller.insertRegID("nastaran11","nastaran");
                               controller.insertRegID(email,regID);

                                Log.d("Login Insert","insertReg ID");


                        } else
                            Log.i(TAG, "No valid Google Play Services APK found.");


                        //*********************

                    }
                }


            });

/*
        RequestParams params = new RequestParams();
        params.put("username", email);
       // params.put("password", password);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800/view_profile/",params, new AsyncHttpResponseHandler()
        {

            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response){
                mProgressView.setVisibility(View.GONE);
                mButtonView.setVisibility(View.VISIBLE);
                try {
                    Log.d("Profile RESPONSE",new String(response));
                    JSONObject s_response= new JSONObject(new String(response));
                    JSONArray jsonArrayAllProjects= s_response.getJSONArray("projects");
                    JSONArray  jsonArrayEachProject= new JSONArray();
                    for (int i=0;i<jsonArrayAllProjects.length();i++) {
                        Log.d("Items in json :", jsonArrayAllProjects.get(i).toString());
                        jsonArrayEachProject=jsonArrayAllProjects.getJSONArray(i);
                        Log.d("Json Array Each",jsonArrayEachProject.toString());
                     //   Array array=
                        for( int j=0;j<jsonArrayEachProject.length();j++)
                            Log.d("Items in Items ",jsonArrayEachProject.get(j).toString());
                    }
                    Log.d("profile Json Array:",jsonArrayAllProjects.toString());


                   /* if(s_response.getBoolean("successful"))
                    {

                        JSONObject user = new JSONObject(s_response.getJSONObject("user_info").toString());
                        controller.insertMe(user.getString("username"), user.getString("name"));
                        JSONObject hi = controller.getMe();
                        Log.d("hi", hi.toString());
                        Intent intent = new Intent(LoginActivity.this,Home.class);
                        Log.d("array",s_response.getJSONArray("projects").toString());
                        intent.putExtra("projects",s_response.getJSONArray("projects").toString());
                        intent.putExtra("user_info",s_response.getJSONObject("user_info").toString());

                        finish();
                        startActivity(intent);

                    }
                    else
                    {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(LoginActivity.this);
                        dlg.setCancelable(false);
                        dlg.setMessage("خطا! پست الکترونیکی یا گذرواژه نادرست است");
                        dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dlg.create().show();
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                mProgressView.setVisibility(View.GONE);
                mButtonView.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage("خطا! دوباره وارد شوید");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });
        */
        }

    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


    public void ClickOnLoginBody(View view) // this for hiding Keyboard
    {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);


    }


/*
protected void displayNotification(int number)
{
    NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
    mBuilder.setContentTitle("user Login");
    mBuilder.setTicker("Home");


    mBuilder.setNumber(number);

    Intent


}*/

    private void sendRegistrationIdToBackend() {



        // Your implementation here.


        RequestParams params = new RequestParams();

        params.put("reg_id",regID);
        try {


            String username = controller.getMe().getString("username");
            Log.d("put username",username);

            params.put("username",username );

        }
        catch (JSONException e) {
            e.printStackTrace();
        }



        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("LOG","Client Started");
        client.post("http://104.236.33.128:8800/gcmDatabase/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("starttt gcm");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                //   progressBar.setVisibility(View.GONE); //for saving
                try {


                    JSONObject jsonobj = new JSONObject(new String(response));


                    if (jsonobj.getBoolean("successful")) {

                        Toast.makeText(getApplicationContext(), "ذخیره شد", Toast.LENGTH_LONG).show();

                    }
                }
                catch(JSONException e){
                    e.printStackTrace();


                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {


                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage("مشکل در سرور");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regID = gcm.register(SENDER_ID);
                    Log.d("regidafter",regID);
                    msg = regID;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.


                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regID);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                Log.d("message",msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n");
                if(msg.indexOf("Error")==-1)
                {
                    Log.d("request to save","request");
                    sendRegistrationIdToBackend();
                }

            }
        }.execute(null, null, null);
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    // suppose we have regID
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(Home.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

//check for googleplay services

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
