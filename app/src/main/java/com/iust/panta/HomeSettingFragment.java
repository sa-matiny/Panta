package com.iust.panta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rayehe on 11/23/2014.
 */
public class HomeSettingFragment extends Fragment {
    View set;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle saveInstanceState){
        set = inflater.inflate(R.layout.fragment_home_setting, container, false);
        return set ;
    }
}
