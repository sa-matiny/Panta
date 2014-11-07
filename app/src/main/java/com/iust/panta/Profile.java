package com.iust.panta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.iust.panta.Expand.adapter.ExpandListViewAdapter;
import com.iust.panta.Expands.ExpandChildList;
import com.iust.panta.Expands.ExpandGroupList;

import org.json.JSONArray;

import java.util.ArrayList;



public class Profile extends Activity {

    private ExpandListViewAdapter Expadapter;
    private ArrayList<ExpandGroupList> expGroup;
    private ExpandableListView Explist;
    public Intent intentExtra;
    public String[] mYprojectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        EditText ed = (EditText) findViewById(R.id.edittext);
        intentExtra.getExtras();
        mYprojectName=intentExtra.toString().substring(1,intentExtra.toString().length()-2).split(",");
        ImageButton men = (ImageButton) findViewById(R.id.Button);
        Explist = (ExpandableListView) findViewById(R.id.expandableListView);
        expGroup = SetStandardGroup();
        Expadapter = new ExpandListViewAdapter(Profile.this, expGroup);
        Explist.setAdapter(Expadapter);
        registerForContextMenu(men);
    }

    public ArrayList<ExpandGroupList> SetStandardGroup() {
        ArrayList<ExpandGroupList> lst = new ArrayList<ExpandGroupList>();
        ArrayList<ExpandChildList> lst2 = new ArrayList<ExpandChildList>();



            for(int i=0;i<mYprojectName.length;i++)
            {
                ExpandGroupList gr1 = new ExpandGroupList();
                gr1.SetName(mYprojectName[i]);
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
                lst.add(gr1);
        }

        /*
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
        */
        return lst;

    }


    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }


    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }


    public void onDestroyActionMode(ActionMode actionMode) {

    }

    public void onCreateContextMenu(ContextMenu m, View v, ContextMenu.ContextMenuInfo menuinf) {

        super.onCreateContextMenu(m, v, menuinf);

        if (v.getId() == R.id.Button) {
            getMenuInflater().inflate(R.menu.mn_u, m);

        }
    }

    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search:

                showToast("search Clicked");

                return true;

            case R.id.notif:

                showToast("notification Clicked");

                return true;
            case R.id.add_new:
                showToast("add cliched");

            default:

                return super.onContextItemSelected(item);

        }

    }

    public void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void showPopup(View v) {
        PopupMenu pop = new PopupMenu(this, v);

        //  MenuInflater inf=pop.getMenuInflater();
        // inf.inflate(R.menu.acts,pop.getMenu());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       /* if (id == R.id.action_settings) {
            showToast("search Clicked1");
            return true;
        }*/
        switch (item.getItemId()) {

            case R.id.search:

                showToast("search Clicked");

                return true;

            case R.id.notif:

                showToast("notification Clicked");

                return true;
            case R.id.add_new:
                showToast("add cliched");

            default:

                return super.onOptionsItemSelected(item);
        }

    }

}