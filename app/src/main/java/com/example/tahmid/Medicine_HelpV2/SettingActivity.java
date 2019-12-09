package com.example.tahmid.Medicine_HelpV2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button buttonApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        radioGroup=findViewById(R.id.radio_group_Id);
        buttonApply=findViewById(R.id.applyId);


        SharedPreferences preferences= getSharedPreferences("prefs",MODE_PRIVATE);
        boolean b=preferences.getBoolean("prefs",true);

        SharedPreferences authPreferences = getSharedPreferences("auth",MODE_PRIVATE);
        int a = authPreferences.getInt("auth",2);

        if(b){
            showStartDialog();
        }else{

        }

        //Authentication check . if 0 -> Finger Print, if 1 -> Pattern lock

        if(a==0){
            Intent intent = new Intent(getApplicationContext(),FingerPrintActivity.class);
            finish();
            startActivity(intent);
        }
        else if(a == 1){
            Intent intent = new Intent(getApplicationContext(),PatternActivity.class);
            finish();
            startActivity(intent);
        }

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);

                String ss=radioButton.getText().toString().trim();

                if(ss.equals("Finger Print")){

                    SharedPreferences authPreferences = getSharedPreferences("auth",MODE_PRIVATE);
                    SharedPreferences.Editor editor = authPreferences.edit();
                    editor.putInt("auth",0);
                    editor.apply();

                    Toast.makeText(getApplicationContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(),FingerPrintActivity.class);
                    finish();
                    startActivity(intent);
                }
                if(ss.equals("Pattern Lock")){
                    SharedPreferences authPreferences = getSharedPreferences("auth",MODE_PRIVATE);
                    SharedPreferences.Editor editor = authPreferences.edit();
                    editor.putInt("auth",1);
                    editor.apply();

                    Toast.makeText(getApplicationContext(),radioButton.getText(),Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(),PatternActivity.class);
                    finish();
                    startActivity(intent);
                }

            }
        });
    }

    public  void showStartDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("Privacy Policy")
                .setMessage(getString(R.string.privacy_policy))
                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        SharedPreferences preferences=getSharedPreferences("prefs",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putBoolean("prefs",false);
                        editor.apply();
                        //Intent intent=new Intent(getApplicationContext(),EmniKhulsi.class);
                        //startActivity(intent);
                    }
                })
                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .create().show();



    }
}
