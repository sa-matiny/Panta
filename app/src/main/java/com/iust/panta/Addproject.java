package com.iust.panta;

/**
 * Created by Fariba on 11/26/2014.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class Addproject extends Activity {

    private TextView TprojectNameView;
    private EditText EprojectNameView;

 //   private TextView TmanagerView;
   // private EditText Emanagerview;

    private TextView TprojectInfoView;
    private EditText EprojectInfoview;

    private Button ButtonView;
    private ProgressBar ProgressView;
    private boolean has_error = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        TprojectNameView = (TextView) findViewById(R.id.Tprojectname);
        EprojectNameView = (EditText) findViewById(R.id.Eprojectname);


//        TmanagerView = (TextView) findViewById(R.id.Tmanager);
  //      Emanagerview = (EditText) findViewById(R.id.Emanager);


        TprojectInfoView = (TextView) findViewById(R.id.TprojectInfo);
        EprojectInfoview = (EditText) findViewById(R.id.EprojectInfo);

        ButtonView = (Button) findViewById(R.id.add_project_button);
        ProgressView = (ProgressBar) findViewById(R.id.AddProject_progress);
    }


    public void AddProject(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EprojectNameView.getWindowToken(), 0);

        //set null error
        EprojectNameView.setError(null);
   //     Emanagerview.setError(null);
        EprojectInfoview.setError(null);



        //set variable
        this.has_error = false;
        String EprojectName = EprojectNameView.getText().toString();
     //   String Emanager = Emanagerview.getText().toString();
        String EprojectInfo = EprojectInfoview.getText().toString();

        View focus_view;
        ProgressView.setVisibility(View.VISIBLE);


        // check if Edit texts are Empty
        //    ProgressView.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(EprojectName)) {
            EprojectNameView.setError("نام پروژه را وارد کنید");
            focus_view = EprojectNameView;
            focus_view.requestFocus();
            this.has_error = true;


        }

    /*    if (TextUtils.isEmpty(Emanager)) {
            Emanagerview.setError("نام مدیر را وارد کنید");
            focus_view = Emanagerview;
            focus_view.requestFocus();
            this.has_error = true;


        }*/

        if (TextUtils.isEmpty(EprojectInfo)) {
            EprojectInfoview.setError("اطلاعاتی درباره پروژه ارائه دهید");
            focus_view = EprojectInfoview;
            focus_view.requestFocus();
            this.has_error = true;


        }
        if (this.has_error)
            ProgressView.setVisibility(View.GONE);
        writeInAddProjectTb();
    }

    public void writeInAddProjectTb() {
        if (!has_error) {
            ButtonView.setVisibility(View.GONE);
            ProgressView.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.put("projectName", EprojectNameView.getText().toString());
         //   params.put("username", Emanagerview.getText().toString());
            params.put("project_info", EprojectInfoview.getText().toString());
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
                    ProgressView.setVisibility(View.GONE);
                    ButtonView.setVisibility(View.VISIBLE);
                    try {
                        Log.d("RESPONSE",new String(response));
                        JSONObject s_response= new JSONObject(new String(response));

                        // JSONArray
                        if (s_response.getBoolean("successful"))
                        {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(Addproject.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("successful");
                            dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dlg.create().show();
                        }


                        else {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(Addproject.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("خطا");
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
                    ProgressView.setVisibility(View.GONE);
                    ButtonView.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Addproject.this);
                    builder.setCancelable(false);
                    builder.setMessage("خطا!");
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



    public void ClickOnAddProjectBody (View view)
    {

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EprojectNameView.getWindowToken(), 0);
   //     imm.hideSoftInputFromWindow(Emanagerview.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(EprojectInfoview.getWindowToken(), 0);

    }




}


