package com.iust.panta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SONY on 12/02/2014.
 */
public class EditProject extends Activity {

    private TextView TprojectNameView;
    private EditText EprojectNameView;

    private TextView TprojectInfoView;
    private EditText EprojectInfoview;

    private Button ButtonView;
    private ProgressBar ProgressView;

    private DatePicker datePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_add_project);

        Intent intent = getIntent();
        try {
            JSONObject info = new JSONObject(intent.getExtras().getString("projectInfo"));
            Log.d("response", info.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
