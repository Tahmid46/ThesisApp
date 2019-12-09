package com.example.tahmid.Medicine_HelpV2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PatternActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences=getSharedPreferences("PREF",0);
                String password = preferences.getString("password","0");

                if(password.equals("0")){
                    Intent intent = new Intent(getApplicationContext(),CreatePasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),InputPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },1000);
    }
}
