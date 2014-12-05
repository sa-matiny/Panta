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
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class EditProject extends Activity {

    private TextView TprojectNameView;
    private EditText EprojectNameView;

    private TextView TprojectInfoView;
    private EditText EprojectInfoView;
    private Integer projectID;

    private Button ButtonView;
    private ProgressBar ProgressView;

    private DatePicker datePicker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_add_project);

        Intent intent = getIntent();


        TprojectNameView = (TextView) findViewById(R.id.Tprojectname);
        EprojectNameView = (EditText) findViewById(R.id.Eprojectname);

        TprojectInfoView = (TextView) findViewById(R.id.TprojectInfo);
        EprojectInfoView = (EditText) findViewById(R.id.EprojectInfo);


        ButtonView = (Button) findViewById(R.id.add_project_button);
        ButtonView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                AddProject(view);
            }
        });

        try {
            JSONObject info = new JSONObject(intent.getExtras().getString("projectInfo"));
            Log.d("response", info.toString());
            EprojectNameView.setText(info.getString("projectName"));
            EprojectInfoView.setText(info.getString("project_info"));
            projectID = info.getInt("projectID");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void AddProject(View view) {

        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

        RequestParams params = new RequestParams();

        params.put("projectID", projectID);
        params.put("projectName", EprojectNameView.getText().toString());
        params.put("project_info", EprojectInfoView.getText().toString());
        params.put("year", String.valueOf(datePicker.getYear()));
        params.put("month", String.valueOf(datePicker.getMonth()));
        params.put("day", String.valueOf(datePicker.getDayOfMonth()));



        Log.d("projectID", String.valueOf(projectID));
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800//editProject/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

                // called before request is started
                Log.d("STARTED", "STARTED");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Log.d("SUCCESS", "SUCCESS");
               // ProgressView.setVisibility(View.GONE);
                ButtonView.setVisibility(View.VISIBLE);
                try {

                    Log.d("RESPONSE", new String(response));
                    JSONObject s_response = new JSONObject(new String(response));

                    // JSONArray



                    if (s_response.getBoolean("successful")) {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(EditProject.this);
                        dlg.setCancelable(false);
                        dlg.setMessage("successful");
                        dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dlg.create().show();
                        finish();
                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(EditProject.this);
                        dlg.setCancelable(false);
                        dlg.setMessage("خطای وارد کردن در دیتابیس");
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

                AlertDialog.Builder builder = new AlertDialog.Builder(EditProject.this);
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
        });


    }

}