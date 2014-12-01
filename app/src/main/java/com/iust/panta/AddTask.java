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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class AddTask extends Activity {
    private TextView TUserNameView;
    private EditText EUserNameView;

    private TextView TTaskNameView;
    private EditText ETaskNameView;

    private TextView TTaskInfoView;
    private EditText ETaskInfoView;

    private Button ButtonView;
    private ProgressBar ProgressView;
    private boolean has_error = false;
    private int projectID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        TUserNameView = (TextView) findViewById(R.id.TUserName);
        EUserNameView = (EditText) findViewById(R.id.EUserName);

        TTaskNameView = (TextView) findViewById(R.id.TTaskName);
        ETaskNameView = (EditText) findViewById(R.id.ETaskName);

        TTaskInfoView = (TextView) findViewById(R.id.TTaskInfo);
        ETaskInfoView = (EditText) findViewById(R.id.ETaskInfo);

        ButtonView = (Button) findViewById(R.id.add_Task_button);
        ProgressView = (ProgressBar) findViewById(R.id.AddTask_progress);

        Intent intent = getIntent();
        projectID = intent.getExtras().getInt("projectID");

    }

    public void AddTask(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EUserNameView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(ETaskNameView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(ETaskInfoView.getWindowToken(), 0);

        //set null error
        EUserNameView.setError(null);
        ETaskNameView.setError(null);
        ETaskInfoView.setError(null);


        //set variable
        this.has_error = false;
        String EUserName = EUserNameView.getText().toString();
        String ETaskName = ETaskNameView.getText().toString();
        String ETaskInfo = ETaskInfoView.getText().toString();

        View focus_view;
        ProgressView.setVisibility(View.VISIBLE);


        // check if Edit texts are Empty

        if (TextUtils.isEmpty(EUserName)) {
            EUserNameView.setError("نام را وارد کنید");
            focus_view = EUserNameView;
            focus_view.requestFocus();
            this.has_error = true;


        }

        if (TextUtils.isEmpty(ETaskName)) {
            ETaskNameView.setError("نام وظیفه را وارد کنید");
            focus_view = ETaskNameView;
            focus_view.requestFocus();
            this.has_error = true;
        }

        if (TextUtils.isEmpty(ETaskInfo)) {
            ETaskInfoView.setError("شرح وظیفه را وارد کنید");
            focus_view = ETaskInfoView;
            focus_view.requestFocus();
            this.has_error = true;
        }


        if (this.has_error)
            ProgressView.setVisibility(View.GONE);
        writeInAddTaskTb();
    }

    public void writeInAddTaskTb() {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

        if (!has_error) {
            ButtonView.setVisibility(View.GONE);
            ProgressView.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.put("username", EUserNameView.getText().toString());
            params.put("projectID", projectID);
            params.put("taskName", ETaskNameView.getText().toString());
            params.put("task_info", ETaskInfoView.getText().toString());
            params.put("year", String.valueOf(datePicker.getYear()));
            params.put("month", String.valueOf(datePicker.getMonth()));
            params.put("day", String.valueOf(datePicker.getDayOfMonth()));
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://104.236.33.128:8800/addTask/", params, new AsyncHttpResponseHandler() {

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
                        Log.d("RESPONSE", new String(response));
                        JSONObject s_response = new JSONObject(new String(response));

                        // JSONArray
                        if (s_response.getBoolean("successful")) {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(AddTask.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("successful");
                            dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dlg.create().show();
                        } else {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(AddTask.this);
                            dlg.setCancelable(false);
                            dlg.setMessage("خطای اطلاعات کاربر");
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddTask.this);
                    builder.setCancelable(false);
                    builder.setMessage("خطای سرور");
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


    public void ClickOnAddTaskBody(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EUserNameView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(ETaskNameView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(ETaskInfoView.getWindowToken(), 0);
    }


}