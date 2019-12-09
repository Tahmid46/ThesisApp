package com.example.tahmid.Medicine_HelpV2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import android.support.v7.widget.Toolbar;

public class MyApp extends AppCompatActivity {

    ListView lvapp;
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
        setContentView(R.layout.activity_my_app);
        myDatabaseHelper=new MyDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase=myDatabaseHelper.getWritableDatabase();

        lvapp=findViewById(R.id.listV);

        //Toolbar
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        display();
    }
    public  void  display(){
        Cursor cursor=myDatabaseHelper.getAppointment();
        if(cursor.getCount()==0){
            Toast.makeText(getApplicationContext(),"You don't have any appointment",Toast.LENGTH_LONG).show();
            return;

        }
        ArrayList<String> docname = new ArrayList<String>();
        ArrayList<String> apptdate = new ArrayList<String>();
        final ArrayList<String> appid = new ArrayList<String>();
        while(cursor.moveToNext()){
            docname.add(cursor.getString(0));
            apptdate.add(cursor.getString(1));
            appid.add(cursor.getString(2));
        }
        CustomAdapterDoc c = new CustomAdapterDoc(MyApp.this,docname,apptdate);
        lvapp.setAdapter(c);

        lvapp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int in=i;

                AlertDialog.Builder a_builder=new AlertDialog.Builder(MyApp.this);
                a_builder.setMessage("Do you want to turn off the alarm?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String from=appid.get(in);
                                int haha=Integer.parseInt(from);
                                AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                                Intent intent=new Intent(MyApp.this, Notification_receiver.class);
                                intent.putExtra("CNT",from);
                                intent.setAction("MY_NOTIFICATION_MESSAGE");
                                PendingIntent pendingIntent=PendingIntent.getBroadcast(MyApp.this,haha,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.cancel(pendingIntent);
                                myDatabaseHelper.deleteapp(appid.get(in));

                                Cursor cursor=myDatabaseHelper.getAppointment();
                                if(cursor.getCount()==0){
                                    Toast.makeText(getApplicationContext(),"You don't have any appointment",Toast.LENGTH_LONG).show();

                                }
                                ArrayList<String> docname = new ArrayList<String>();
                                ArrayList<String> apptdate = new ArrayList<String>();
                                final ArrayList<String> appid = new ArrayList<String>();
                                while(cursor.moveToNext()){
                                    docname.add(cursor.getString(0));
                                    apptdate.add(cursor.getString(1));
                                    appid.add(cursor.getString(2));
                                }
                                CustomAdapterDoc c = new CustomAdapterDoc(MyApp.this,docname,apptdate);
                                lvapp.setAdapter(c);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                                }
                        });
                AlertDialog alert=a_builder.create();
                alert.setTitle("CANCEL ALARM");
                alert.show();
            }
        });
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


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

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
