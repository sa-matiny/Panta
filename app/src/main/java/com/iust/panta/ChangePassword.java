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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by user on 1/2/2015.
 */
public class ChangePassword extends Activity {
    private EditText EcurrentPassView;
    private EditText EnewPassView;
    private EditText EconfirmPassView;
    private Button ButtonView;
    private ProgressBar ProgressView;
    private int password_size = 2; // dynamic
    private boolean has_error = false;
    private Activity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();

        EcurrentPassView = (EditText) findViewById(R.id.EcurrentPassword);
        EnewPassView = (EditText) findViewById(R.id.EnewPassword);
        EconfirmPassView = (EditText) findViewById(R.id.EconfirmPassword);
        ProgressView = (ProgressBar) findViewById(R.id.change_password_progress);
        ButtonView = (Button) findViewById(R.id.change_password_button);


    }


    public void change_password(View view) {


        ButtonView.setOnClickListener(new View.OnClickListener()


        {
            @Override
            public void onClick(View view) {

                //set null error
                EcurrentPassView.setError(null);
                EnewPassView.setError(null);
                EnewPassView.setError(null);

                //set variable
                boolean has_error = false;
                String EcurrentPassword = EcurrentPassView.getText().toString();
                String EnewPassword = EnewPassView.getText().toString();
                String EconfirmPassword = EconfirmPassView.getText().toString();

                View focus_view;


                // check if Edit texts are Empty

                if (TextUtils.isEmpty(EcurrentPassword)) {
                    EcurrentPassView.setError("گذرواژه فعلی را وارد کنید");
                    focus_view = EcurrentPassView;
                    focus_view.requestFocus();
                    has_error = true;


                }
                if (TextUtils.isEmpty(EnewPassword)) {
                    EnewPassView.setError("گذروارژه جدید را وارد کنید");
                    focus_view = EnewPassView;
                    focus_view.requestFocus();
                    has_error = true;


                }
                if (TextUtils.isEmpty(EconfirmPassword)) {
                    EconfirmPassView.setError("گذرواژه جدید را دوباره وارد کنید");
                    focus_view = EconfirmPassView;
                    focus_view.requestFocus();
                    has_error = true;
                }
                if (!EnewPassword.equals(EconfirmPassword)) {
                    Log.d("passwrd1", EnewPassword);
                    Log.d("passwrd2", EconfirmPassword);
                    EconfirmPassView.setError("گذر واژه را اشتباه وارد کردید");
                    focus_view = EconfirmPassView;
                    focus_view.requestFocus();
                    has_error = true;

                }
                if (isInvalidPassword(EnewPassword)) // number of character
                {
                    EnewPassView.setError("  حداقل" + password_size + "کاراکتر وارد کنید ");
                    focus_view = EnewPassView;
                    focus_view.requestFocus();
                    has_error = true;


                }
                change_password(has_error);

            }
        });


    }


    public void change_password(boolean has_error) {
        if (!has_error) {
            ButtonView.setVisibility(View.GONE);
            ProgressView.setVisibility(View.VISIBLE);

            RequestParams params = new RequestParams();

            params.put("username", "mahla@yahoo.com");
            params.put("password", EconfirmPassView.getText().toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://104.236.33.128:8800/changePassword/", params, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    Log.d("STARTED", "STARTED");

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    Log.d("SUCCESS", "SUCCESS");
                    ProgressView.setVisibility(View.GONE);
                    ButtonView.setVisibility(View.VISIBLE);
                    try {
                        Log.d("RESPONSE", new String(response));

                        JSONObject s_response = new JSONObject(new String(response));

                        // JSONArray
                        if (s_response.getBoolean("successful")) {

                            AlertDialog.Builder dlg = new AlertDialog.Builder(ChangePassword.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("گذرواژه به روز شد");
                            dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent(ChangePassword.this, Welcome.class);
                                    finish();
                                    startActivity(intent);
                                }
                            });
                            dlg.create().show();
                        } else {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(ChangePassword.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("خطا!");
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

                    Log.d("statuscode", String.valueOf(statusCode));
                    //Log.d("FAILED", new String(errorResponse));
                    ProgressView.setVisibility(View.GONE);
                    ButtonView.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                    builder.setCancelable(false);
                    builder.setMessage("خطا! دوباره امتحان کنید");
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

    private boolean isInvalidPassword(String password) // just determine the number
    {
        return (password.length() < this.password_size);

    }

}







