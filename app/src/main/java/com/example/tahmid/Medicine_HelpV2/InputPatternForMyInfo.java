package com.example.tahmid.Medicine_HelpV2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class InputPatternForMyInfo extends AppCompatActivity {

    PatternLockView mPatternLockView;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pattern_for_my_info);

        SharedPreferences preferences=getSharedPreferences("PREF",0);
        password=preferences.getString("password","0");


        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view2);

        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                if(password.equals(PatternLockUtils.patternToString(mPatternLockView, pattern))) {

                    Intent intent = new Intent(getApplicationContext(), DosesActivity.class);
                    startActivity(intent);
                    finish();


                }
            }

            @Override
            public void onCleared() {

            }
        });
    }
}
