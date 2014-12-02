package com.iust.panta;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Rayehe on 11/23/2014.
 */
public class HomeAddProjectFragment extends Fragment {

    private TextView TprojectNameView;
    private EditText EprojectNameView;

    private TextView TprojectInfoView;
    private EditText EprojectInfoview;

    private Button ButtonView;
    private ProgressBar ProgressView;

    private DatePicker datePicker;

    private Bundle msg;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle saveInstanceState){
        final View rootView = inflater.inflate(R.layout.fragment_home_add_project, container, false);
        TprojectNameView = (TextView) rootView.findViewById(R.id.Tprojectname);
        EprojectNameView = (EditText) rootView.findViewById(R.id.Eprojectname);

        TprojectInfoView = (TextView) rootView.findViewById(R.id.TprojectInfo);
        EprojectInfoview = (EditText) rootView.findViewById(R.id.EprojectInfo);

        msg = new Bundle();
        msg = getArguments();

        ButtonView = (Button) rootView.findViewById(R.id.add_project_button);
        ButtonView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                //set null error
                EprojectNameView.setError(null);
                //     Emanagerview.setError(null);
                EprojectInfoview.setError(null);


                //set variable
                boolean has_error = false;
                String EprojectName = EprojectNameView.getText().toString();
                String EprojectInfo = EprojectInfoview.getText().toString();

                View focus_view;
                ProgressView.setVisibility(View.VISIBLE);


                // check if Edit texts are Empty

                if (TextUtils.isEmpty(EprojectName)) {
                    EprojectNameView.setError("نام پروژه را وارد کنید");
                    focus_view = EprojectNameView;
                    focus_view.requestFocus();
                    has_error = true;


                }
                if (TextUtils.isEmpty(EprojectInfo)) {
                    EprojectInfoview.setError("اطلاعاتی درباره پروژه ارائه دهید");
                    focus_view = EprojectInfoview;
                    focus_view.requestFocus();
                    has_error = true;


                }
                if (has_error)
                    ProgressView.setVisibility(View.GONE);

                writeInAddProjectTb(has_error);

            }
        });
        ProgressView = (ProgressBar) rootView.findViewById(R.id.AddProject_progress);
        datePicker = (DatePicker) rootView.findViewById(R.id.datePicker);

        return rootView;
    }


    public void writeInAddProjectTb(boolean has_error) {
        if (!has_error) {
            ButtonView.setVisibility(View.GONE);
            ProgressView.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();
            params.put("projectName", EprojectNameView.getText().toString());
            params.put("username", msg.getString("username"));
            params.put("project_info", EprojectInfoview.getText().toString());
            params.put("year", String.valueOf(datePicker.getYear()));
            params.put("month", String.valueOf(datePicker.getMonth()));
            params.put("day", String.valueOf(datePicker.getDayOfMonth()));
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://104.236.33.128:8800/addProject/", params, new AsyncHttpResponseHandler() {

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
                            AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                            dlg.setCancelable(false);
                            dlg.setMessage("successful");
                            dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            dlg.create().show();
                        } else {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                            dlg.setCancelable(false);
                            dlg.setMessage("خطای اطلاعات کاربر");
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

                    Log.d("statuscode", String.valueOf(statusCode));
                    //Log.d("FAILED", new String(errorResponse));
                    ProgressView.setVisibility(View.GONE);
                    ButtonView.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });


        }
    }


}
