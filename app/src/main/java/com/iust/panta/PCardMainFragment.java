package com.iust.panta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PCardMainFragment extends Fragment {


    private TextView mProName;
    private TextView mProManager;
    private TextView mProProgress;
    private TextView mProInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pcard_main_fragment, container, false);

        mProName = (TextView) rootView.findViewById(R.id.pro_name);
        mProManager = (TextView) rootView.findViewById(R.id.pro_manager);
        mProProgress = (TextView) rootView.findViewById(R.id.pro_progress);
        mProInfo = (TextView) rootView.findViewById(R.id.pro_info);

        return rootView;
    }

}