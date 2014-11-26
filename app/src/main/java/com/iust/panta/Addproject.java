package com.iust.panta;

/**
 * Created by Fariba on 11/26/2014.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Addproject extends Activity {

    private TextView TprojectNameView;
    private EditText EprojectNameView;

    private TextView TmanagerView;
    private EditText Emanagerview;

    private TextView TprojectInfoView;
    private EditText EprojectInfoview;

    private Button ButtonView;
    private ProgressBar ProgressView;
    private boolean has_error = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        TprojectNameView = (TextView) findViewById(R.id.Tprojectname);
        EprojectNameView = (EditText) findViewById(R.id.Eprojectname);


        TmanagerView = (TextView) findViewById(R.id.Tmanager);
        Emanagerview = (EditText) findViewById(R.id.Emanager);


        TprojectInfoView = (TextView) findViewById(R.id.TprojectInfo);
        EprojectInfoview = (EditText) findViewById(R.id.EprojectInfo);

        ButtonView = (Button) findViewById(R.id.addproject_button);
        ProgressView = (ProgressBar) findViewById(R.id.AddProject_progress);
    }


    public void AddProject(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EprojectNameView.getWindowToken(), 0);

        //set null error
        EprojectNameView.setError(null);
        Emanagerview.setError(null);
        EprojectInfoview.setError(null);



        //set variable
        this.has_error = false;
        String EprojectName = EprojectNameView.getText().toString();
        String Emanager = Emanagerview.getText().toString();
        String EprojectInfo = EprojectInfoview.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // check if Edit texts are Empty
        //    ProgressView.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(EprojectName)) {
            EprojectNameView.setError("نام پروژه را وارد کنید");

        }

        if (TextUtils.isEmpty(Emanager)) {
            Emanagerview.setError("نام مدیر را وارد کنید");

        }

        if (TextUtils.isEmpty(EprojectInfo)) {
            EprojectInfoview.setError("اطلاعاتی درباره پروژه ارائه دهید");

        }
        if (this.has_error)
            ProgressView.setVisibility(View.GONE);
        writeInAddProjectTb();
    }

    public void writeInAddProjectTb() {
    }
    public void ClickOnAddProjectBody (View view)
    {

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EprojectNameView.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(Emanagerview.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(EprojectInfoview.getWindowToken(), 0);

    }



}


