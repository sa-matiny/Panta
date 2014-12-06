package com.iust.panta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class EditTask extends Activity {

    private EditText EUserNameView;

    private EditText ETaskNameView;

    private EditText ETaskInfoView;

    private Button ButtonView;
    private ProgressBar ProgressView;
    private int taskID;
    private DatePicker datePicker;

    private int projectID;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        ETaskNameView = (EditText) findViewById(R.id.ETaskName);

        ETaskInfoView = (EditText) findViewById(R.id.ETaskInfo);

        EUserNameView = (EditText) findViewById(R.id.EUserName);

        ButtonView = (Button) findViewById(R.id.add_Task_button);
        ProgressView = (ProgressBar) findViewById(R.id.AddTask_progress);

        datePicker = (DatePicker) findViewById(R.id.datePicker);

        Intent intent=getIntent();


        try {
            JSONObject info = new JSONObject(intent.getExtras().getString("taskInfo"));
            Log.d("response", info.toString());
            ETaskNameView.setText(info.getString("taskName"));
            ETaskInfoView.setText(info.getString("task_info"));
            EUserNameView.setText(info.getString("username"));
            projectID = info.getInt("projectID");
            taskID = info.getInt("taskID");
            int year= Integer.parseInt(info.getString("deadline").split("-")[0]);
            int month= Integer.parseInt(info.getString("deadline").split("-")[1]);
            int day= Integer.parseInt(info.getString("deadline").split("-")[2]);

            datePicker.updateDate(year, month - 1, day);


           // datePicker = setText(info.getString("year"));

           // datePicker.updateDate(getInt("year"),getString("month"),getString("day"));
         //   datePicker= info.getInt(updateDate("year","month","day"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void AddTask(View view) {


        ProgressView.setVisibility(View.VISIBLE);
        ButtonView.setVisibility(View.GONE);
        RequestParams params = new RequestParams();
        params.put("taskID", taskID);
        params.put("projectID", projectID);
        params.put("taskName", ETaskNameView.getText().toString());
        params.put("task_info", ETaskInfoView.getText().toString());
        params.put("username", EUserNameView.getText().toString());

        params.put("year", String.valueOf(datePicker.getYear()));
        params.put("month", String.valueOf(datePicker.getMonth()+1));
        params.put("day", String.valueOf(datePicker.getDayOfMonth()));

        Log.d("projectID", String.valueOf(projectID));
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800//editTask/", params, new AsyncHttpResponseHandler() {

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
                        AlertDialog.Builder dlg = new AlertDialog.Builder(EditTask.this);
                        dlg.setCancelable(false);
                        dlg.setMessage("پست الکترونیکی موجود نیست");
                        dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
                        dlg.create().show();

                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(EditTask.this);
                        dlg.setCancelable(false);
                        dlg.setMessage("خطای اطلاعات کابر");
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


                ProgressView.setVisibility(View.GONE);
                ButtonView.setVisibility(View.VISIBLE);

                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
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
