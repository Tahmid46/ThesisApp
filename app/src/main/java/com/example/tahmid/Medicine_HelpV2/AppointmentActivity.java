package com.example.tahmid.Medicine_HelpV2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import android.support.v7.widget.Toolbar;



public class AppointmentActivity extends AppCompatActivity {

    private EditText etdoc;
   private Button btnsave;
   DatePickerDialog datePickerDialog;
   String selectedDate,Msg="Please select a date!";
   CalendarView cvappt;
    private PendingIntent pendingIntent;
    MyDatabaseHelper myDatabaseHelper;

    private  int day,mmonth,yyear,count=1000;

    private ProgressDialog  progressDialog;

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

        myDatabaseHelper=new MyDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase=myDatabaseHelper.getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        progressDialog=new ProgressDialog(this);
        btnsave=findViewById(R.id.butsave);
        cvappt=findViewById(R.id.cvappointmen);
        etdoc=findViewById(R.id.editText4);

        //Toolbar
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //buib

        cvappt.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                day=dayOfMonth;
                yyear=year;
                mmonth=month;

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //alarmMethod();

                String doc = etdoc.getText().toString().trim();
                if (doc.length() < 15) {

                    selectedDate = day + " / " + (mmonth + 1) + " / " + yyear;
                    Msg = "MAppointment saved! \nDate is : " + day + " / " + (mmonth + 1) + " / " + yyear;
                    if (doc.equals(null) || doc.equals("")) {
                        Toast.makeText(getApplicationContext(), "Doctor's name is empty", Toast.LENGTH_LONG).show();
                    } else {
                        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(AppointmentActivity.this);
                        String ppp = s.getString("val", "");
                        long rowID = myDatabaseHelper.inserintoAppointment(doc, selectedDate, ppp);
                        if (rowID == -1) {
                            Toast.makeText(getApplicationContext(), "Row not inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Row successfully inserted", Toast.LENGTH_LONG).show();
                        }

                        count = Integer.parseInt(ppp);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        calendar.set(Calendar.MONTH, mmonth);
                        calendar.set(Calendar.YEAR, yyear);
                        Intent intent = new Intent(AppointmentActivity.this, Notification_receiver.class);
                        intent.putExtra("DOC", doc);
                        intent.putExtra("CNT", ppp);
                        intent.setAction("MY_NOTIFICATION_MESSAGE");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), count, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_LONG).show();
                        count++;
                        s.edit().putString("val", count + "").commit();
                    }


                }
                else{
                    Toast.makeText(getApplicationContext(), "Doctor's name is too long", Toast.LENGTH_SHORT).show();
                }
            }
        });



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
