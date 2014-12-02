package com.iust.panta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Home extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public ArrayList<String> listProjectNames;
    public String name;
    public Bundle bundle_data;
    public String userName;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listProjectNames = new ArrayList<String>();
        bundle_data =new Bundle();

        mTitle = getTitle();


        try {

            Intent intent = getIntent();
            Log.d("try", intent.getExtras().getString("projects"));
            JSONArray Projects_obj = new JSONArray(intent.getExtras().getString("projects"));
             bundle_data =intent.getExtras();

            for (int i = 0; i < Projects_obj.length(); i++) {

                JSONObject temp = new JSONObject(Projects_obj.getString(i));
                listProjectNames.add(temp.getString("projectName"));
            }


            JSONObject info = new JSONObject(intent.getExtras().getString("user_info"));
            setTitle(info.getString("name"));
            userName = new String(info.getString("username"));




        } catch (JSONException jsone) {

          //  expGroup = SetStandardGroup(hasData);
            jsone.printStackTrace();

        } catch (NullPointerException npe) {

           npe.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

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
