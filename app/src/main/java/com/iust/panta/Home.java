package com.iust.panta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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


public class Home extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    static final String TAG = "Pantaa";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    SqliteController controller = new SqliteController(this);
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Bundle msg;
    private String mTitle;
    private GoogleCloudMessaging gcm;
    private String regID;
    private String SENDER_ID="653195473278";
    TextView mDisplay;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        //*** nastaran Notification
        context= getApplicationContext();
        //check Google playe service
        msg = new Bundle();
        String userName;

        try {
            userName = controller.getMe().getString("username");
            msg.putString("username", userName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (checkPlayServices())
        {
            gcm=GoogleCloudMessaging.getInstance(this);
            regID=getRegistrationId(context);
            Log.d("regId",regID);
            if(regID.isEmpty())// create new
            {
                Log.d("isEmpty","is");
                registerInBackground();
            }
            else
                sendRegistrationIdToBackend();
        }
        else
            Log.i(TAG, "No valid Google Play Services APK found.");



        //*********************


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
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
/*
    // Send an upstream message.
    public void onClick(final View view) {

        if (view == findViewById(R.id.send)) {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String msg = "";
                    try {
                        Bundle data = new Bundle();
                        data.putString("my_message", "Hello World");
                        data.putString("my_action", "com.google.android.gcm.demo.app.ECHO_NOW");
                        String id = Integer.toString(msgId.incrementAndGet());
                        gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
                        msg = "Sent message";
                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                    }
                    return msg;
                }

                @Override
                protected void onPostExecute(String msg) {
                    mDisplay.append(msg + "\n");
                }
            }.execute(null, null, null);
        } else if (view == findViewById(R.id.clear)) {
            mDisplay.setText("");
        }
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


                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment objfrag = null;

        switch (position) {
            case 0:
                mTitle = getString(R.string.Home_section);
                objfrag = new HomeProfileFragment();
                objfrag.setArguments(msg);
                break;
            case 1:
                mTitle = getString(R.string.add_section);
                objfrag = new HomeAddProjectFragment();
                objfrag.setArguments(msg);
                break;
            case 2:
                mTitle = getString(R.string.Setting_section);
                objfrag = new HomeSettingFragment();
                break;
            case 3:
                mTitle = getString(R.string.feedback_section);
                objfrag = new HomeFeedbackFragment();
                break;

            default:
                mTitle = getString(R.string.Home_section);
                objfrag = new HomeProfileFragment();
                objfrag.setArguments(msg);
                break;

        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objfrag)
                .commit();
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("می خواهید از برنامه خارج شوید؟");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               // controller.deleteMe();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              //  finish();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

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