package com.iust.panta;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class SignUp extends Activity {


    SqliteController controller = new SqliteController(this);

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPassword2View;
    private ProgressBar mProgressView;
    private Button mButtonView;
    private int password_size = 2; // dynamic
    private boolean has_error = false;
    public boolean successfull = false;

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
        setContentView(R.layout.activity_sign_up);
        // set Up sign Up information

        mNameView = (EditText) findViewById(R.id.name);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPassword2View = (EditText) findViewById(R.id.rePassword);
        mProgressView = (ProgressBar) findViewById(R.id.signup_progress);
        mButtonView = (Button) findViewById(R.id.signup_button);
        context = getApplicationContext();
        Log.d("SignUpCOntext",context.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }

    // on click function

    public void Sign_up(View view) {


        //this is for hiding keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mNameView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mPassword2View.getWindowToken(), 0);

        //set null error
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordView.setError(null);
        mNameView.setError(null);

        //set variable
        this.has_error = false;
        String email,
                name,
                password,
                password2;
        email = mEmailView.getText().toString();
        name = mNameView.getText().toString();
        password = mPasswordView.getText().toString();
        password2 = mPassword2View.getText().toString();


        View focus_view;

        // check if Edit texts are Empty


        mProgressView.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(password2)) {
            mPassword2View.setError("تکرار گذرواژه را وارد کنید");  // sentence is wrong
            focus_view = mPassword2View;
            focus_view.requestFocus();
            this.has_error = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("گذرواژه را وارد کنید");
            focus_view = mPasswordView;
            focus_view.requestFocus();
            this.has_error = true;

        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("پست الکترونیکی را وارد کنید ");
            focus_view = mEmailView;
            focus_view.requestFocus();
            this.has_error = true;

        }

        if (!password.equals(password2)) {
            Log.d("passwrd1", password);
            Log.d("passwrd2", password2);
            mPassword2View.setError("گذر واژه را اشتباه وارد کردید");
            focus_view = mPassword2View;
            focus_view.requestFocus();
            this.has_error = true;

        }
        if (isInvalidPassword(password)) // number of character
        {
            mPasswordView.setError("  حداقل" + this.password_size + "کاراکتر وارد کنید ");
            focus_view = mPasswordView;
            focus_view.requestFocus();
            this.has_error = true;


        }


        //invalid Email
        if (isInvalidEmail(email)) {
            mEmailView.setError("پست الکترونیکی معتبر نمی باشد ");
            focus_view = mEmailView;
            focus_view.requestFocus();
            this.has_error = true;

        }

        if (TextUtils.isEmpty(name)) {

            mNameView.setError("نام و نام خانوادگی را وارد کنید");
            focus_view = mNameView;
            focus_view.requestFocus();
            this.has_error = true;

        }
        if (this.has_error)
            mProgressView.setVisibility(View.GONE);
        writeInSignUpTb();

    }

    private boolean isInvalidEmail(String email) {

        return !(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }

    private boolean isInvalidPassword(String password) // just determine the number
    {
        return (password.length() < this.password_size);

    }

    public void writeInSignUpTb() {

        if (!has_error) {
            mButtonView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.put("name", mNameView.getText().toString());
            params.put("username", mEmailView.getText().toString());
            params.put("password", mPasswordView.getText().toString());
            AsyncHttpClient client = new AsyncHttpClient();
            successfull=false;
            client.post("http://104.236.33.128:8800/register/", params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.d("STARTED", "STARTED");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    Log.d("SUCCESS", "SUCCESS");
                    mProgressView.setVisibility(View.GONE);
                    mButtonView.setVisibility(View.VISIBLE);
                    try {
                        Log.d("RESPONSE", new String(response));
                        JSONObject s_response = new JSONObject(new String(response));

                        // JSONArray
                        if (s_response.getBoolean("successful")) {
                            successfull = true;
                            JSONObject user = new JSONObject(s_response.getJSONObject("user_info").toString());
                            controller.insertMe(user.getString("username"), user.getString("name"));
                            JSONObject hi = controller.getMe();
                            Log.d("hi", hi.toString());
                            Intent intent = new Intent(SignUp.this, Home.class);

                            finish();
                            startActivity(intent);


                        } else {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(SignUp.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("خطا! پست الکترونیکی در سیستم موجود است");
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

                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.d("FAILED", "FAILED");
                    mProgressView.setVisibility(View.GONE);
                    mButtonView.setVisibility(View.VISIBLE);
                    /*Log.d("statusCode", String.valueOf(statusCode));
                    Log.d("errorResponse", errorResponse.toString());
                    Log.d("HEADERS",headers.toString());*/
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setCancelable(false);
                    builder.setMessage("خطا! دوباره ثبت نام کنید");
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
                public void onRetry(int retryNo) {
                    // called when request is retried
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
                                Log.d("afterRegister",regID);


                            } else
                                sendRegistrationIdToBackend();

                            // controller.deleteRegID();
                            // controller.deleteRegID();

                            //  controller.insertRegID("nastaran11","nastaran");


                            //************* next line must be active
                            controller.insertRegID(mEmailView.getText().toString(), regID);

                            Log.d("Login Insert", "insertReg ID");


                        } else
                            Log.i(TAG, "No valid Google Play Services APK found.");


                        //*********************

                    }
                }
            });


        }
    }

    public void ClickOnSignUpBody(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mNameView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mPassword2View.getWindowToken(), 0);


    }



    private void sendRegistrationIdToBackend() {



        // Your implementation here.


        RequestParams params = new RequestParams();

        params.put("reg_id",regID);
        Log.d("regID in signUp",regID);
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


                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
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
            /*    int noOfAttemptsAllowed = 5;   // Number of Retries allowed
                int noOfAttempts = 0;          // Number of tries done
                boolean stopFetching = false;

                while (!stopFetching)
                {
                    noOfAttempts ++;
                    try {
                        gcm.register()
                    }
                    try
                    {
                        // Leave some time here for the register to be
                        // registered before going to the next line
                        Thread.sleep(2000);   // Set this timing based on trial.
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                        Log.d("GCM", gcm.toString());

                    }
                    regID = gcm.register(SENDER_ID);
                    Log.d("regidafter",regID);
                    msg = regID;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    Log.d("after Registerbac",regID);
                    //sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    //
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                Log.d("message",msg);
                return msg;
            }

           /* @Override
            public void onFinish()
            {

            }*/

            @Override
            protected void onPostExecute(String msg) {
                Log.d("MSG",msg);
                Log.d("IF", String.valueOf(msg.indexOf("Error")));
                //mDisplay.append(msg + "\n");
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
        return getSharedPreferences(SignUp.class.getSimpleName(),
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



