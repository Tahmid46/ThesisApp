package com.example.tahmid.Medicine_HelpV2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class DosesActivity extends AppCompatActivity {

    private Button myMed,myAppt,myDoctor,myHist;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item4:
                changeSettings();
                return true;
            case R.id.item2:
                showPrivacyPolicyPopup();
                return  true;
            case R.id.item3:
                shareDataIntent();
                return true;
            case R.id.item1:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;
            case R.id.item5:
                clearData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doses);
        myMed=findViewById(R.id.myMedId);
        myAppt=findViewById(R.id.myApptId);
        //myDoctor=findViewById(R.id.myDocId);

        myDatabaseHelper=new MyDatabaseHelper(this);

        //Toolbar
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // myHist=findViewById(R.id.myHistoryId);
        //mm

        myAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DosesActivity.this,MyApp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
      /*  myHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DosesActivity.this,MyHi.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });*/
        myMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DosesActivity.this,MyMe.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
        /*myDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(DosesActivity.this,MyDo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });*/

        //comit
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    private void changeSettings()
    {
        SharedPreferences authPreferences = getSharedPreferences("auth",MODE_PRIVATE);
        SharedPreferences.Editor editor = authPreferences.edit();
        editor.putInt("auth",2);
        editor.apply();

        SharedPreferences authPreferences2 = getSharedPreferences("PREF",0);
        SharedPreferences.Editor editor2 = authPreferences2.edit();
        editor2.putString("password","0");
        editor2.apply();

        Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
        startActivity(intent);
    }

    private void showPrivacyPolicyPopup()
    {
        new AlertDialog.Builder(this)
                .setTitle("Privacy Policy")
                .setMessage(getString(R.string.privacy_policy))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })

                .create().show();
    }

    private void shareDataIntent()
    {
        Intent intent = new Intent(getApplicationContext(),ShareDataActivity.class);
        startActivity(intent);
    }

    private void clearData()
    {

        try{
            Cursor cursor1 = myDatabaseHelper.getHistory();
            Cursor cursor2= myDatabaseHelper.getAppointment();
            //Toast.makeText(this, Integer.toString(cursor1.getCount())+"  "+Integer.toString(cursor2.getCount()), Toast.LENGTH_SHORT).show();

            if(cursor1.getCount()==0 && cursor2.getCount()==0){
                //Toast.makeText(this, "You have no data !", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this)
                        .setMessage("No Data Found")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Clear All Data")
                    .setMessage(getString(R.string.clear_data))
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            try {
                                boolean confirmation = myDatabaseHelper.deleteAll();
                                if (confirmation) {
                                    Toast.makeText(getApplicationContext(), "All your data have been deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


}
