package com.iust.panta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.View;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;



public class SignUp extends Activity {

    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPassword2View;
    private ProgressBar mProgressView;
    private int password_size=8; // dynamic
    private boolean has_error=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // set Up sign Up information

        mNameView = (EditText) findViewById(R.id.name);
        mEmailView=(EditText) findViewById(R.id.email);
        mPasswordView=(EditText) findViewById(R.id.password);
        mPassword2View=(EditText) findViewById(R.id.rePassword);
        mProgressView=(ProgressBar) findViewById(R.id.signup_progress);
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

    public void Sign_up(View view)
    {
        this.has_error=false;
        String email ,
                name,
                password,
                password2;
        email=mEmailView.getText().toString();
        name=mNameView.getText().toString();
        password=mPasswordView.getText().toString();
        password2=mPassword2View.getText().toString();

        View focus_view;

        // check if Edit texts are Empty




        if(TextUtils.isEmpty(password2))
        {
            mPassword2View.setError("تکرار گذرواژه را وارد کنید");  // sentence is wrong
            focus_view = mPassword2View;
            focus_view.requestFocus();
            this.has_error=true;
        }

        if(TextUtils.isEmpty(password))
        {
            mPasswordView.setError("گذرواژه را وارد کنید");
            focus_view = mPasswordView;
            focus_view.requestFocus();
            this.has_error=true;

        }

        if(TextUtils.isEmpty(email))
        {
            mEmailView.setError("پست الکترونیکی را وارد کنید ");
            focus_view=mEmailView;
            focus_view.requestFocus();
            this.has_error=true;

        }

        if(! password.equals(password2))
        {
            Log.d("passwrd1",password);
            Log.d("passwrd2",password2);
             mPassword2View.setError("گذر واژه را اشتباه وارد کردید");
             focus_view=mPassword2View;
             focus_view.requestFocus();
            this.has_error=true;

        }
        if(isInvalidPassword(password)) // number of character
        {
            mPasswordView.setError("  حداقل"+ this.password_size +"کاراکتر وارد کنید ");
            focus_view = mPasswordView;
            focus_view.requestFocus();
            this.has_error=true;


        }


        //invalid Email
        if(isInvalidEmail(email))
        {
            mEmailView.setError("پست الکترونیکی معتبر نمی باشد ");
            focus_view=mEmailView;
            focus_view.requestFocus();
            this.has_error=true;

        }

        if(TextUtils.isEmpty(name))
        {

            mNameView.setError("نام و نام خانوادگی را وارد کنید");
            focus_view=mNameView;
            focus_view.requestFocus();
            this.has_error=true;

        }
        writeInSignUpTb();

    }

    private boolean isInvalidEmail(String email)
    {

        return !(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }
    private boolean isInvalidPassword(String password) // just determine the number
    {
        if(password.length()< this.password_size)
            return true;

        return false;
    }

    public  void writeInSignUpTb()
    {

        RequestParams params= new RequestParams();
     //   JSONObject  object= new JSONObject();

        if(! has_error )
        {

      //  params.put("name",mNameView.getText().toString());
        params.put("username",mEmailView.getText().toString());
        params.put("password",mPasswordView.getText().toString());

            AsyncHttpClient client = new AsyncHttpClient();
           // client.post("http://104.236.61.35:8800/login/",params)
        client.post("http://104.236.61.35:8800/register/",params, new JsonHttpResponseHandler()

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
                    if(response.getBoolean("successful"))
                    {
                        Intent intent = new Intent(SignUp.this,Profile.class);
                        finish();
                        startActivity(intent);

                    }
                    else
                    {
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

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                mProgressView.setVisibility(View.GONE);
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

        });



}
}
}

