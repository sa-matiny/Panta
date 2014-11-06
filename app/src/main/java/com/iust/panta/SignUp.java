package com.iust.panta;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;



public class SignUp extends Activity {

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPassword2View;
    private ProgressBar mProgressView;
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // on click function

    public void Sign_up(View view) {
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
        writeInSignUpTb();

    }

    private boolean isInvalidEmail(String email) {

        return !(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }

    private boolean isInvalidPassword(String password) // just determine the number
    {
        if (password.length() < this.password_size)
            return true;

        return false;
    }

    public void writeInSignUpTb() {
        //   JSONObject  object= new JSONObject();

        if (!has_error) {

            RequestParams params = new RequestParams();
            params.put("name", mNameView.getText().toString());
            params.put("username", mEmailView.getText().toString());
            params.put("password", mPasswordView.getText().toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://172.17.10.42:8800/register/", params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.d("STARTED", "STARTED");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    Log.d("SUCCESS", "SUCCESS");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.d("FAILED", "FAILED");
                    Log.d("statusCode", String.valueOf(statusCode));
                    Log.d("headers", String.valueOf(headers));
                    Log.d("headers", String.valueOf(headers));

                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });

        /*AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://172.17.10.42:8800/register", params, new JsonHttpResponseHandler()

        {

            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mProgressView.setVisibility(View.GONE);
                try {


                    //   JSONObject s_response= response.getJSONObject("");

                    // JSONArray
                    if (response.getBoolean("successful")) {
                        Intent intent = new Intent(SignUp.this, Profile.class);
                        finish();
                        startActivity(intent);

                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(SignUp.this);
                        dlg.setCancelable(false);
                        dlg.setMessage("خطا  شما به سرور وصل نیستید!");
                        dlg.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
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
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                mProgressView.setVisibility(View.GONE);
                Log.d("statusCode", String.valueOf(statusCode));
                Log.d("errorResponse", String.valueOf(errorResponse));
                Log.d("HEADERS", String.valueOf(headers));
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setCancelable(false);
                builder.setMessage("خطا دوباره ثبت نام کنید");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

        });*/


        }
    }
}

