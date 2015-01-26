package com.iust.panta;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.iust.panta.Expand.adapter.notification_list_adapter;
import com.iust.panta.Expands.notif_row_control;

import java.util.ArrayList;


public class Notification_page extends Activity {

    private ListView notif_list_view;
    private notification_list_adapter notif_adapter;
    private ArrayList<notif_row_control> notification_ready_toadapt;
    private ArrayList<String> list_notif_text;
    private Bundle msg;
    private boolean hasData;
    private SearchView searchView;
    private TextView no_res_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);
        notif_list_view=(ListView)findViewById(R.id.notif_list_view);
        searchView=(SearchView)findViewById(R.id.search_not);
        no_res_label=(TextView)findViewById(R.id.no_res_label_not);
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

        final SearchView.OnQueryTextListener queryTextListener=new SearchView.OnQueryTextListener()
        {
            public boolean onQueryTextChange(String newText) {
                boolean mid=notif_adapter.filterData(newText);
                if(!mid)
                    no_res_label.setText("موردی یافت نشد");
                else
                    no_res_label.setText("");

                notif_adapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                boolean mid=notif_adapter.filterData(query);
                if(!mid)
                    no_res_label.setText("موردی یافت نشد");
                else
                    no_res_label.setText("");
                notif_adapter.notifyDataSetChanged();
                return true;
            }

        };
        searchView.setOnQueryTextListener(queryTextListener);

        final SearchView.OnCloseListener closeListener=new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose() {
                notif_adapter.filterData("");
                return false;
            }
        };
        searchView.setOnCloseListener(closeListener);

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
        Log.d("nashod",setList.toString());
        return setList;
    }


}
