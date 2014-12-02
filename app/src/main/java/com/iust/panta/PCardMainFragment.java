package com.iust.panta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PCardMainFragment extends Fragment {


    private TextView mProName;
    private TextView mProManager;
    private TextView mProProgress;
    private TextView mProInfo;
    private Bundle msg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pcard_main, container, false);

        mProName = (TextView) rootView.findViewById(R.id.pro_name);
        mProManager = (TextView) rootView.findViewById(R.id.pro_manager);
        mProProgress = (TextView) rootView.findViewById(R.id.pro_progress);
        mProInfo = (TextView) rootView.findViewById(R.id.pro_info);
        msg = new Bundle();
        msg = getArguments();

        try {
            Log.d("4", new String(msg.getByteArray("response")));
            Log.d("RESPONSE", new String(msg.getByteArray("response")));
            JSONObject s_response = new JSONObject(new String(msg.getByteArray("response")));
            JSONObject pro_info = s_response.getJSONObject("projectInfo");
            mProName.setText(pro_info.getString("projectName"));
            mProManager.setText(pro_info.getString("managerName"));
            mProProgress.setText(pro_info.getString("progress"));
            mProInfo.setText(pro_info.getString("project_info"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}