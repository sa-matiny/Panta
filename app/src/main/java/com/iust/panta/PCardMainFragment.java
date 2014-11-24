package com.iust.panta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class PCardMainFragment extends Fragment {


    private TextView mProName;
    private TextView mProManager;
    private TextView mProProgress;
    private TextView mProInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_pcard_main, container, false);

        mProName = (TextView) rootView.findViewById(R.id.pro_name);
        mProManager = (TextView) rootView.findViewById(R.id.pro_manager);
        mProProgress = (TextView) rootView.findViewById(R.id.pro_progress);
        mProInfo = (TextView) rootView.findViewById(R.id.pro_info);

        RequestParams params = new RequestParams();
        params.put("projectID", 1);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800/projectInfo/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    Log.d("RESPONSE", new String(response));
                    JSONObject s_response = new JSONObject(new String(response));
                    JSONObject pro_info = s_response.getJSONObject("projectInfo");
                    mProName.setText(pro_info.getString("projectName"));
                    mProManager.setText(pro_info.getString("managerName"));
                    mProProgress.setText(pro_info.getString("progress"));
                    mProInfo.setText(pro_info.getString("project_info"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
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
            }

        });

        return rootView;
    }
}