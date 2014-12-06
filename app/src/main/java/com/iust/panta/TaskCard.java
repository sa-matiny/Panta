package com.iust.panta;

//import com.iust.panta.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
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

    private boolean manager;
    private Integer taskID;
    private JSONObject jobj;
    private SqliteController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_card);
        //taskOwnerName= new TextView();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        taskID = extras.getInt("taskID");
        manager = extras.getBoolean("manager");


        taskOwnerName = (TextView) findViewById(R.id.TaskOwenersName);
        // taskOwnerName.setText("test");
        //    Log.d("Iwant CHeck ",taskOwnerName.toString());

        taskOwnerUsername = (TextView) findViewById(R.id.TaskOwenerUserName);

        taskName = (TextView) findViewById(R.id.TaskName);

        taskDescription = (TextView) findViewById(R.id.TaskDescription);

        taskDeadline = (TextView) findViewById(R.id.TaskDeadline);

        userCheckBox = (CheckBox) findViewById(R.id.UserCheckBox);

        managerCheckBox = (CheckBox) findViewById(R.id.ManagerCheckBox);

        userCheckBox.setChecked(false);





     //   managerCheckBox.

        //managerCheckBox.setClickable(false);

        controller = new SqliteController(this);

        RequestParams params = new RequestParams();
        Log.d("inTASKCARD", " " + taskID);
        params.put("taskID", taskID);
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

                    JSONObject jobj_1 = new JSONObject(new String(response));
                    jobj = jobj_1.getJSONObject("taskInfo");
                    //    Log.d("obj",jobj.toString());
                    taskOwnerUsername.setText(jobj.getString("username"));

                    taskName.setText(jobj.getString("taskName"));
                    taskDescription.setText(jobj.getString("task_info"));
                    taskDeadline.setText(jobj.getString("deadline"));
                    JSONObject data=controller.getMe();

                  //  SQLiteDatabase db= this.getReadableDataBasel
                    taskOwnerName.setText("درست باید شود!");
                   // Log.d("Sqlite", (data.get(";

                  boolean issame=false;
                    if (!data.getString("username").equals(jobj.getString("username"))) {
                        issame = true;
                        Log.d("username ", "equals");
                        userCheckBox.setChecked(false);
                        userCheckBox.setButtonDrawable(R.drawable.grayunchecked);
                        userCheckBox.setClickable(false);
                        //   userCheckBox.setClickable(true);

                    }
                    if( ! manager)
                    {
                        Log.d("she is manager","manager");
                        managerCheckBox.setChecked(false);

                        managerCheckBox.setButtonDrawable(R.drawable.grayunchecked);
                        managerCheckBox.setClickable(false);
                      //  managerCheckBox.setChecked(true);
                        //managerCheckBox.setClickable(true);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Problem in catching ", "hichi");
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


       /* params.put("taskID", 0);

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
                    JSONObject jobj = jobj_1.getJSONObject("taskInfo");
                    //    Log.d("obj",jobj.toString());
                    taskOwnerUsername.setText(jobj.getString("username"));

                    taskName.setText(jobj.getString("taskName"));
                    taskDescription.setText(jobj.getString("task_info"));
                    taskDeadline.setText(jobj.getString("deadline"));

                    if (jobj.getString("status").equals("1")) {
                        userCheckBox.setChecked(true);
                        managerCheckBox.setChecked(true);

                        //   managerCheckBox.setdrawa

                        //  Drawable d= getResources().getDrawable(R.drawable.graycheck  );


                        managerCheckBox.setButtonDrawable(R.drawable.graycheck);

                        managerCheckBox.setClickable(false);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Problem in catching ", "hichi");
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
*/

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
        if (id == R.id.action_edit_task) {
            if (!manager) {
                Toast.makeText(getApplicationContext(), "تنها مدیر پروژه می تواند وظیفه تغییر دهد", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, EditTask.class);
                intent.putExtra("taskInfo", jobj.toString());
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
