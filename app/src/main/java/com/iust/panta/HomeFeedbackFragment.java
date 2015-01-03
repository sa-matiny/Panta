package com.iust.panta;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFeedbackFragment extends Fragment {
    private Button btnSubmit;
    private ProgressBar ProgressView;
    private EditText FeedbackInfoview;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home_feedback, container, false);


        FeedbackInfoview = (EditText) rootView.findViewById(R.id.FeedbackInfo);
        ProgressView = (ProgressBar) rootView.findViewById(R.id.Feedback_progress);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener()


        {
            @Override
            public void onClick(View view) {


                FeedbackInfoview.setError(null);
                boolean has_error = false;
                String FeedbackInfo = FeedbackInfoview.getText().toString();

                View focus_view;

                if (TextUtils.isEmpty(FeedbackInfo)) {
                    FeedbackInfoview.setError("لطفا توضیحاتی ارائه دهید");
                    focus_view = FeedbackInfoview;
                    focus_view.requestFocus();
                    has_error = true;


                }

                Feedback(has_error);
            }

        });
        return rootView;

    }


    public void Feedback(boolean has_error) {
        if (!has_error) {
            btnSubmit.setVisibility(View.GONE);
            ProgressView.setVisibility(View.VISIBLE);
            RequestParams params = new RequestParams();

            params.put("feedback_info", FeedbackInfoview.getText().toString());
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://104.236.33.128:8800/Feedback/", params, new AsyncHttpResponseHandler() {
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
                    btnSubmit.setVisibility(View.VISIBLE);
                    try {
                        Log.d("RESPONSE", new String(response));

                        JSONObject s_response = new JSONObject(new String(response));

                        // JSONArray
                        if (s_response.getBoolean("successful")) {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                            dlg.setCancelable(false);
                            dlg.setMessage("نظر شما ثبت شد");
                            dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent(getActivity(), Home.class);
                                    getActivity().finish();
                                    startActivity(intent);
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
                    btnSubmit.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage("خطا! دوباره امتحان کنید");
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