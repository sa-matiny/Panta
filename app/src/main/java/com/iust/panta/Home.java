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
import android.widget.ExpandableListView;

import com.iust.panta.Expand.adapter.ExpandListViewAdapter;
import com.iust.panta.Expands.ExpandChildList;
import com.iust.panta.Expands.ExpandGroupList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Home extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public boolean f_data;
    public ArrayList<String> list;
    public String name;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ExpandListViewAdapter Expadapter;
    private ArrayList<ExpandGroupList> expGroup;
    private ExpandableListView Explist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("11111111111","1111111111");




        Log.d("55555","5555555");

        mTitle = getTitle();

        // Set up the drawer.






        f_data=false;

        try {
           // Log.d("test","inja");
            Intent intent = getIntent();
            Log.d("try",intent.getExtras().getString("projects"));
            JSONArray unyekijo =new JSONArray(intent.getExtras().getString("projects"));

           // Log.d("array",unyekijo.toString());

            JSONObject info = new JSONObject(intent.getExtras().getString("user_info"));
            setTitle(info.getString("name"));
            list=new ArrayList<String>();
            for(int i=0;i<unyekijo.length();i++)
            {

                JSONObject temp=new JSONObject(unyekijo.getString(i));
                list.add(temp.getString("projectName"));
            }

            Log.d("myList",list.toString());
            if(list.isEmpty())
            {

                f_data=false;
                expGroup = SetStandardGroup(f_data);
            }
            else {
                f_data=true;
                expGroup=SetStandardGroup(f_data);

            }


        }
        catch (JSONException jsone){f_data=false;
            expGroup = SetStandardGroup(f_data);
           // Log.d("exep","Nabud");
           }


        catch(NullPointerException npe) {

            f_data=false;
            expGroup=SetStandardGroup(f_data);
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // Log.d("resid b exlist","ok");
        try {
            Explist = (ExpandableListView) findViewById(R.id.Final_list);

            Expadapter = new ExpandListViewAdapter(Home.this, expGroup);

            Explist.setAdapter(Expadapter);
        }
        catch (NullPointerException nm){
            Log.d("asan moshkel chie",expGroup.toString());}
        //Log.d("try", "injammmm");

        Log.d("2","2");
        // registerForContextMenu(men);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public ArrayList<ExpandGroupList> SetStandardGroup(boolean flag) {


        Log.d("3","3");
        ArrayList<ExpandGroupList> lst = new ArrayList<ExpandGroupList>();

        if (flag) {
           // Log.d("try", "varede stan if");

            for (int i = 0; i < this.list.size(); i++) {

                ArrayList<ExpandChildList> lst2 = new ArrayList<ExpandChildList>();
                ExpandGroupList gr1 = new ExpandGroupList();
                gr1.SetName(list.get(i));
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
        }



        else {
            ArrayList<ExpandChildList> lst2 = new ArrayList<ExpandChildList>();
           // Log.d("try","varede stan else");
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Log.d("4","4");
        Fragment objfrag= null;
        Bundle bundle =new Bundle();
        Log.d("Selected******","selected");

        switch (position)
        {
            case 0: {
                objfrag = new HomeProfileFragment();
                //Log.d("Mylist in case",list.toString());
                bundle.putStringArrayList("Projects",list);
                Log.i("Bundle",bundle.toString());
                objfrag.setArguments(bundle);

            }
                break;
            case 1:
                objfrag = new HomeAddProjectFragment();
                break;
            case 2:
                objfrag = new HomeSettingFragment();
                break;
            default:
            {
                objfrag = new HomeProfileFragment();
                Log.d("defult Mylist in case",list.toString());
                bundle.putStringArrayList("Projects",list);
                Log.i("defult Bundle",bundle.toString());
                objfrag.setArguments(bundle);

            }
                break;

        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objfrag)
                .commit();    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle=name;
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
