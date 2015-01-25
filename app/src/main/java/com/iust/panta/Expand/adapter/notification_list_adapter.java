package com.iust.panta.Expand.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private int LayoutResourceId;

    public notification_list_adapter(Context context,ArrayList<notif_row_control> notifs){
        this.context=context;
        this.notifs=new ArrayList<notif_row_control>();
        this.notifs.addAll(notifs);

    }

    public Object getNotif(int NotifId) {
        return notifs.get(NotifId);
    }

    public int getNotifCount() {
        return notifs.size();
    }

    public long getNotifId(int NotifId) {
        return NotifId;

    }

    public View getGroupView(int NotifId, View v, ViewGroup parent) {
        View view=v;
        notif_row_control nc = (notif_row_control) getNotif(NotifId);
        final int id = ((notif_row_control) getNotif(NotifId)).getID();
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

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
