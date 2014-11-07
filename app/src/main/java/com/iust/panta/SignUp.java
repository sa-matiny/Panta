package com.iust.panta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class SignUp extends Activity {

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPassword2View;
    private ProgressBar mProgressView;
    private Button mButtonView;
    private int password_size = 2; // dynamic
    private boolean has_error = false;


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
        mButtonView = (Button)findViewById(R.id.signup_button);
    }


    // on click function

    public void Sign_up(View view) {


        //this is for hiding keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(
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
        if(this.has_error)
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
                        Log.d("RESPONSE",new String(response));
                        JSONObject s_response= new JSONObject(new String(response));

                        // JSONArray
                        if (s_response.getBoolean("successful")) {
                            Intent intent = new Intent(SignUp.this, Profile.class);
                            Log.d("array",s_response.getJSONArray("projects").toString());
                            intent.putExtra("projects",s_response.getJSONArray("projects").toString());
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
            });


        }
    }

    public void ClickOnSignUpBody(View view)
    {

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mNameView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mPassword2View.getWindowToken(), 0);


    }
}

