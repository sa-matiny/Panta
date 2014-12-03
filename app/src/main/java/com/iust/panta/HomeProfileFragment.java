package com.iust.panta;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.iust.panta.Expand.adapter.ExpandListViewAdapter;
import com.iust.panta.Expands.DataHelper;
import com.iust.panta.Expands.ExpandChildList;
import com.iust.panta.Expands.ExpandGroupList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rayehe on 11/23/2014.
 *
 */


public class HomeProfileFragment extends Fragment {

    private Bundle bundle;
    private ExpandListViewAdapter expandAdapter;
    private ArrayList<ExpandGroupList> expGroup;
    public ExpandableListView Explist;

    private ArrayList<String> listProjectsName;
    private ArrayList<String> listProjectsID;
    private ArrayList<ArrayList<String>> listEachProjectsTask;
    private ArrayList<ArrayList<String>> listEachProjectsTaskID;

    public boolean hasData;

    public HashMap<String, String> project_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = new Bundle();
        Bundle recievie = new Bundle();

        recievie = this.getArguments();
        bundle = recievie.getBundle("Projects");
        hasData = true;

        listEachProjectsTask = new ArrayList<ArrayList<String>>();
        listEachProjectsTaskID = new ArrayList<ArrayList<String>>();

        listProjectsName = new ArrayList<String>();
        listProjectsID= new ArrayList<String >();


        listProjectsName = (bundle.getStringArrayList("projectsName"));
        listProjectsID=(bundle.getStringArrayList("projectsID"));
        DataHelper d=(DataHelper)bundle.getSerializable("tasksName");
        listEachProjectsTask=d.getList();
        d=(DataHelper)bundle.getSerializable("tasksID");
        listEachProjectsTaskID=d.getList();

        // Log.d("seriazable",(listEachProjectsTask.toString()));
        //  listEachProjectsTask=new ArrayList<ArrayList<String>>().get;


        //  JSONArray objProjectInfo = new JSONArray(bundleProjectInfo.getString("projects"));
        if (listProjectsName.isEmpty()) {
            hasData = false;
        }


        expGroup = SetStandardGroup(hasData);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            saveInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_profile, container, false);


        this.Explist = (ExpandableListView) rootView.findViewById(R.id.Final_list);


        expandAdapter = new ExpandListViewAdapter(rootView.getContext(), expGroup);

        Explist.setAdapter(expandAdapter);

        Explist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

              //
              //  final String idd =
               // Log.d("Clicccccked",id);
                 String idd=listEachProjectsTaskID.get(groupPosition).get(childPosition);

                 Intent intent=new Intent(HomeProfileFragment.this.getActivity(),TaskCard.class);
                 intent.putExtra("taskID",Integer.parseInt(idd));
                 startActivity(intent);

                return true;
            }
        });


        return rootView;
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
                /*else
                  {
                      //I
                  }*/
                expandGroup.setItemes(expandChildArray);
                expandGroupArray.add(expandGroup);

            }
        } else {

            Toast.makeText(this.getActivity(), "برای شما پروژه ای موجود نمی باشد .", Toast.LENGTH_LONG).show();


        }
        return expandGroupArray;
    }

}

/**
 * Back button listener.
 * Will close the application if the back button pressed twice.
 */

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("می خواهید از برنامه خرج شوید؟");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}*/



