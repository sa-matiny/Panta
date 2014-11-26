package com.iust.panta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

public class PCardTaskFragment extends Fragment {

    private ListView listView;
    private   ArrayList<String> array;
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_pcard_task, container, false);

        listView=(ListView)rootView.findViewById(R.id.listView);
        array= new ArrayList<String>();
        RequestParams params = new RequestParams();
        params.put("projectID", 1);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800//project_tasks/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    Log.d("My__RESPONSE", new String(response));

                   JSONObject jobj=new JSONObject(new String(response));
                    JSONArray projects= jobj.getJSONArray("project_tasks");

                  //  Dictionary<int,int> indexToTaskId=new Dictionary<int, int>() ;
                    for (int i=0; i<projects.length();i++)
                    {
                        array.add(projects.getJSONObject(i).getString("taskName"));

                        Log.d("array :",array.get(i));
                    }
                    ArrayAdapter<String> ArrayItems =new ArrayAdapter<String>(PCardTaskFragment.this.getActivity(),android.R.layout.simple_list_item_1,array);

                    listView.setAdapter(ArrayItems);




                 //   string
                /*    mProName.setText(pro_info.getString("projectName"));
                    mProManager.setText(pro_info.getString("managerName"));
                    mProProgress.setText(pro_info.getString("progress"));
                    mProInfo.setText(pro_info.getString("project_info"));*/

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                /*
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
                alert.show();*/

                Log.d("problemm",new String("in catching"));
            }

        });
        // this.get activity is for fragments


        return rootView;
    }
}
