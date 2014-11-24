package com.iust.panta;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * Created by Rayehe on 11/23/2014.
 */
public class home_fragment extends Fragment {
    View home;
    public  View exv;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle saveInstanceState){
        home= inflater.inflate(R.layout.home_view,container,false);
        exv=home.findViewById(R.id.Final_list);
        Log.d("hastam", "inahash");
        return home ;
    }
}
