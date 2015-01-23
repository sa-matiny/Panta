package com.iust.panta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class EditTask extends FragmentActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Spinner EUserNameView;

    private EditText ETaskNameView;

    private EditText ETaskInfoView;

    private Button ButtonView;
    private ProgressBar ProgressView;
    private int taskID;
    private int projectID;
    JSONArray pro_users;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    int yearofyear, monthofmonth, dayofday, hourofhour, minofmin;

    ArrayList<String> memberArray = new ArrayList<String>();
    final ArrayList<String> member_users = new ArrayList<String>();
    int combo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        ETaskNameView = (EditText) findViewById(R.id.ETaskName);

        ETaskInfoView = (EditText) findViewById(R.id.ETaskInfo);

        EUserNameView = (Spinner) findViewById(R.id.EUserName);

        ButtonView = (Button) findViewById(R.id.add_Task_button);
        ProgressView = (ProgressBar) findViewById(R.id.AddTask_progress);




        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

        findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        findViewById(R.id.timeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });
        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }


        Intent intent=getIntent();


        try {
            JSONObject info = new JSONObject(intent.getExtras().getString("taskInfo"));
            Log.d("response", info.toString());
            ETaskNameView.setText(info.getString("taskName"));
            ETaskInfoView.setText(info.getString("task_info"));
            //EUserNameView.setText(info.getString("username"));
            projectID = info.getInt("projectID");
            taskID = info.getInt("taskID");

            try {
                pro_users = new JSONArray(intent.getExtras().getString("users"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("taskusersget",pro_users.toString());
            member_users.add(info.getString("username"));
            memberArray.add(info.getString("name") + "\n(" + info.getString("username") + ")");
            for (int i = 0; i < pro_users.length(); i++) {
                try {
                    JSONObject member = pro_users.getJSONObject(i);
                    if (info.getString("username").equals(member.getString("username"))) {
                        continue;
                    }
                    member_users.add(member.getString("username"));
                    memberArray.add(member.getString("name") + "\n(" + member.getString("username") + ")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("taskusers",memberArray.toString());
            ArrayAdapter<String> stringArrayAdapter =
                    new ArrayAdapter<String>(
                            this,
                            android.R.layout.simple_spinner_dropdown_item,
                            memberArray);
            EUserNameView.setAdapter(stringArrayAdapter);

            final AdapterView.OnItemSelectedListener onSpinner =
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(
                                AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {
                            combo = position;
                        }

                        @Override
                        public void onNothingSelected(
                                AdapterView<?> parent) {
                        }
                    };

            EUserNameView.setOnItemSelectedListener(onSpinner);

     /*       String[] listdate = info.getString("deadline").split(" ");

            int year= Integer.parseInt(listdate[0].split("-")[0]);
            int month= Integer.parseInt(listdate[0].split("-")[1]);
            int day= Integer.parseInt(listdate[0].split("-")[2]);
            int hour = Integer.parseInt(listdate[1].split(":")[0]);
            int minute = Integer.parseInt(listdate[1].split(":")[1]);
*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        //Toast.makeText(AddTask.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
        this.yearofyear=year;
        this.monthofmonth=month;
        this.dayofday=day;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        //Toast.makeText(AddTask.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
        this.hourofhour=hourOfDay;
        this.minofmin=minute;
    }


    public void AddTask(View view) {
        RequestParams params = new RequestParams();
        params.put("taskID", taskID);
        params.put("projectID", projectID);
        params.put("taskName", ETaskNameView.getText().toString());
        params.put("task_info", ETaskInfoView.getText().toString());
        params.put("username", member_users.get(combo));
        Log.d("problemm", String.valueOf(hourofhour));
        Log.d("problemm", String.valueOf(minofmin));

        params.put("year",this.yearofyear );
        params.put("month",(this.monthofmonth)+1  );
        params.put("day",this.dayofday  );
        params.put("hour",this.hourofhour  );
        params.put("minute",this.minofmin  );

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
                        dlg.setMessage("successful");
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

                AlertDialog.Builder builder = new AlertDialog.Builder(EditTask.this);
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
