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
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectCard extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private Bundle msg_main;
    private Bundle msg_task;
    private Bundle msg_member;
    private Boolean manager;
    private int projectID;

    private JSONObject pro_info;

    // Tab titles
    private String[] tabs = {"معرفی پروژه", "وظایف", "اعضا"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        projectID = intent.getExtras().getInt("projectId");

        final SqliteController controller = new SqliteController(this);


        //Bundel msg for projectID
        msg_main = new Bundle();
        msg_member = new Bundle();
        msg_task = new Bundle();
        msg_main.putInt("projectID", projectID);
        msg_member.putInt("projectID", projectID);
        msg_task.putInt("projectID", projectID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_card);


        RequestParams params = new RequestParams();
        params.put("projectID", projectID);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://104.236.33.128:8800/projectInfo/", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                System.out.println("Start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    Log.d("RESPONSE", new String(response));
                    JSONObject s_response = new JSONObject(new String(response));
                    pro_info = s_response.getJSONObject("projectInfo");

                    manager = new Boolean(false);
                    // TODO : set username
                    JSONObject data = controller.getMe();
                    Log.d("meeeeeeee", data.getString("username"));
                    if (pro_info.getString("managerUser").equals(data.getString("username"))) {
                        manager = true;

                    }
                    msg_task.putBoolean("manager", manager);

                    //Main
                    msg_main.putByteArray("response", response);

                    /*//Task
                    msg_task.putString("managerUser", pro_info.getString("managerUser"));
                    msg_task.putString("managerName", pro_info.getString("managerName"));
                    //Member
                    msg_member.putString("managerUser", pro_info.getString("managerUser"));
                    msg_member.putString("managerName", pro_info.getString("managerName"));*/

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
                // Completed the request (either success or failure)


                Log.d("22", "22");
                // Initilization
                viewPager = (ViewPager) findViewById(R.id.pager);
                actionBar = getActionBar();
                mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

                viewPager.setAdapter(mAdapter);
                actionBar.setHomeButtonEnabled(false);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


                // Adding Tabs
                for (String tab_name : tabs) {
                    actionBar.addTab(actionBar.newTab().setText(tab_name)
                            .setTabListener(ProjectCard.this));
                }

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
            }

        });

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_card, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.action_addtask:
                Log.d("me", manager.toString());
                if (!manager) {
                    Toast.makeText(getApplicationContext(), "تنها مدیر پروژه می تواند وظیفه اضافه کند", Toast.LENGTH_LONG).show();
                    break;
                }
                Intent intent = new Intent(this, AddTask.class);
                intent.putExtra("projectID", projectID);
                startActivity(intent);
                return true;

            case R.id.action_addmember:
                if (!manager) {
                    Toast.makeText(getApplicationContext(), "تنها مدیر پروژه می تواند عضو اضافه کند", Toast.LENGTH_LONG).show();
                    break;
                }
                AlertDialog.Builder add_member = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                System.out.println(input.getInputType());
                add_member.setMessage("پست الکترونیکی عضو جدید را وارد کنید :");
                add_member.setView(input);
                add_member.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                add_member.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                add_member.create().show();
                return true;

            case R.id.action_editpro:

                Intent intent2 = new Intent(this, EditProject.class);
                intent2.putExtra("projectInfo", pro_info.toString());
                startActivity(intent2);
                return true;

            case R.id.action_deletepro:
                if (!manager) {
                    Toast.makeText(getApplicationContext(), "تنها مدیر پروژه می تواند عضو اضافه کند", Toast.LENGTH_LONG).show();
                    break;
                }
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

