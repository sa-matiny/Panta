package com.iust.panta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

/**
 * Created by Rayehe on 11/23/2014.
 */
public class HomeProfileFragment extends Fragment {

    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        bundle=new Bundle();

        bundle=this.getArguments();

        Log.d("GetBundle",bundle.toString());

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_profile, container, false);




        return rootView;
    }
}

/**
 * Back button listener.
 * Will close the application if the back button pressed twice.
 */

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("می خواهید از برنامه خرج شوید؟");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}*/



