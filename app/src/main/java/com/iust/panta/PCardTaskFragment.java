package com.iust.panta;



import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.content.Intent;

import android.widget.Toast;

import com.iust.panta.interface1.InterfaceCommiunication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PCardTaskFragment extends Fragment {

    private ListView listView;
    private   ArrayList<String> taskNameArray;
    private ArrayList<Integer>  taskID;
    private  View rootView;
    //private OnItemClickListener onItemClickListener ;
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       rootView= inflater.inflate(R.layout.fragment_pcard_task, container, false);

        listView=(ListView)rootView.findViewById(R.id.listView);
        taskNameArray = new ArrayList<String>();
        taskID=new ArrayList<Integer>();
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
                    JSONArray ProjectTasksInformation= jobj.getJSONArray("project_tasks");


                  //  Dictionary<int,int> indexToTaskId=new Dictionary<int, int>() ;
                    for (int i=0; i<ProjectTasksInformation.length();i++)
                    {
                        taskNameArray.add(ProjectTasksInformation.getJSONObject(i).getString("taskName"));
                        taskID.add(ProjectTasksInformation.getJSONObject(i).getInt("taskID"));

                
                    }
                    ArrayAdapter<String> ArrayItems =new ArrayAdapter<String>(PCardTaskFragment.this.getActivity(),android.R.layout.simple_list_item_1, taskNameArray);

                    listView.setAdapter(ArrayItems);

                }
                catch (JSONException e) {
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
        // this.get activity is for fragments



  //  listView

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {

                    Log.d("selected TaskID ","taskID"+(taskID.get(position)));
                    Intent intent=new Intent(getActivity(),TaskCard.class);
                    intent.putExtra("taskID",taskID.get(position));
                    startActivity(intent);

            }
        });
        return rootView;



    }

/*
    @Override
    public void testFunction1()
    {
        //Toast.makeTex,"Clicked",Toast.LENGTH_LONG).show();
    }
    @Override
    public void testFunction2()
    {
        //Toast.makeTex,"Clicked",Toast.LENGTH_LONG).show();
    }*/
}

