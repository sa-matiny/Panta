package com.iust.panta;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectCard extends FragmentActivity implements
        ActionBar.TabListener {

    private TabsPagerAdapter mAdapter;
    private ViewPager viewPager;
    private ActionBar actionBar;
    private Bundle msg_main;
    private Bundle msg_task;
    private Bundle msg_member;
    private Boolean manager;
    private int projectID;

    private JSONObject pro_info;
    private JSONArray pro_users;
    private JSONArray pro_tasks;
    private Intent intent1;

    private ProgressBar mProgressView;
    private JSONObject data;
    private Boolean flag;
    private int combo;

    // Tab titles
    private String[] tabs = {"معرفی پروژه", "وظایف", "اعضا"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_card);

        // Initilization

        mProgressView = (ProgressBar) findViewById(R.id.PCard_progress);
        viewPager = (ViewPager) findViewById(R.id.pager);


        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(ProjectCard.this));
        }

        flag = true;

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        Intent intent = getIntent();
        projectID = intent.getExtras().getInt("projectId");

        SqliteController controller = new SqliteController(this);
        try {
            data = controller.getMe();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Bundel msg for projectID
        msg_main = new Bundle();
        msg_member = new Bundle();
        msg_task = new Bundle();

        mProgressView.setVisibility(View.VISIBLE);

        RequestParams params = new RequestParams();
        params.put("projectID", projectID);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800/project_all/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                mProgressView.setVisibility(View.GONE);
                try {
                    Log.d("RESPONSE", new String(response));
                    JSONObject s_response = new JSONObject(new String(response));
                    pro_info = s_response.getJSONObject("projectInfo");
                    pro_users = s_response.getJSONArray("project_users");
                    pro_tasks = s_response.getJSONArray("project_tasks");

                    manager = false;
                    Log.d("meeeeeeee", data.getString("username"));
                    if (pro_info.getString("managerUser").equals(data.getString("username"))) {
                        manager = true;
                        invalidateOptionsMenu();
                    }
                    //Tasks
                    msg_task.putBoolean("manager", manager);
                    msg_task.putString("pro_tasks", pro_tasks.toString());

                    //Members
                    msg_member.putString("pro_users", pro_users.toString());

                    //Main
                    msg_main.putString("pro_info", pro_info.toString());


                } catch (JSONException e) {
                    e.printStackTrace();

                    System.out.println("Catch");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                System.out.println("Fail");
                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectCard.this);
                builder.setCancelable(false);
                builder.setMessage("خطا! اتصال به اینترنت با مشکل مواجه است");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }


            @Override
            public void onFinish() {
                System.out.println("Finish");
                mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(mAdapter);
            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();
        recreate_pcard();
    }

    public void recreate_pcard() {
        Log.d("restart", "pcard");
        if (!flag) {
            Intent intent = new Intent(this, ProjectCard.class);
            intent.putExtra("projectId", projectID);
            finish();
            startActivity(intent);
        } else {
            flag = false;
        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        try {
            if (manager) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.project_card, menu);
            }
        }
        catch (Exception e) {

        }

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.action_addtask:
                Intent intent = new Intent(this, AddTask.class);
                intent.putExtra("projectID", projectID);
                intent.putExtra("users",pro_users.toString());
                Log.d("taskusersset",pro_users.toString());
                startActivity(intent);
                return true;

            case R.id.action_addmember:
                AlertDialog.Builder add_member = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                System.out.println(input.getInputType());
                add_member.setMessage("پست الکترونیکی عضو جدید را وارد کنید :");
                add_member.setView(input);
                add_member.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressView.setVisibility(View.VISIBLE);
                        RequestParams params = new RequestParams();
                        params.put("projectID", projectID);
                        params.put("username", input.getText().toString());
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.post("http://104.236.33.128:8800/addMember/", params, new AsyncHttpResponseHandler() {

                            @Override
                            public void onStart() {
                                System.out.println("Start");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                mProgressView.setVisibility(View.GONE);
                                try {
                                    Log.d("RESPONSE", new String(response));
                                    JSONObject s_response = new JSONObject(new String(response));
                                    if (!s_response.getBoolean("successful")) {
                                        if (s_response.getString("error").equals("1")) {
                                            input.setError("قبلا اضافه شده است");
                                        }
                                        if (s_response.getString("error").equals("2")) {
                                            input.setError("پست الکترونیکی موجود نیست");
                                        }
                                        AlertDialog.Builder dlg = new AlertDialog.Builder(ProjectCard.this);
                                        dlg.setCancelable(false);
                                        dlg.setMessage(input.getError());
                                        dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        dlg.create().show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "عضو جدید اضافه شد", Toast.LENGTH_LONG).show();
                                        recreate_pcard();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                                mProgressView.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectCard.this);
                                builder.setCancelable(false);
                                builder.setMessage("خطا! اتصال به اینترنت با مشکل مواجه است");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }

                        });
                    }
                });
                add_member.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                add_member.create().show();
                return true;

            case R.id.action_deletemember:
                if (pro_users.length() == 1) {
                    Toast.makeText(getApplicationContext(), "در حال حاضر برای این پروژه عضوی جز مدیر وجود ندارد", Toast.LENGTH_LONG).show();
                    break;
                }

                AlertDialog.Builder del_member = new AlertDialog.Builder(this);
                final Spinner comboBox = new Spinner(this);
                ArrayList<String> memberArray = new ArrayList<String>();
                final ArrayList<String> member_users = new ArrayList<String>();

                for (int i = 0; i < pro_users.length(); i++) {
                    try {
                        JSONObject member = pro_users.getJSONObject(i);
                        if (pro_info.getString("managerUser").equals(member.getString("username"))) {
                            continue;
                        }
                        member_users.add(member.getString("username"));
                        memberArray.add(member.getString("name") + "\n(" + member.getString("username") + ")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<String> stringArrayAdapter =
                        new ArrayAdapter<String>(
                                this,
                                android.R.layout.simple_spinner_dropdown_item,
                                memberArray);
                comboBox.setAdapter(stringArrayAdapter);
                del_member.setMessage("کدام عضو را می خواهید حذف کنید؟");
                del_member.setView(comboBox);
                final AdapterView.OnItemSelectedListener onSpinner =
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(
                                    AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                                combo = position;
                            }

                            @Override
                            public void onNothingSelected(
                                    AdapterView<?> parent) {
                            }
                        };

                comboBox.setOnItemSelectedListener(onSpinner);
                del_member.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressView.setVisibility(View.VISIBLE);
                        RequestParams params = new RequestParams();
                        params.put("projectID", projectID);
                        params.put("username", member_users.get(combo));
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.post("http://104.236.33.128:8800/deleteMember/", params, new AsyncHttpResponseHandler() {

                            @Override
                            public void onStart() {
                                System.out.println("Start");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                mProgressView.setVisibility(View.GONE);
                                try {
                                    Log.d("RESPONSE", new String(response));
                                    JSONObject s_response = new JSONObject(new String(response));
                                    if (!s_response.getBoolean("successful")) {
                                        AlertDialog.Builder dlg = new AlertDialog.Builder(ProjectCard.this);
                                        dlg.setCancelable(false);
                                        //dlg.setMessage();
                                        dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        dlg.create().show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "عضو حذف شد", Toast.LENGTH_LONG).show();
                                        recreate_pcard();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                                mProgressView.setVisibility(View.GONE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectCard.this);
                                builder.setCancelable(false);
                                builder.setMessage("خطا! اتصال به اینترنت با مشکل مواجه است");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }

                        });


                    }
                });
                del_member.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                del_member.create().show();
                return true;


            case R.id.action_editpro:
                Intent intent2 = new Intent(this, EditProject.class);
                intent2.putExtra("projectInfo", pro_info.toString());
                startActivity(intent2);
                return true;

            case R.id.action_deletepro:
                intent1 = new Intent(this, Home.class);
                final AlertDialog.Builder acc_del = new AlertDialog.Builder(this);
                acc_del.setMessage("می خواهید پروژه را حذف کنید؟");
                acc_del.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestParams params = new RequestParams();
                        params.put("projectID", projectID);
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.post("http://104.236.33.128:8800/deleteProject/", params, new AsyncHttpResponseHandler() {

                            @Override
                            public void onStart() {
                                System.out.println("Start");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                try {
                                    Log.d("RESPONSE", new String(response));
                                    JSONObject s_response = new JSONObject(new String(response));
                                    if (s_response.getBoolean("successful")) {
                                        AlertDialog.Builder dlg = new AlertDialog.Builder(ProjectCard.this);
                                        dlg.setCancelable(false);
                                        dlg.setMessage("پروژه حذف شد!");
                                        dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                finish();
                                                startActivity(intent1);
                                            }
                                        });
                                        dlg.create().show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectCard.this);
                                builder.setCancelable(false);
                                Log.d("error", errorResponse.toString());
                                builder.setMessage("خطا! اتصال به اینترنت با مشکل مواجه است");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });
                    }
                });
                acc_del.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                acc_del.create().show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    Fragment PCardMain = new PCardMainFragment();
                    PCardMain.setArguments(msg_main);
                    return PCardMain;

                case 1:
                    Fragment PCardTask = new PCardTaskFragment();
                    PCardTask.setArguments(msg_task);
                    return PCardTask;

                case 2:
                    Fragment PCardMembers = new PCardMembersFragment();
                    PCardMembers.setArguments(msg_member);
                    return PCardMembers;
            }

            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 3;
        }

    }
}