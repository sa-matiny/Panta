package com.iust.panta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;

import org.json.JSONException;


public class Home extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    SqliteController controller = new SqliteController(this);
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Bundle msg;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        msg = new Bundle();
        String userName;

        try {
            userName = controller.getMe().getString("username");
            msg.putString("username", userName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment objfrag = null;

        switch (position) {
            case 0:
                mTitle = getString(R.string.Home_section);
                objfrag = new HomeProfileFragment();
                objfrag.setArguments(msg);
                break;
            case 1:
                mTitle = getString(R.string.add_section);
                objfrag = new HomeAddProjectFragment();
                objfrag.setArguments(msg);
                break;
            case 2:
                mTitle = getString(R.string.Setting_section);
                objfrag = new HomeSettingFragment();
                break;
            case 3:
                mTitle = getString(R.string.feedback_section);
                objfrag = new HomeFeedbackFragment();
                break;

            default:
                mTitle = getString(R.string.Home_section);
                objfrag = new HomeProfileFragment();
                objfrag.setArguments(msg);
                break;

        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objfrag)
                .commit();
    }


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("می خواهید از برنامه خارج شوید؟");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                controller.deleteMe();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }
}
