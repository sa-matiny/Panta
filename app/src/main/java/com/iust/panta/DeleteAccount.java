package com.iust.panta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class DeleteAccount extends Activity {

    private EditText EAccountview;
    private Button ButtonView;
    private ProgressBar ProgressView;
    private Activity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);


        EAccountview = (EditText) findViewById(R.id.EAccount);
        ProgressView = (ProgressBar) findViewById(R.id.delete_account_progress);
        ButtonView = (Button) findViewById(R.id.delete_account_button);

        Intent intent = getIntent();
   /*     try {
            JSONObject info = new JSONObject(intent.getExtras().getString("accountInfo"));
            Log.d("response", info.toString(EAccountview.setText("username"));


        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        EAccountview.setText("ma@yahoo.com");

    }
    public void DeleteAccount(View view) {


        ProgressView.setVisibility(View.VISIBLE);
        ButtonView.setVisibility(View.GONE);
        RequestParams params = new RequestParams();
       // params.put("username", EAccountview.getText().toString());
        params.put("username", "ma@yahoo.com");

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
                ProgressView.setVisibility(View.GONE);
                ButtonView.setVisibility(View.VISIBLE);
                try {
                    Log.d("RESPONSE", new String(response));
                    JSONObject s_response = new JSONObject(new String(response));

                    // JSONArray
                    if (s_response.getBoolean("successful")) {

                        AlertDialog.Builder dlg = new AlertDialog.Builder(DeleteAccount.this);
                        dlg.setCancelable(false);
                        dlg.setMessage("حساب کاربری شما با موفقیت حذف شد");
                        dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Intent intent = new Intent(DeleteAccount.this, Welcome.class);
                                finish();
                                startActivity(intent);

                            }


                        });

                        dlg.create().show();

                    } else {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(DeleteAccount.this);
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


                ProgressView.setVisibility(View.GONE);
                ButtonView.setVisibility(View.VISIBLE);

                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAccount.this);
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

    private Activity getActivity() {return activity;
    }

}

