package com.iust.panta;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeSettingFragment extends Fragment {



    private View rootView;
    private Button changePassword;
    private Button deleteAccount;
    private Button aboutUs;
    private Button signOut;
    private SqliteController controller;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home_setting, container, false);

        controller = new SqliteController(getActivity());


        changePassword = (Button) rootView.findViewById(R.id.changePassword_button);
        changePassword.setOnClickListener(new View.OnClickListener()


        {
            @Override
            public void onClick(View view) {
                boolean has_error = false;


                changePassword(has_error);

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
        intent = new Intent(getActivity(), ChangePassword.class);
        startActivity(intent);

    }


    public void signOut(boolean view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage("آیا مایل به خروج از حساب کاربری خود هستید؟");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                controller.deleteMe();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage("یا مایل به حذف حساب کاربری خود هستید؟");
        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestParams params = new RequestParams();
                try {
                    params.put("username",controller.getMe().getString("username"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsyncHttpClient client = new AsyncHttpClient();
                client.post("http://104.236.33.128:8800//deleteAccount/", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {

                        // called before request is started
                        Log.d("STARTED", "STARTED");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        Log.d("SUCCESS", "SUCCESS");
                        try {
                            Log.d("RESPONSE", new String(response));
                            JSONObject s_response = new JSONObject(new String(response));

                            // JSONArray
                            if (s_response.getBoolean("successful")) {

                                controller.deleteMe();
                                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                                dlg.setCancelable(false);
                                dlg.setMessage("حساب کاربری شما با موفقیت حذف شد");
                                dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        Intent intent = new Intent(getActivity(), Welcome.class);
                                        getActivity().finish();
                                        startActivity(intent);

                                    }


                                });

                                dlg.create().show();

                            } else {
                                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                                dlg.setCancelable(false);
                                dlg.setMessage("خطا!");
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


                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(false);
                        builder.setMessage("خطا! دوباره امتحان کنید");
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
        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}