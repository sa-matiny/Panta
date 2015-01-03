package com.iust.panta;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeSettingFragment extends Fragment {


    private View rootView;
    private Button changePassword;
    private Button deleteAccount;
    private Button aboutUs;
    private Button signOut;



    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle saveInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_setting, container, false);


        changePassword = (Button) rootView.findViewById(R.id.changePassword_button);
        changePassword.setOnClickListener(new View.OnClickListener()


        {
            @Override
            public void onClick(View view) {
                boolean has_error = false;


                changePassword( has_error);

            }
        });



        deleteAccount = (Button) rootView.findViewById(R.id.delete_account_button);
        deleteAccount.setOnClickListener(new View.OnClickListener()


        {
            @Override
            public void onClick(View view) {
                boolean has_error = false;


                deleteAccount(has_error);

            }
        });





        signOut = (Button) rootView.findViewById(R.id.signOut_button);
        signOut.setOnClickListener(new View.OnClickListener()


        {
            @Override
            public void onClick(View view) {
                boolean has_error = false;


                signOut(has_error);

            }
        });



        aboutUs = (Button) rootView.findViewById(R.id.aboutUs_button);
        aboutUs.setOnClickListener(new View.OnClickListener()


        {
            @Override
            public void onClick(View view) {
                boolean has_error = false;


                aboutUs(has_error);

            }
        });





        return rootView;
    }





    private void changePassword(boolean view) {
        Intent intent;
        intent = new Intent(getActivity(),ChangePassword.class);
        startActivity(intent);

    }


    public void signOut(boolean view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(false);
                    builder.setMessage("آیا مایل به خروج از حساب کاربری خود هستید؟");
                    builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(getActivity(), Welcome.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().finish();
                            startActivity(intent);

                        }
                    });
                    builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }


    public void aboutUs(boolean view) {

        Intent intent = new Intent(getActivity(), AboutUs.class);
        startActivity(intent);
    }

    public void deleteAccount(boolean view) {

        Intent intent = new Intent(getActivity(), DeleteAccount.class);
        startActivity(intent);
    }

}