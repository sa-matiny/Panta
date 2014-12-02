package com.iust.panta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SONY on 12/02/2014.
 */
public class EditTask extends Activity {
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

        Intent intent = getIntent();
        try {
            JSONObject info = new JSONObject(intent.getExtras().getString("taskInfo"));
            Log.d("response", info.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
