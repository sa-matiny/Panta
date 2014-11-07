package com.iust.panta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mProgressView = (ProgressBar)findViewById(R.id.login_progress);



    }

    public void Login(View view) {

        //hide Keyboard

        InputMethodManager imm = (InputMethodManager)getSystemService(
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
            mProgressView.setVisibility(View.VISIBLE);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            RequestParams params = new RequestParams();
            params.put("username", email);
            params.put("password", password);
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://104.236.33.128:8800/login/",params, new AsyncHttpResponseHandler()
            {

                @Override
                public void onStart() {
                    System.out.println("Start");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response){
                    mProgressView.setVisibility(View.GONE);
                    try {
                        Log.d("RESPONSE",new String(response));
                        JSONObject s_response= new JSONObject(new String(response));
                        if(s_response.getBoolean("successful"))
                        {
                            Intent intent = new Intent(LoginActivity.this,Profile.class);
                            Log.d("array",s_response.getJSONArray("projects").toString());
                            intent.putExtra("projects",s_response.getJSONArray("projects").toString());
                            finish();
                            startActivity(intent);

                        }
                        else
                        {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(LoginActivity.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("خطا! پست الکترونیکی یا گذر واژه نادرست است");
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
        }
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public void GoRegister(View view) {
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }

    public void ClickOnLoginBody(View view) // this for hiding Keyboard
    {

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);


    }
}