package com.iust.panta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;


public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SqliteController controller = new SqliteController(this);
        try {
            if(!controller.getMe().toString().equals("{}")) {
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void signup(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void signin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
