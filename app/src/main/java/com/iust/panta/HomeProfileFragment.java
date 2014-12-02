package com.iust.panta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.iust.panta.Expand.adapter.ExpandListViewAdapter;
import com.iust.panta.Expands.ExpandChildList;
import com.iust.panta.Expands.ExpandGroupList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rayehe on 11/23/2014.
 */
public class HomeProfileFragment extends Fragment {

    private Bundle bundle;
    private ExpandListViewAdapter expandAdapter;
    private ArrayList<ExpandGroupList> expGroup;
    public ExpandableListView Explist;

    public boolean hasData;

    public HashMap<String,String> project_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = new Bundle();

        bundle = this.getArguments();
        hasData =false;


        try {


            Bundle bundleProjectInfo = new Bundle(bundle.getBundle("Projects"));
            try {


                JSONArray objProjectInfo = new JSONArray(bundleProjectInfo.getString("projects"));
                project_name = new HashMap<String, String>();
                for (int i = 0; i < objProjectInfo.length(); i++) {
                    JSONObject temp = new JSONObject(objProjectInfo.getString(i));
                    project_name.put(temp.getString("projectName"), temp.getString("projectID"));
                }

            } catch (JSONException jex) {
                hasData =false;

            }

        } catch (NullPointerException ne) {
            hasData =false;
            Log.d("Null", "null exep");
        }


        if (project_name.isEmpty()) {
            hasData = false;
            expGroup = SetStandardGroup(hasData);
        } else {
            hasData = true;
            expGroup = SetStandardGroup(hasData);

        }


    }

        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
        saveInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_home_profile, container, false);






       this.Explist = (ExpandableListView) rootView.findViewById(R.id.Final_list);


        expandAdapter = new ExpandListViewAdapter(rootView.getContext(), expGroup);

        Explist.setAdapter(expandAdapter);

            return rootView;
        }


        public ArrayList<ExpandGroupList> SetStandardGroup ( boolean flag){

            ArrayList<ExpandGroupList> expandGroupArray = new ArrayList<ExpandGroupList>();

            if (flag) {
                for (String key:project_name.keySet()) {

                    ArrayList<ExpandChildList> expandChildArray = new ArrayList<ExpandChildList>();
                    ExpandGroupList expandGroup = new ExpandGroupList();
                    expandGroup.SetName(key);
                    expandGroup.setId(project_name.get(key));
                    ExpandChildList expandChild = new ExpandChildList();
                    expandChild.setName("task1");
                    expandChild.setTag(null);
                    expandChildArray.add(expandChild);
                    expandChild = new ExpandChildList();
                    expandChild.setName("task2");
                    expandChild.setTag(null);
                    expandChildArray.add(expandChild);
                    expandGroup.setItemes(expandChildArray);
                    expandGroupArray.add(expandGroup);
                }
            }
            else {

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



