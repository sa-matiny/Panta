package com.iust.panta;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.iust.panta.Expand.adapter.notification_list_adapter;
import com.iust.panta.Expands.notif_row_control;

import java.util.ArrayList;


public class Notification_page extends Activity {

    private ListView notif_list_view;
    private notification_list_adapter notif_adapter;
    private ArrayList<notif_row_control> notification_ready_toadapt;
    private SearchView searchView;
    private ArrayList<String> list_notif_text;
    private Bundle msg;
    private boolean hasData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);
        notif_list_view=(ListView)findViewById(R.id.notif_list_view);
        msg = getIntent().getExtras();
        list_notif_text = msg.getStringArrayList("notification");
        Log.d("notifications", list_notif_text.toString());
        if(list_notif_text.size()!=0)
            hasData=true;
        else
            hasData=false;

        notification_ready_toadapt = setStandardNotif(hasData);

        notif_adapter = new notification_list_adapter(this, notification_ready_toadapt);

        notif_list_view.setAdapter(notif_adapter);


    }
    public void onStart() {
        super.onStart();

    }

    public ArrayList<notif_row_control> setStandardNotif(boolean hasData){


        ArrayList<notif_row_control> setList=new ArrayList<notif_row_control>();
        if(hasData)
        {
            for(int i=0;i<list_notif_text.size();i++)
            {
                notif_row_control nc=new notif_row_control();
                nc.setTitle(list_notif_text.get(i));
                nc.setID(i);
                nc.setShort_exp("");
                setList.add(nc);
            }
        }
        return setList;
    }

    /** @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification_page, menu);
        return true;
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
    }*/
}
