package com.iust.panta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    public boolean f_data;
    public JSONObject job;
    public ArrayList<String> list;
    public String name;
    private Context eee;
    private int home_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        EditText  ed = (EditText) findViewById(R.id.edittext);

        f_data=false;

        try {
           // Log.d("test","inja");
            Intent intent=getIntent();
           // Log.d("try",intent.getExtras().getString("projects"));
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
            if(list.isEmpty())
            {
               // Log.d("flag","umadam");
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
       // Log.d("resid b exlist","ok");
        try {
            Explist = (ExpandableListView) findViewById(R.id.Final_list);

            Expadapter = new ExpandListViewAdapter(Home.this, expGroup);

            Explist.setAdapter(Expadapter);
        }
        catch (NullPointerException nm){
            Log.d("asan moshkel chie",expGroup.toString());}
        //Log.d("try", "injammmm");

        // registerForContextMenu(men);
    }

    public ArrayList<ExpandGroupList> SetStandardGroup(boolean flag) {

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

        Fragment objfrag= null;
        switch (position)
        {
            case 0:
                objfrag=new home_fragment();

                break;
            case 1:
                objfrag=new add_project_fragment();
                break;
            case 2:
                objfrag=new setting_fragment();
                break;
            default:
                objfrag=new home_fragment();
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
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
