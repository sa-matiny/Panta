package com.iust.panta;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iust.panta.Expand.adapter.ExpandListViewAdapter;
import com.iust.panta.Expands.ExpandChildList;
import com.iust.panta.Expands.ExpandGroupList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class HomeProfileFragment extends Fragment {
    public ExpandableListView Explist;
    public boolean hasData;
    private ExpandListViewAdapter expandAdapter;
    private ArrayList<ExpandGroupList> expGroup;
    private ArrayList<String> listProjectsName;
    private ArrayList<String> listProjectsID;
    private ArrayList<String> listManagerUsers;
    private ArrayList<ArrayList<String>> listEachProjectsTask;
    private ArrayList<ArrayList<String>> listEachProjectsTaskID;
    private View rootView;
    private ProgressBar mProgressView;
    private String userName;
    public ArrayList<String > notification_title;
    private SearchView searchView;
    private TextView no_res_label;

    public void onCreate (Bundle savedInstanceState)
    {
        notification_title=new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            saveInstanceState) {
       // setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.fragment_home_profile, container, false);
        Explist = (ExpandableListView) rootView.findViewById(R.id.Final_list);

        mProgressView = (ProgressBar) rootView.findViewById(R.id.Profile_progress);

        Bundle msg = getArguments();
        userName = msg.getString("username");
        Log.d("username_profile", userName);



        searchView =
                (SearchView) rootView.findViewById(R.id.search);

        no_res_label=(TextView)rootView.findViewById(R.id.no_res_label);




        return rootView;



    }

    @Override


    public void onStart() {
        super.onStart();

        listEachProjectsTask = new ArrayList<ArrayList<String>>();
        listEachProjectsTaskID = new ArrayList<ArrayList<String>>();

        listManagerUsers = new ArrayList<String>();

        listProjectsName = new ArrayList<String>();
        listProjectsID= new ArrayList<String >();

        mProgressView.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        params.put("username", userName);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800/view_profile/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("profileStart");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                mProgressView.setVisibility(View.GONE);
                notification_title=new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray Projects_obj = jObject.getJSONArray("projects");
                    JSONArray Notif_title=jObject.getJSONArray("notification");
                    Log.d("notif",Notif_title.toString());
                    Log.d("projectsjsonArray", Projects_obj.toString());


                    if(Notif_title.length()!=0)
                    {
                        for(int i=0;i<Notif_title.length();i++) {


                            JSONObject not = Notif_title.getJSONObject(i);
                            String msgtype = not.getString("msg_type");
                            String sentence = not.getString("message");
                            String message = "";
                            String task_name1="";

                            if (msgtype.equals("1")) {

                                message = "کاربر " + " "+sentence +" "+ " وظیفه ی خود را انجام داده است";


                            }
                            if (msgtype.equals("2")) {

                                message = "شما به پروژه " +" "+ sentence +" "+ " اضافه شدید";


                            }

                            if (msgtype.equals("3")) {

                                message = "برای شما وظیفه ی جدید در پروژه ی " +" " +sentence +" "+ "تعریف شده است .";

                            }
                            if (msgtype.equals("4")) {
                                message = "زمان پروژه " + sentence + "به پایان رسیده است .";


                            }
                            if (msgtype.equals("5")) {


                                task_name1=not.getString("data");
                                message = "زمان وظیفه ی " + task_name1 + "در پروژه ی" +" "+ sentence +" "+ "به پایان رسیده است .";


                            }
                            if (msgtype.equals("6")) {

                                task_name1=not.getString("data");
                                message = "زمان وظیفه ی " + task_name1 + "برای کاربر " +" "+ sentence +" "+ "به پایان رسیده است.";




                            }

                            notification_title.add(message.toString());
                            message="";
                        }
                    }

                    else
                    {
                        String[] a={""};
                        notification_title=new ArrayList<String>(Arrays.asList(a));
                    }

                    for (int i = 0; i < Projects_obj.length(); i++) {

                        Log.d("jsonArrayItems", Projects_obj.get(i).toString());

                        JSONObject eachProjects = Projects_obj.getJSONObject(i);

                        Log.d("projectName :", eachProjects.getString("projectName"));
                        listProjectsName.add(eachProjects.getString("projectName"));
                        Log.d("projectID :", eachProjects.getString("projectID"));
                        listProjectsID.add(eachProjects.getString("projectID"));
                       listManagerUsers.add(eachProjects.getString("managerUser"));
                        JSONArray jsonTasks = eachProjects.getJSONArray("tasks");
                        ArrayList<String> eachTask = new ArrayList<String>();
                        ArrayList<String> eachTaskID = new ArrayList<String>();


                        for (int j = 0; j < jsonTasks.length(); j++) {

                            JSONObject taskInformation = jsonTasks.getJSONObject(j);
                            Log.d("tasks :" + j, taskInformation.getString("taskName"));
                            Log.d("tasks :" + j, taskInformation.getString("taskID"));
                            eachTask.add(taskInformation.getString("taskName"));
                            eachTaskID.add(taskInformation.getString("taskID"));

                        }
                        Log.d("end of for 2", "end of for 2");
                        listEachProjectsTask.add(eachTask);
                        listEachProjectsTaskID.add(eachTaskID);

                        hasData = true;
                        if (listProjectsName.isEmpty()) {
                            hasData = false;
                        }



                    }

                } catch (JSONException jsone) {
                    jsone.printStackTrace();


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                mProgressView.setVisibility(View.GONE);
                System.out.println("Fail");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setMessage("خطا! اتصال به اینترنت با مشکل مواجه است");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                String[] a={"failed to load"};
                notification_title=new ArrayList<String>(Arrays.asList(a));
                Log.d("nashod","notif nabud :/");



                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onFinish() {


                //getActivity().invalidateOptionsMenu();
                expGroup = SetStandardGroup(hasData);

                expandAdapter = new ExpandListViewAdapter(rootView.getContext(), expGroup);

                Explist.setAdapter(expandAdapter);

                Explist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        String idd = listEachProjectsTaskID.get(groupPosition).get(childPosition);

                        Intent intent = new Intent(HomeProfileFragment.this.getActivity(), TaskCard.class);
                        if (userName.equals(listManagerUsers.get(groupPosition))) {
                            Log.d("yesss", "yess");
                            intent.putExtra("manager", true);//
                        } else {
                            Log.d("No", "yess");
                            intent.putExtra("manager", false);
                        }
                        intent.putExtra("taskID", Integer.parseInt(idd));
                        startActivity(intent);

                        return true;
                    }
                });

            }

        });

        //////////////////////////////////notif

    /*    this.notification_title=new ArrayList<String>();
        String[]b={"aa","bb","cc"};
        this.notification_description=new ArrayList<String>(Arrays.asList(b));
        RequestParams paramNot = new RequestParams();
        paramNot.put("username", userName);

        AsyncHttpClient clientNotifReq = new AsyncHttpClient();

        clientNotifReq.post("http://104.236.33.128:8800/getNotif/", paramNot, new AsyncHttpResponseHandler() {


            @TargetApi(Build.VERSION_CODES.KITKAT)

            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                try {

                    Log.d("RESPONSE_Notificationsss", new String(response));
                    JSONObject s_response = new JSONObject(new String(response));
                    if (s_response.getBoolean("successful")) {
                        Log.d("not",s_response.toString());
                        JSONArray notifs = s_response.getJSONArray("notification");
                        Log.d("notifsss",notifs.toString());

                        if(notifs.length()!=0)
                        {
                            for(int i=0;i<notifs.length();i++){

                                JSONObject not=notifs.getJSONObject(i);
                                String message=not.getString("msg");
                                notification_title.add(message.toString());


                            }}
                        else
                        {
                            String[] a={"asuccessemt","b","c"};
                            notification_title=new ArrayList<String>(Arrays.asList(a));
                        }



                    }}

                catch (JSONException je){}
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String[] a={"aFailure","b","c"};
                notification_title=new ArrayList<String>(Arrays.asList(a));
                getActivity().invalidateOptionsMenu() ;
                Log.d("nashod","notif nabud :/");


            }
        });*/
        //////////////////////////////////endnotif




        final SearchView.OnQueryTextListener queryTextListener=new SearchView.OnQueryTextListener()
        {
            public boolean onQueryTextChange(String newText) {
                boolean mid=expandAdapter.filterData(newText);
                if(!mid)
                    no_res_label.setText("موردی یافت نشد");
                else
                    no_res_label.setText("");

                expandAdapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                boolean mid=expandAdapter.filterData(query);
                if(!mid)
                    no_res_label.setText("موردی یافت نشد");
                else
                    no_res_label.setText("");
                expandAdapter.notifyDataSetChanged();
                return true;
            }

        };
        searchView.setOnQueryTextListener(queryTextListener);

        final SearchView.OnCloseListener closeListener=new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose() {
                expandAdapter.filterData("");
                return false;
            }
        };
        searchView.setOnCloseListener(closeListener);

    }

   /* public void OnCreateOptionMenu(Menu menu){
        menu.clear();
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        for(int i=0;i<notification_title.size();i++)
        {
            menu.add(notification_title.get(i));
        }
    }*/

    /*public void onPrepareOptionsMenu (Menu menu) {

            menu.clear();
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            int begin=0;
            if(notification_title.size()<10)
                begin=0;
            else
                begin=notification_title.size()-10;
            for(int i=begin;i<notification_title.size();i++)
            {
                menu.add(notification_title.get(i));
            }
    }*/



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notif:
                Intent intent_notif = new Intent(getActivity(),Notification_page.class);
                intent_notif.putExtra("notification",notification_title);
                startActivity(intent_notif);
                // Not implemented here
                return true;
            default:
                break;
        }
        return false;
    }


    public ArrayList<ExpandGroupList> SetStandardGroup(boolean flag) {

        ArrayList<ExpandGroupList> expandGroupArray = new ArrayList<ExpandGroupList>();

        if (flag) {
            for (int i = 0; i < listProjectsName.size(); i++) {

                ArrayList<ExpandChildList> expandChildArray = new ArrayList<ExpandChildList>();
                ExpandGroupList expandGroup = new ExpandGroupList();
                expandGroup.SetName(listProjectsName.get(i));
                expandGroup.setId(listProjectsID.get(i));

                if (!listEachProjectsTask.get(i).isEmpty()) {

                    for (int j = 0; j < listEachProjectsTask.get(i).size(); j++) {
                        ExpandChildList expandChild = new ExpandChildList();

                        expandChild.setName(listEachProjectsTask.get(i).get(j));
                        expandChild.setID(listEachProjectsTaskID.get(i).get(j));
                        expandChild.setTag(null);
                        expandChildArray.add(expandChild);
                    }
                }
                expandGroup.setItemes(expandChildArray);
                expandGroupArray.add(expandGroup);

            }
        } else {
            Toast.makeText(this.getActivity(), "برای شما پروژه ای موجود نمی باشد .", Toast.LENGTH_LONG).show();
        }
        return expandGroupArray;
    }

}