package com.iust.panta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rayehe on 11/23/2014.
 */
public class HomeAddProjectFragment extends Fragment {
    View add_pro;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle saveInstanceState){
        add_pro = inflater.inflate(R.layout.fragment_home_add_project, container, false);
        return add_pro ;
    }
}
