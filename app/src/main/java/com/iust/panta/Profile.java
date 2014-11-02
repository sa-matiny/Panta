package com.iust.panta;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.Adapter;

import java.util.ArrayList;


public class Profile extends ExpandableListActivity implements ExpandableListView.OnChildClickListener {

    int user_ID;

    public Profile(int user_ID)
    {
        this.user_ID=user_ID;
    }
    public Profile(){}


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExpandableListView expandbleLis = getExpandableListView();
        expandbleLis.setDividerHeight(2);
        expandbleLis.setGroupIndicator(null);
        expandbleLis.setClickable(true);

        setGroupData();
        setChildGroupData();

        Adapter mNewAdapter = new Adapter(groupItem, childItem);
        mNewAdapter
                .setInflater(
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                        this);
        getExpandableListView().setAdapter(mNewAdapter);
        expandbleLis.setOnChildClickListener(this);
    }








      /*  ExpandableListView Elv=(ExpandableListView)findViewById(R.id.expandableListViewgc);
        Elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
                return false;
            }
        });
        Button option_b =(Button) findViewById(R.id.option_b);
        option_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        })

      //  Elv.setDividerHeight(2);
       // Elv.setGroupIndicator(null);
       // Elv.setClickable(true);


      //  setGroupData();
       // setChildGroupData();
       */


    public void setGroupData() {
        groupItem.add("پروژه های شما");
        groupItem.add("وظایف شما");

    }

    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    public void setChildGroupData() {

        ArrayList<String> child = new ArrayList<String>();
        /** "child" should initialize by result of query that give the projects name*/
        child.add("p1");
        child.add("p2");
        child.add("p3");
        child.add("p4");
        childItem.add(child);

        /**
         * and this one should initialize by another query result to set tasks list
         */
        child = new ArrayList<String>();
        child.add("t1");
        child.add("t2");
        child.add("t3");
        child.add("t4");
        childItem.add(child);
    }


    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long l) {
        return false;
    }
}



