package com.example.tahmid.Medicine_HelpV2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import android.support.v7.widget.Toolbar;

public class MyHi extends AppCompatActivity {

    ListView lvhistory;
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
        setContentView(R.layout.activity_my_hi);
        lvhistory=findViewById(R.id.lvmyhistory);
        myDatabaseHelper=new MyDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase=myDatabaseHelper.getWritableDatabase();

        //Toolbar
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        display();
    }
    public  void  display(){
        Cursor cursor=myDatabaseHelper.getHistory();
        if(cursor.getCount()==0){
            Toast.makeText(getApplicationContext(),"You don't have any history",Toast.LENGTH_LONG).show();
            return;

        }

        final ArrayList<String> med = new ArrayList<String>();
        final ArrayList<String> doc = new ArrayList<String>();
        final ArrayList<String> stdate = new ArrayList<String>();
        final ArrayList<String> endate = new ArrayList<String>();
        final ArrayList<String> mtime = new ArrayList<String>();
        final ArrayList<String> atime = new ArrayList<String>();
        final ArrayList<String> ntime = new ArrayList<String>();
        final ArrayList<Integer> fromid = new ArrayList<Integer>();
        final ArrayList<Integer> toid=new ArrayList<Integer>();

        while(cursor.moveToNext()){
            med.add(cursor.getString(0));
            doc.add(cursor.getString(1));
            stdate.add(cursor.getString(2));
            endate.add(cursor.getString(3));
            mtime.add(cursor.getString(4));
            atime.add(cursor.getString(5));
            ntime.add(cursor.getString(6));
            fromid.add(cursor.getInt(7));
            toid.add(cursor.getInt(8));
        }


        CustomAdapterHis c = new CustomAdapterHis(MyHi.this,med,doc,stdate,endate,mtime,atime,ntime,fromid,toid);
        lvhistory.setAdapter(c);
        lvhistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int in=i;

                AlertDialog.Builder a_builder=new AlertDialog.Builder(MyHi.this);
                a_builder.setMessage("Do you want to delete this history?\n This will also turn off the alarm!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                int from=fromid.get(in);
                                int to=toid.get(in);
                                for(int j=from;j<=to;j++){
                                    AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                                    Intent intent=new Intent(MyHi.this, Notification_receiver_med.class);
                                    intent.putExtra("CNT",j);
                                    intent.setAction("MY_NOTIFICATION_MESSAGE_");
                                    PendingIntent pendingIntent=PendingIntent.getBroadcast(MyHi.this,j,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                                    alarmManager.cancel(pendingIntent);
                                }
                                int ok=myDatabaseHelper.deletehis(from);
                                Toast.makeText(getApplicationContext(), "History Deleted", Toast.LENGTH_LONG).show();
                                Cursor cursor=myDatabaseHelper.getHistory();
                                if(cursor.getCount()==0){
                                    Toast.makeText(getApplicationContext(),"You don't have any history",Toast.LENGTH_LONG).show();


                                }

                                final ArrayList<String> med = new ArrayList<String>();
                                final ArrayList<String> doc = new ArrayList<String>();
                                final ArrayList<String> stdate = new ArrayList<String>();
                                final ArrayList<String> endate = new ArrayList<String>();
                                final ArrayList<String> mtime = new ArrayList<String>();
                                final ArrayList<String> atime = new ArrayList<String>();
                                final ArrayList<String> ntime = new ArrayList<String>();
                                final ArrayList<Integer> fromid = new ArrayList<Integer>();
                                final ArrayList<Integer> toid=new ArrayList<Integer>();

                                while(cursor.moveToNext()){
                                    med.add(cursor.getString(0));
                                    doc.add(cursor.getString(1));
                                    stdate.add(cursor.getString(2));
                                    endate.add(cursor.getString(3));
                                    mtime.add(cursor.getString(4));
                                    atime.add(cursor.getString(5));
                                    ntime.add(cursor.getString(6));
                                    fromid.add(cursor.getInt(7));
                                    toid.add(cursor.getInt(8));
                                }


                                CustomAdapterHis c = new CustomAdapterHis(MyHi.this,med,doc,stdate,endate,mtime,atime,ntime,fromid,toid);
                                lvhistory.setAdapter(c);

                                //display();

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

                //
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
