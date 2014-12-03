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
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SONY on 12/02/2014.
 */
public class EditProject extends Activity {

    private TextView TprojectNameView;
    private EditText EprojectNameView;

    private TextView TprojectInfoView;
    private EditText EprojectInfoView;
    private Integer projectID;

    private Button ButtonView;
    private ProgressBar ProgressView;
    // private JSONObject edit;


    private DatePicker datePicker;

    public EditProject() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_add_project);

        Intent intent = getIntent();
        intent.getExtras();


        TprojectNameView = (TextView) findViewById(R.id.Tprojectname);
        EprojectNameView = (EditText) findViewById(R.id.Eprojectname);

        TprojectInfoView = (TextView) findViewById(R.id.TprojectInfo);
        EprojectInfoView = (EditText) findViewById(R.id.EprojectInfo);


        ButtonView = (Button) findViewById(R.id.add_project_button);

        RequestParams params = new RequestParams();
        params.put("projectID", projectID);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800//taskInfo/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Intent intent = getIntent();
                try {
                    Log.d("My__RESPONSE", new String(response));

                    JSONObject jobj_1 = new JSONObject(new String(response));
                    JSONObject edit = jobj_1.getJSONObject("projectInfo");
                    EprojectNameView.setText(edit.getString("projectName"));
                    EprojectInfoView.setText(edit.getString("project_info"));

                } catch (JSONException e1) {
                    e1.printStackTrace();
                    Log.d("Problem in catching ", "خطا!!!");

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditProject.this);
                builder.setCancelable(false);
                builder.setMessage("خطاااااا");
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

        params.put("projectID", 1);

        client.post("http://104.236.33.128:8800//taskInfo/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    Log.d("My__RESPONSE", new String(response));

                    JSONObject jobj_1 = new JSONObject(new String(response));
                    JSONObject edit = jobj_1.getJSONObject("projectInfo");
                    EprojectNameView.setText(edit.getString("projectName"));
                    EprojectInfoView.setText(edit.getString("project_info"));

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditProject.this);
                builder.setCancelable(false);
                builder.setMessage("خطادر اتصال به اینترنت");
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


    public void AddProject(View view) {
        final String EprojectNameView = ((EditText) findViewById(R.id.Eprojectname)).getText().toString();
        final String EprojectInfoView = ((EditText) findViewById(R.id.EprojectInfo)).getText().toString();
        RequestParams params = new RequestParams();

        params.put("projectID", projectID);
        params.put("projectName", EprojectNameView);
        params.put("project_info", EprojectInfoView);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle post = client.post("http://104.236.33.128:8800//taskInfo/", params, new AsyncHttpResponseHandler() {


            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });
    }
}



