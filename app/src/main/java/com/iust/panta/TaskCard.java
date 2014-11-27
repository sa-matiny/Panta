package com.iust.panta;

import com.iust.panta.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TaskCard extends Activity {

    private TextView taskOwnerName;
    private TextView taskOwnerUsername;
    private TextView taskName;
    private TextView taskDescription;
    private TextView taskDeadline;
    private CheckBox userCheckBox;
    private CheckBox managerCheckBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        taskOwnerName= (TextView)findViewById(R.id.TaskOwenersName);

        taskOwnerUsername= (TextView)findViewById(R.id.TaskOwenerUserName);

        taskName= (TextView)findViewById(R.id.TaskName);

        taskDescription= (TextView)findViewById(R.id.TaskDescription);

        taskDeadline= (TextView)findViewById(R.id.TaskDeadline);

        userCheckBox =(CheckBox) findViewById(R.id.UserCheckBox);

        managerCheckBox=(CheckBox) findViewById(R.id.ManagerCheckBox);

        RequestParams params = new RequestParams();
        params.put("taskID", 0);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800//taskInfo/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    Log.d("My__RESPONSE", new String(response));

                    JSONObject jobj=new JSONObject(new String(response));
                    JSONArray ProjectTasksInformation= jobj.getJSONArray("project_tasks");
/*
                    //  Dictionary<int,int> indexToTaskId=new Dictionary<int, int>() ;
                    for (int i=0; i<ProjectTasksInformation.length();i++)
                    {
                        taskNameArray.add(ProjectTasksInformation.getJSONObject(i).getString("taskName"));


                    }
                    ArrayAdapter<String> ArrayItems =new ArrayAdapter<String>(PCardTaskFragment.this.getActivity(),android.R.layout.simple_list_item_1, taskNameArray);

                    listView.setAdapter(ArrayItems);*/

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TaskCard.this);
                builder.setCancelable(false);
                builder.setMessage("خطا! اتصال به اینترنت با مشکل مواجه است");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                //  Log.d("problemm",new String("in catching"));
            }

        });

        setContentView(R.layout.activity_task_card);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_card, menu);
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
}
