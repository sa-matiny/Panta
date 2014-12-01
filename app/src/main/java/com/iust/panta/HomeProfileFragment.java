package com.iust.panta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ExpandableListView;

import com.iust.panta.Expand.adapter.ExpandListViewAdapter;
import com.iust.panta.Expands.ExpandChildList;
import com.iust.panta.Expands.ExpandGroupList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarException;

/**
 * Created by Rayehe on 11/23/2014.
 */
public class HomeProfileFragment extends Fragment {

    private Bundle bundle;
    private ExpandListViewAdapter Expadapter;
    private ArrayList<ExpandGroupList> expGroup;
    public ExpandableListView Explist;
    public boolean f_data;
    public JSONObject job;
    public HashMap<String ,Integer> tasks;
    public HashMap<String,String> project_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = new Bundle();

        bundle = this.getArguments();
        f_data=false;

        Log.d("GetBundle", bundle.toString());
        try {


            Bundle yeki = new Bundle(bundle.getBundle("Projects"));
            try {


                JSONArray jarray = new JSONArray(yeki.getString("projects"));
                String unyeki = yeki.getString("projects");
                Log.d("array", yeki.toString());
                Log.d("jarray", jarray.toString());

                project_name = new HashMap<String, String>();
                for (int i = 0; i < jarray.length(); i++) {


                    JSONObject temp = new JSONObject(jarray.getString(i));
                    String name = temp.getString("projectName");
                    String Id = temp.getString("projectID");
                    Log.d("Nm", name);
                    // Log.d("id",Id.toString());

                    //JSONObject temptemp=new JSONObject(temp.getString())
                    project_name.put(temp.getString("projectName"), temp.getString("projectID"));
                }
                Log.d("message", project_name.values().toString());

            } catch (JSONException jex) {
                f_data=false;
                Log.d("j", "jjj");
            }

        } catch (NullPointerException ne) {
            f_data=false;
            Log.d("Null", "null exep");
        }


        if (project_name.isEmpty()) {
            // Log.d("flag","umadam");
            f_data = false;
            expGroup = SetStandardGroup(f_data);
        } else {
            f_data = true;
            expGroup = SetStandardGroup(f_data);

        }


    }

        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
        saveInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_home_profile, container, false);






       this.Explist = (ExpandableListView) rootView.findViewById(R.id.Final_list);


        Expadapter = new ExpandListViewAdapter(rootView.getContext(), expGroup);

        Explist.setAdapter(Expadapter);

            return rootView;
        }


        public ArrayList<ExpandGroupList> SetStandardGroup ( boolean flag){

            ArrayList<ExpandGroupList> lst = new ArrayList<ExpandGroupList>();

            if (flag) {
                // Log.d("try", "varede stan if");

                for (String key:project_name.keySet()) {

                    ArrayList<ExpandChildList> lst2 = new ArrayList<ExpandChildList>();
                    ExpandGroupList gr1 = new ExpandGroupList();
                    gr1.SetName(key);
                    ExpandChildList ch1 = new ExpandChildList();
                    ch1.setName("tast1");
                    ch1.setTag(null);
                    lst2.add(ch1);
                    ExpandChildList ch1_2 = new ExpandChildList();
                    ch1_2.setName("task2");
                    ch1_2.setTag(null);
                    lst2.add(ch1_2);
                    gr1.setItemes(lst2);
                    lst.add(gr1);
                }
            } else {
                ArrayList<ExpandChildList> lst2 = new ArrayList<ExpandChildList>();
                Log.d("try", "varede stan else");
                ExpandGroupList gr1 = new ExpandGroupList();
                gr1.SetName("پروژه");
                ExpandChildList ch1 = new ExpandChildList();
                ch1.setName("tast1");
                ch1.setTag(null);
                lst2.add(ch1);
                ExpandChildList ch1_2 = new ExpandChildList();
                ch1_2.setName("task2");
                ch1_2.setTag(null);
                lst2.add(ch1_2);
                ExpandChildList ch1_3 = new ExpandChildList();
                ch1_3.setName("task3");
                ch1_3.setTag(null);
                lst2.add(ch1_3);
                gr1.setItemes(lst2);
                // lst.add(gr1);
                lst2 = new ArrayList<ExpandChildList>();
                ExpandGroupList gr2 = new ExpandGroupList();
                gr2.SetName("اون یکی پروژه");
                ExpandChildList ch2_1 = new ExpandChildList();
                ch2_1.setName("tast1");
                ch2_1.setTag(null);
                lst2.add(ch2_1);
                ExpandChildList ch2_2 = new ExpandChildList();
                ch2_2.setName("task2");
                ch2_2.setTag(null);
                lst2.add(ch2_2);
                gr2.setItemes(lst2);
                lst.add(gr1);
                lst.add(gr2);
            }
            return lst;
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



