package com.iust.panta.Expand.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.iust.panta.Expands.ExpandChildList;
import com.iust.panta.Expands.ExpandGroupList;
import com.iust.panta.ProjectCard;
import com.iust.panta.R;

import java.util.ArrayList;

/**
 * Created by Rayehe on 11/3/2014.
 */
public class ExpandListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ExpandGroupList> groups;

    public ExpandListViewAdapter(Context context, ArrayList<ExpandGroupList> groups) {
        this.context = context;
        this.groups = groups;
    }

    public void addItem(ExpandChildList item, ExpandGroupList group) {
        if (!groups.contains(group)) {
            groups.add(group);
        }
        int indes = groups.indexOf(group);
        ArrayList<ExpandChildList> child = groups.get(indes).getItemes();
        child.add(item);
        groups.get(indes).setItemes(child);
    }

    public ExpandChildList getChild(int groupPosition, int childPosition) {
        ArrayList<ExpandChildList> child_list = groups.get(groupPosition).getItemes();
        return child_list.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPositon, boolean isLastChild, View v, ViewGroup parent) {
        ExpandChildList child = (ExpandChildList) getChild(groupPosition, childPositon);
        if (v == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.exp_child, null);
        }
        TextView tv = (TextView) v.findViewById(R.id.tvChild);
        tv.setText(child.getName().toString());
        tv.setTag(child.getTag());
        return v;
    }

    public int getChildrenCount(int groupPosition) {
        ArrayList<ExpandChildList> chl = groups.get(groupPosition).getItemes();
        return chl.size();
    }

    public Object getGroup(int groupPositon) {
        return groups.get(groupPositon);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;

    }

    public View getGroupView(int groupPosition, boolean isLastChild, View v, ViewGroup parent) {
        ExpandGroupList g = (ExpandGroupList) getGroup(groupPosition);
        final String id = ((ExpandGroupList) getGroup(groupPosition)).getId();
        TextView button = null;
        if (v == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.exp_group, null);

         /*   holder=new viewHolder(v);
              v.setTag(holder);
              holder=(viewHolder) v.getTag();*/
        }
        button = (TextView) v.findViewById(R.id.enter_card);
        TextView tv = (TextView) v.findViewById(R.id.tvGroup);
        tv.setText(g.getName());

        v.getTag();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int idd = Integer.parseInt(id);

                Log.d("clicked", Integer.toString(idd));
                Intent intent = new Intent(context, ProjectCard.class);
                intent.putExtra("projectId", idd);
                context.startActivity(intent);


            }
        });
        return v;
    }


    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }


    private class viewHolder {
        private View mRow;
        private TextView description = null;
        private Button enter = null;

        public viewHolder(View row) {
            mRow = row;
        }

        public TextView getDescription() {
            if (description == null) {
                description = (TextView) mRow.findViewById(R.id.tvGroup);
            }
            return description;
        }

        public Button getEnter() {
            if (enter == null) {
                enter = (Button) mRow.findViewById(R.id.enter_card);

            }
            return enter;
        }
    }
}
