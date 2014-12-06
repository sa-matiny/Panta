package com.iust.panta;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.iust.panta.Expand.adapter.ExpandListViewAdapter;
import com.iust.panta.Expands.ExpandChildList;
import com.iust.panta.Expands.ExpandGroupList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeProfileFragment extends Fragment {
    public ExpandableListView Explist;
    public boolean hasData;
    private ExpandListViewAdapter expandAdapter;
    private ArrayList<ExpandGroupList> expGroup;
    private ArrayList<String> listProjectsName;
    private ArrayList<String> listProjectsID;
    private ArrayList<ArrayList<String>> listEachProjectsTask;
    private ArrayList<ArrayList<String>> listEachProjectsTaskID;
    private View rootView;
    private ProgressBar mProgressView;
    private String userName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            saveInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home_profile, container, false);
        Explist = (ExpandableListView) rootView.findViewById(R.id.Final_list);

        mProgressView = (ProgressBar) rootView.findViewById(R.id.Profile_progress);

        Bundle msg = getArguments();
        userName = msg.getString("username");
        Log.d("username_profile", userName);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        listEachProjectsTask = new ArrayList<ArrayList<String>>();
        listEachProjectsTaskID = new ArrayList<ArrayList<String>>();

        listProjectsName = new ArrayList<String>();
        listProjectsID= new ArrayList<String >();

        mProgressView.setVisibility(View.VISIBLE);

        RequestParams params = new RequestParams();
        params.put("username", userName);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800/view_profile/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("profileStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                mProgressView.setVisibility(View.GONE);
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray Projects_obj = jObject.getJSONArray("projects");

                    Log.d("projectsjsonArray", Projects_obj.toString());

                    for (int i = 0; i < Projects_obj.length(); i++) {

                        Log.d("jsonArrayItems", Projects_obj.get(i).toString());

                        JSONObject eachProjects = Projects_obj.getJSONObject(i);

                        Log.d("projectName :", eachProjects.getString("projectName"));
                        listProjectsName.add(eachProjects.getString("projectName"));
                        Log.d("projectID :", eachProjects.getString("projectID"));
                        listProjectsID.add(eachProjects.getString("projectID"));
                        JSONArray jsonTasks = eachProjects.getJSONArray("tasks");
                        ArrayList<String> eachTask = new ArrayList<String>();
                        ArrayList<String> eachTaskID = new ArrayList<String>();


                        for (int j = 0; j < jsonTasks.length(); j++) {

                            JSONObject taskInformation = jsonTasks.getJSONObject(j);
                            Log.d("tasks :" + j, taskInformation.getString("taskName"));
                            Log.d("tasks :" + j, taskInformation.getString("taskID"));
                            eachTask.add(taskInformation.getString("taskName"));
                            eachTaskID.add(taskInformation.getString("taskID"));

                        }
                        Log.d("end of for 2", "end of for 2");
                        listEachProjectsTask.add(eachTask);
                        listEachProjectsTaskID.add(eachTaskID);
                    }

                } catch (JSONException jsone) {
                    jsone.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                mProgressView.setVisibility(View.GONE);
                System.out.println("Fail");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

            @Override
            public void onFinish() {

                hasData = true;
                if (listProjectsName.isEmpty()) {
                    hasData = false;
                }
                expGroup = SetStandardGroup(hasData);
                expandAdapter = new ExpandListViewAdapter(rootView.getContext(), expGroup);

                Explist.setAdapter(expandAdapter);

                Explist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        String idd = listEachProjectsTaskID.get(groupPosition).get(childPosition);

                        Intent intent = new Intent(HomeProfileFragment.this.getActivity(), TaskCard.class);
                        intent.putExtra("taskID", Integer.parseInt(idd));
                        startActivity(intent);

                        return true;
                    }
                });

            }

        });

    }


    public ArrayList<ExpandGroupList> SetStandardGroup(boolean flag) {

        ArrayList<ExpandGroupList> expandGroupArray = new ArrayList<ExpandGroupList>();

        if (flag) {
            for (int i = 0; i < listProjectsName.size(); i++) {

                ArrayList<ExpandChildList> expandChildArray = new ArrayList<ExpandChildList>();
                ExpandGroupList expandGroup = new ExpandGroupList();
                expandGroup.SetName(listProjectsName.get(i));
                expandGroup.setId(listProjectsID.get(i));

                if (!listEachProjectsTask.get(i).isEmpty()) {

                    for (int j = 0; j < listEachProjectsTask.get(i).size(); j++) {
                        ExpandChildList expandChild = new ExpandChildList();

                        expandChild.setName(listEachProjectsTask.get(i).get(j));
                        expandChild.setID(listEachProjectsTaskID.get(i).get(j));
                        expandChild.setTag(null);
                        expandChildArray.add(expandChild);
                    }
                }
                expandGroup.setItemes(expandChildArray);
                expandGroupArray.add(expandGroup);

            }
        } else {
            Toast.makeText(this.getActivity(), "برای شما پروژه ای موجود نمی باشد .", Toast.LENGTH_LONG).show();
        }
        return expandGroupArray;
    }

}