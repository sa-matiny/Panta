package com.iust.panta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.iust.panta.Expands.DataHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Home extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private ArrayList<String> listProjectNames;
    private ArrayList<String> listProjectIDs;
    private ArrayList<ArrayList<String>> listTaskNamesforEachProjects;
    private ArrayList<ArrayList<String>> listTasksIDs;
    private String name;
    private Bundle bundle_data;
    private String userName;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    //private class act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listProjectNames = new ArrayList<String>();
        listProjectIDs=new ArrayList<String>();
        listTaskNamesforEachProjects = new ArrayList<ArrayList<String>>();
        listTasksIDs=new ArrayList<ArrayList<String>>();
        //      super.onCreate();
        bundle_data = new Bundle();

        mTitle = getTitle();

        super.onCreate(savedInstanceState);
        try {


            Intent intent = getIntent();
            // bundle_data=intent.getExtras();
            JSONObject info = new JSONObject(intent.getExtras().getString("user_info"));
            setTitle(info.getString("name"));
            userName = new String(info.getString("username"));
            Log.d("username_profile", userName);
        } catch (JSONException info) {

            Log.d("trye gonde", "tryGonde");
        }
        RequestParams params = new RequestParams();
        params.put("username", userName);
        // params.put("password", password);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800/view_profile/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart()
            {
                System.out.println("profileStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {

                    Log.d("successs","success");
                    //        Log.d("try", intent.getExtras().getString("projects"));
                    //      JSONArray Projects_obj = new JSONArray(intent.getExtras().getString("projects"));
                    //  Log.d("profile_respone",new String(response));

                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray Projects_obj = jObject.getJSONArray("projects");

                    Log.d("projectsjsonArray", Projects_obj.toString());

                    //        bundle_data =new Bundle(new String(response));
                    for (int i = 0; i < Projects_obj.length(); i++) {

                        Log.d("jsonArrayItems", Projects_obj.get(i).toString());

                        JSONObject eachProjects = Projects_obj.getJSONObject(i);

                        Log.d("projectName :", eachProjects.getString("projectName"));
                        listProjectNames.add(eachProjects.getString("projectName"));
                        Log.d("projectID :", eachProjects.getString("projectID"));
                        listProjectIDs.add(eachProjects.getString("projectID"));
                        JSONArray jsonTasks = eachProjects.getJSONArray("tasks");
                        ArrayList<String> eachTask = new ArrayList<String>();
                        ArrayList<String> eachTaskID= new ArrayList<String>();


                        for (int j = 0; j < jsonTasks.length(); j++) {

                            JSONObject taskInformation = jsonTasks.getJSONObject(j);
                            Log.d("tasks :" + j, taskInformation.getString("taskName"));
                            Log.d("tasks :" + j, taskInformation.getString("taskID"));
                            eachTask.add(taskInformation.getString("taskName"));
                            eachTaskID.add(taskInformation.getString("taskID"));

                        }
                        Log.d("end of for 2","end of for 2");
                        listTaskNamesforEachProjects.add(eachTask);
                        listTasksIDs.add(eachTaskID);


                        // listProjectNames.add(temp.getString("projectName"));
                    }
                    bundle_data.putSerializable("tasksName", new DataHelper(listTaskNamesforEachProjects));
                    bundle_data.putStringArrayList("projectsName", listProjectNames);
                    bundle_data.putStringArrayList("projectsID", listProjectIDs);
                    bundle_data.putSerializable("tasksID",new DataHelper(listTasksIDs));
                } catch (JSONException jsone) {

                    //  expGroup = SetStandardGroup(hasData);
                    jsone.printStackTrace();
                    Log.d("exception", "exept");

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                Log.d("problem", "problem");
              /*  mProgressView.setVisibility(View.GONE);
                mButtonView.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setMessage("خطا! دوباره وارد شوید");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();*/
            }

            @Override
            public void onFinish() {



                setContentView(R.layout.activity_home);
                mNavigationDrawerFragment = (NavigationDrawerFragment)
                        getFragmentManager().findFragmentById(R.id.navigation_drawer);

                mNavigationDrawerFragment.setUp(
                        R.id.navigation_drawer,
                        (DrawerLayout) findViewById(R.id.drawer_layout));


            }


        });
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.d("hereeee", "hereee");

        Fragment objfrag = null;
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:

                objfrag = new HomeProfileFragment();
                bundle.putBundle("Projects", bundle_data);
                objfrag.setArguments(bundle);


                break;
            case 1:
                bundle.putString("username", userName);
                objfrag = new HomeAddProjectFragment();
                objfrag.setArguments(bundle);
                break;
            case 2:
                objfrag = new HomeSettingFragment();
                break;
            default:
                objfrag = new HomeProfileFragment();
                bundle.putBundle("Projects", bundle_data);
                objfrag.setArguments(bundle);


                break;

        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objfrag)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = name;
            case 2:
                mTitle = getString(R.string.Home_section);
                break;
            case 3:
                mTitle = getString(R.string.Setting_section);
                break;
            case 4:
                mTitle = getString(R.string.add_section);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}

//testing


