package com.iust.panta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PCardMembersFragment extends Fragment {

    private View rootView;
    private ListView listView;
    private ArrayList<String> memberArray;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize
        rootView = inflater.inflate(R.layout.fragment_pcard_members, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);

        // RequestParam
        RequestParams params = new RequestParams();
        params.put("projectID", 1);
        AsyncHttpClient clinet = new AsyncHttpClient();
        clinet.post("http://104.236.33.128:8800//project_users/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                System.out.println("Members Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {

                    Log.d("My_Member__RESPONSE", new String(response));

                    JSONObject jobj = new JSONObject(new String(response));
                    JSONArray ProjectMembersInformation = jobj.getJSONArray("project_users");
                    Log.d("ProjectInfromation", (ProjectMembersInformation).toString());
                    //  Dictionary<int,int> indexToTaskId=new Dictionary<int, int>() ;
                    memberArray = new ArrayList<String>();
                    for (int i = 0; i < ProjectMembersInformation.length(); i++) {
                        memberArray.add(ProjectMembersInformation.getJSONObject(i).getString("name"));
                        // Log.d("my members",projects.get(i).toString());

                    }
                    //  Log.d("memberArray",memberArray.toString());
                    ArrayAdapter<String> ArrayItems = new ArrayAdapter<String>(PCardMembersFragment.this.getActivity(), android.R.layout.simple_list_item_1, memberArray);

                    listView.setAdapter(ArrayItems);

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

                //  Log.d("problemm",new String("in catching"));
            }

        });

        return rootView;
    }
}
