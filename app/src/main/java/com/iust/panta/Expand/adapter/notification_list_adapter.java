package com.iust.panta.Expand.adapter;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iust.panta.Expands.ExpandGroupList;
import com.iust.panta.Expands.notif_row_control;
import com.iust.panta.R;

import java.util.ArrayList;

/**
 * Created by Rayehe on 1/21/2015.
 */
public class notification_list_adapter  extends BaseAdapter {


    private Context context;
    private ArrayList<notif_row_control> notifs;
    private ArrayList<notif_row_control> original;
    private int LayoutResourceId;

    public notification_list_adapter(Context context,ArrayList<notif_row_control> notifs){
        this.context=context;
        this.notifs=new ArrayList<notif_row_control>();
        this.notifs.addAll(notifs);
        this.original=notifs;
        Log.d("vasate sakht", "notif nabud :/");
    }


    @Override
    public int getCount() {
        return notifs.size();
    }

    @Override
    public Object getItem(int NotifId) {
        return notifs.get(NotifId);
    }

    @Override
    public long getItemId(int NotifId) {
        return NotifId;

    }

    @Override
    public View getView(int NotifId, View v, ViewGroup parent) {

        notif_row_control nc = (notif_row_control) getItem(NotifId);
        final int id = ((notif_row_control) getItem(NotifId)).getID();
        if (v == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            v = inf.inflate(R.layout.notif_row, null);
        }
        TextView tv = (TextView) v.findViewById(R.id.notif_title);
        tv.setText(nc.getTitle());
        TextView sd=(TextView) v.findViewById(R.id.notif_short_explain);
        sd.setText(nc.getShort_exp());
        v.getTag();
        return v;}


    public boolean filterData(String query){

        notifs.clear();

        if(query.isEmpty()){

            notifs.clear();
            notifs.addAll(original);
            return true;
        }
        else if( query==null){
            notifs.clear();
            return true;
        }
        else {
            notifs.clear();
            ArrayList<notif_row_control> newList = new ArrayList<notif_row_control>();
            for(notif_row_control notification: original){
                if(notification.getTitle().toLowerCase().contains(query.toLowerCase()) ){
                    newList.add(notification);
                }

            }
            if(newList.size() > 0){
                notifs.addAll(newList);
                return true;
            }
            else {
                notifs.clear();
                Log.d("no", "no result");
            }
        }

        notifyDataSetChanged();
        return false;

    }
}
