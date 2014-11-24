package com.iust.panta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rayehe on 11/23/2014.
 */
public class HomeProfileFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle saveInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_home_profile, container, false);

        return rootView;
    }
}
