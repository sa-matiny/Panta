package com.iust.panta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
