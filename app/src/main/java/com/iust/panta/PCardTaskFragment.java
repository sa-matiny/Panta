package com.iust.panta;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PCardTaskFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> taskNameArray;
    private ArrayList<Integer> taskIDArrayList;
    private boolean manager;
    private View rootView;

    private Bundle bundle;

    //private OnItemClickListener onItemClickListener ;
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_pcard_task, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        taskNameArray = new ArrayList<String>();
        taskIDArrayList = new ArrayList<Integer>();
        bundle = new Bundle();
        bundle = getArguments();

        manager = bundle.getBoolean("manager");

        try {
            Log.d("PCardTasks", bundle.toString());
            JSONArray ProjectTasksInformation = new JSONArray(bundle.getString("pro_tasks"));
            //  Dictionary<int,int> indexToTaskId=new Dictionary<int, int>() ;
            for (int i = 0; i < ProjectTasksInformation.length(); i++) {
                taskNameArray.add(ProjectTasksInformation.getJSONObject(i).getString("taskName"));
                taskIDArrayList.add(ProjectTasksInformation.getJSONObject(i).getInt("taskID"));


            }
            ArrayAdapter<String> ArrayItems = new ArrayAdapter<String>(PCardTaskFragment.this.getActivity(), android.R.layout.simple_list_item_1, taskNameArray);

            listView.setAdapter(ArrayItems);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                Log.d("selected TaskID ", "taskIDArrayList" + (taskIDArrayList.get(position)));
                Intent intent = new Intent(getActivity(), TaskCard.class);
                intent.putExtra("taskID", taskIDArrayList.get(position));
                intent.putExtra("manager", manager);
                startActivity(intent);

            }
        });
        return rootView;


    }
}