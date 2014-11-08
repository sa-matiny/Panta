package com.iust.panta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class Profile extends Activity {

    private ExpandListViewAdapter Expadapter;
    private ArrayList<ExpandGroupList> expGroup;
    private ExpandableListView Explist;
    public String[] l;
    public String mYprojectName;
    public boolean f_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        EditText ed = (EditText) findViewById(R.id.edittext);
        f_data=false;
        try {
            Intent intent=getIntent();
            Log.d("try",intent.getExtras().getString("projects"));
            JSONObject jo=new JSONObject();



            mYprojectName = intent.getExtras().getString("projects").replace("}", "");
            mYprojectName.replace("{", "");
            Log.d("projectname", mYprojectName.toString());
            try {
                jo.put("projects", mYprojectName);
                Log.d("Bundle",jo.toString());
            }
            catch (JSONException je){return;}
            if(!mYprojectName.toString().equals("[]")) {
                f_data = true;
                l = mYprojectName.substring(1, mYprojectName.length()-1).split(",");

                Log.d("l0 chie", l[0]);
                Log.d("l1 chie", l[1]);
                Log.d("l2 chie", l[2]);
                Log.d("l3 chie",l[3]);



                Log.d("mine", l[3].substring(2, l[3].length() - 1).split(":")[1]);
                expGroup = SetStandardGroup(f_data);
            }
            else {
                f_data=false;
                expGroup = SetStandardGroup(f_data);
            }
           // else {expGroup = SetStandardGroup(f_data);}

          //  mYprojectName = intentExtra.toString().substring(1, intentExtra.toString().length() - 2).split(",");
          //  mYprojectName = intentExtra.getStringArrayExtra("projects");

        }
        catch(NullPointerException npe) {
            f_data=false;
            expGroup = SetStandardGroup(f_data);
            Log.d("exep","Nabud");
        }
       // ImageButton men = (ImageButton) findViewById(R.id.Button);
        Explist = (ExpandableListView) findViewById(R.id.expandableListView);

        Expadapter = new ExpandListViewAdapter(Profile.this, expGroup);
        Explist.setAdapter(Expadapter);
        //registerForContextMenu(men);
    }

    public ArrayList<ExpandGroupList> SetStandardGroup(boolean flag) {
        ArrayList<ExpandGroupList> lst = new ArrayList<ExpandGroupList>();
        ArrayList<ExpandChildList> lst2 = new ArrayList<ExpandChildList>();

        if (flag) {
            Log.d("try","varede stan if");
           // for (int i = 0; i <l.length; i++) {
                ExpandGroupList gr1 = new ExpandGroupList();
                String temp=l[3].substring(2,l[3].length()).split(":")[1];
                gr1.SetName(temp);
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

        else {
            Log.d("try","varede stan else");
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


    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }


    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return false;
    }


    public void onDestroyActionMode(ActionMode actionMode) {

    }

  /*  public void onCreateContextMenu(ContextMenu m, View v, ContextMenu.ContextMenuInfo menuinf) {

        super.onCreateContextMenu(m, v, menuinf);

        if (v.getId() == R.id.Button) {
            getMenuInflater().inflate(R.menu.mn_u, m);

        }
    }*/

  /*  public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:

                showToast("setting Clicked");

                return true;

            case R.id.action_notif:

                showToast("notification Clicked");

                return true;
            case R.id.action_add:
                showToast("add cliched");

            default:

                return super.onContextItemSelected(item);

        }



    }
    */
    public void showToast(String message) {

        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        toast.show();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mn_u, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void showPopup(View v) {
        PopupMenu pop = new PopupMenu(this, v);

        //  MenuInflater inf=pop.getMenuInflater();
        // inf.inflate(R.menu.acts,pop.getMenu());
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {

            case R.id.action_settings:

                showToast("setting Clicked");

                return true;

            case R.id.action_notif:

                showToast("notification Clicked");

                return true;
            case R.id.action_add:
                showToast("add cliched");

            default:

                return super.onOptionsItemSelected(item);
        }

    }

}