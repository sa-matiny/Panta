package com.iust.panta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class PCardMembersFragment extends Fragment {

    private View rootView;
    private ListView listView;
    private ArrayList<String> memberArray;
    private Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize
        rootView = inflater.inflate(R.layout.fragment_pcard_members, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);

        bundle = new Bundle();
        bundle = getArguments();

        try {
            Log.d("PCardMembers", bundle.toString());
            JSONArray ProjectMembersInformation = new JSONArray(bundle.getString("pro_users"));
            //  Dictionary<int,int> indexToTaskId=new Dictionary<int, int>() ;
            memberArray = new ArrayList<String>();
            for (int i = 0; i < ProjectMembersInformation.length(); i++) {
                memberArray.add(ProjectMembersInformation.getJSONObject(i).getString("name"));
                // Log.d("my members",projects.get(i).toString());

            }
            //  Log.d("memberArray",memberArray.toString());
            ArrayAdapter<String> ArrayItems = new ArrayAdapter<String>(PCardMembersFragment.this.getActivity(), android.R.layout.simple_list_item_1, memberArray);

            listView.setAdapter(ArrayItems);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
