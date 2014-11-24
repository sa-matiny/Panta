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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class  ProjectCard extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"معرفی پروژه", "وظایف", "اعضا"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_card);

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
                    .setTabListener(this));
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


    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    // Top Rated fragment activity
                    return new PCardMainFragment();
                case 1:
                    // Games fragment activity
                    return new PCardTaskFragment();
                case 2:
                    // Movies fragment activity
                    return new PCardMembersFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 3;
        }

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
                Intent intent = new Intent(this, AddTask.class);
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
                        RequestParams params = new RequestParams();
                        params.put("projectID", 1);
                        params.put("username",input.getText().toString());
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
                                Log.d("error",errorResponse.toString());
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
        }
        return super.onOptionsItemSelected(item);
    }
}

