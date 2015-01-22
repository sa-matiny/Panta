package com.iust.panta;

//import com.iust.panta.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class TaskCard extends Activity {

    JSONObject data;
    private TextView taskOwnerName;
    private TextView taskOwnerUsername;
    private TextView taskName;
    private TextView taskDescription;
    private TextView taskDeadline;
    private CheckBox userCheckBox;
    private CheckBox managerCheckBox;


    private ProgressBar progressBar;
    private boolean manager;
    private String taskUsername;
    private String username;
    private Integer taskID;
    private Integer projectID;
    private Integer status;
    private JSONObject jobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_card);
        //taskOwnerName= new TextView();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        taskID = extras.getInt("taskID");
        manager = extras.getBoolean("manager");
        //  status= new Integer();

        if (manager) {
            invalidateOptionsMenu();
        }

        taskOwnerName = (TextView) findViewById(R.id.TaskOwenersName);

        taskOwnerUsername = (TextView) findViewById(R.id.TaskOwenerUserName);

        taskName = (TextView) findViewById(R.id.TaskName);

        taskDescription = (TextView) findViewById(R.id.TaskDescription);

        taskDeadline = (TextView) findViewById(R.id.TaskDeadline);

        taskUsername = new String();

        userCheckBox = (CheckBox) findViewById(R.id.UserCheckBox);

        managerCheckBox = (CheckBox) findViewById(R.id.ManagerCheckBox);

        progressBar = (ProgressBar) findViewById(R.id.TCard_progress);

        username = new String();

        SqliteController controller = new SqliteController(this);

        try {
            data = controller.getMe();
            username = data.getString("username"); // who login ?
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        // userCheckBox.setChecked(false);

        progressBar.setVisibility(View.VISIBLE);
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
                progressBar.setVisibility(View.GONE);
                try {

                    Log.d("My__RESPONSE", new String(response));

                    JSONObject jobj_1 = new JSONObject(new String(response));
                    jobj = jobj_1.getJSONObject("taskInfo");

                    Log.d("obj", jobj.toString());


                    status = jobj.getInt("status");

                    taskOwnerUsername.setText(jobj.getString("username"));

                    taskName.setText(jobj.getString("taskName"));
                    taskDescription.setText(jobj.getString("task_info"));
                    taskDeadline.setText(jobj.getString("deadline"));

                    projectID = jobj.getInt("projectID");
                    //  SQLiteDatabase db= this.getReadableDataBasel
                    taskOwnerName.setText(jobj.getString("name"));


                    // Log.d("Sqlite", (data.get(";
                    taskUsername = jobj.getString("username"); // taskcard for this username


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Problem in catching ", "hichi");
                }
            }

            @Override
            public void onFinish() {

                if (status == 0) // not user  not manager
                {
                    Log.d("0", "0");
                    userCheckBox.setChecked(false);
                    managerCheckBox.setChecked(false);
                    userCheckBox.setButtonDrawable(R.drawable.grayunchecked);
                    managerCheckBox.setButtonDrawable(R.drawable.grayunchecked);
                    managerCheckBox.setEnabled(false);
                    userCheckBox.setEnabled(false);

                    if (manager) {

                        managerCheckBox.setEnabled(true);
                        managerCheckBox.setButtonDrawable(R.drawable.blueunchecked);


                    }
                    if (username.equals(taskUsername)) {

                        //  userCheckBox.setClickable(true);
                        userCheckBox.setEnabled(true);
                        userCheckBox.setButtonDrawable(R.drawable.blueunchecked);
                    }


                } else if (status == 1)// user not manager
                {
                    Log.d("1", "1");

                    userCheckBox.setChecked(true);
                    managerCheckBox.setChecked(false);
                    userCheckBox.setClickable(true);
                    userCheckBox.setButtonDrawable(R.drawable.graycheck);
                    managerCheckBox.setButtonDrawable(R.drawable.grayunchecked);
                    managerCheckBox.setEnabled(false);
                    userCheckBox.setEnabled(false);

                    if (manager) {
                        managerCheckBox.setEnabled(true);
                        managerCheckBox.setButtonDrawable(R.drawable.blueunchecked);


                    }
                    if (username.equals(taskUsername)) {
                        userCheckBox.setEnabled(true);
                        // userCheckBox.setClickable(true);
                        userCheckBox.setButtonDrawable(R.drawable.bluecheck);
                    }
                } else // user & manager
                {

                    Log.d("2", "2");

                    userCheckBox.setChecked(true);
                    managerCheckBox.setChecked(true);
                    userCheckBox.setButtonDrawable(R.drawable.graycheck);
                    managerCheckBox.setButtonDrawable(R.drawable.graycheck);
                    managerCheckBox.setEnabled(false);
                    userCheckBox.setEnabled(false);
                      /*  if(manager)
                        {
                           // managerCheckBox.setClickable(true);
                            managerCheckBox.setButtonDrawable(R.drawable.bluecheck);


                        }
                        if(username.equals(taskUsername) )
                        {
                          //  userCheckBox.setClickable(true);
                            userCheckBox.setButtonDrawable(R.drawable.bluechecked);
                        }*/
                }
             /*   userCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //userCheckBox.setChecked(false);
                      //  Log.d("status before click ",Boolean.toString(userCheckBox.isChecked()));
                      //  userCheckBox.setChecked(!userCheckBox.isChecked());
                      //  Log.d("status after click ",Boolean.toString(userCheckBox.isChecked()));
                        if (userCheckBox.isChecked()) {
                            if (!managerCheckBox.isChecked())
                                status = 1;

                        } else
                            status = 0;

                        saveStatusCheckBoxes(status);


                    }

                });

                managerCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        managerCheckBox.setChecked(!managerCheckBox.isChecked());

                        if (managerCheckBox.isChecked()) {
                            if (userCheckBox.isChecked())
                                status = 2;

                            else
                                status = -1;
                        } else {
                            if (userCheckBox.isChecked())
                                status = 1;
                            else
                                status = 0;
                        }

                        saveStatusCheckBoxes(status);
                    }
                });


*/
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                progressBar.setVisibility(View.GONE);

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            if (manager) {
                getMenuInflater().inflate(R.menu.task_card, menu);
            }
        } catch (Exception e) {

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_edit_task) {
            Intent intent = new Intent(this, EditTask.class);
            intent.putExtra("taskInfo", jobj.toString());
            startActivity(intent);
            return true;

        }

        if (id == R.id.action_delete_task) {
            AlertDialog.Builder acc_del = new AlertDialog.Builder(TaskCard.this);
            acc_del.setMessage("می خواهید این وظیفه را حذف کنید؟");
            acc_del.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestParams params = new RequestParams();
                    params.put("taskID", taskID);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post("http://104.236.33.128:8800/deleteTask/", params, new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            System.out.println("Start");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                            try {
                                Log.d("RESPONSE", new String(response));
                                JSONObject s_response = new JSONObject(new String(response));
                                if (s_response.getBoolean("successful")) {
                                    AlertDialog.Builder dlg = new AlertDialog.Builder(TaskCard.this);
                                    dlg.setCancelable(false);
                                    dlg.setMessage("وظیفه حذف شد!");
                                    dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            finish();
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskCard.this);
                            builder.setCancelable(false);
                            Log.d("error", errorResponse.toString());
                            builder.setMessage("خطا! اتصال به اینترنت با مشکل مواجه است");
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
            });
            acc_del.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            acc_del.create().show();
            return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public void clickOnCheckBox2(View view) {
        // managerCheckBox.setChecked(!managerCheckBox.isChecked());
        if (managerCheckBox.isChecked())
            managerCheckBox.setButtonDrawable(R.drawable.bluecheck);
        else
            managerCheckBox.setButtonDrawable(R.drawable.blueunchecked);

        if (managerCheckBox.isChecked()) {
            if (userCheckBox.isChecked())
                status = 2;

            else {
                status = -1;

            }
        } else {
            if (userCheckBox.isChecked())
                status = 1;
            else
                status = 0;
        }

        saveStatusCheckBoxes(status);
    }

    public void clickOnCheckBox1(View view) {
        //userCheckBox.setChecked(false);
        //  Log.d("status before click ",Boolean.toString(userCheckBox.isChecked()));
        //  userCheckBox.setChecked(!userCheckBox.isChecked());
        //   userCheckBox.setChecked(userCheckBox.isChecked());
        if (userCheckBox.isChecked())
            userCheckBox.setButtonDrawable(R.drawable.bluecheck);
        else
            userCheckBox.setButtonDrawable(R.drawable.blueunchecked);

        Log.d("status after click ", Boolean.toString(userCheckBox.isChecked()));
        if (userCheckBox.isChecked()) {
            if (!managerCheckBox.isChecked())
                status = 1;

        } else
            status = 0;

        saveStatusCheckBoxes(status);

    }

    private void saveStatusCheckBoxes(int status) {
        RequestParams params = new RequestParams();
        Log.d("status", Integer.toString(status));

        if (status == -1) {
            Toast.makeText(getApplicationContext(), "تایید شما قبل از تایید کاربر امکان پذیر نمیباشد", Toast.LENGTH_LONG).show();
            /*if(managerCheckBox.isChecked())
                managerCheckBox.setButtonDrawable(R.drawable.blueunchecked);
            else
                managerCheckBox.setButtonDrawable(R.drawable.bluecheck);*/


        } else {
            params.put("projectID", projectID);
            params.put("taskID", taskID);
            params.put("status", status);

            if (status == 1) {

            }
            AsyncHttpClient client = new AsyncHttpClient();

            client.post("http://104.236.33.128:8800/changeStatus/", params, new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    System.out.println("change status start");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    progressBar.setVisibility(View.GONE); //for saving
                    try {


                        JSONObject jsonobj = new JSONObject(new String(response));


                        if (jsonobj.getBoolean("successful")) {

                            Toast.makeText(getApplicationContext(), "تغییرات ثبت شد . ", Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();


                    }


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(TaskCard.this);
                    builder.setCancelable(false);
                    builder.setMessage("خطا! دوباره امتحانa کنید");
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

}




