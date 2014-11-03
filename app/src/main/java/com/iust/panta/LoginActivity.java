package com.iust.panta;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void Login(View view) {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
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
            client.post("http://104.236.61.35:8800/login/",params, new JsonHttpResponseHandler()
            {

                @Override
                public void onStart() {
                    System.out.println("Start");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    mProgressView.setVisibility(View.GONE);
                    try {
                        if(response.getBoolean("success"))
                        {
                            Intent intent = new Intent(LoginActivity.this,Profile.class);
                            finish();
                            startActivity(intent);

                        }
                        else
                        {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(LoginActivity.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("خطا! دوباره وارد شوید");
                            dlg.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
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
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                    mProgressView.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setCancelable(false);
                    builder.setMessage("نا موفق.لطفا دوباره سعی کنید!");
                    builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    //System.out.println(errorResponse);
                    //System.out.println(statusCode);
                }

            });
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public void GoRegister(View view) {
        Intent intent = new Intent(this,SignUp.class);
        //finish();
        startActivity(intent);
    }
}