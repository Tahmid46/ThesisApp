package com.example.tahmid.Medicine_HelpV2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class ShareDataActivity extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;
    private Button sendBtn;
    private TextView tv;
    private EditText emailTxt1,emailTxt2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_data);

        sendBtn = findViewById(R.id.btnSend);
        tv = findViewById(R.id.sampleTv);
        emailTxt1 = findViewById(R.id.recId);
        emailTxt2 = findViewById(R.id.recId2);

        myDatabaseHelper=new MyDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase=myDatabaseHelper.getWritableDatabase();


            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {


                        String data = getAllData();

                        if (!data.isEmpty()) {
                            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                            if (!cd.isConnected()) {
                                Toast.makeText(getApplicationContext(), "Internet/wifi connection is unavailable", Toast.LENGTH_SHORT).show();
                            } else {
                                sendEmail(data);
                            }

                        }
                    }catch (Exception e){
                        //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            });

        }


    private String getAllData()
    {
        Cursor cursor = myDatabaseHelper.getHistory();
        Cursor cursor1 = myDatabaseHelper.getAppointment();
        if (cursor.getCount() == 0 && cursor1.getCount()==0) {
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_LONG).show();
            return null;

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

            try{

                    med.add(cursor.getString(0));
                    doc.add(cursor.getString(1));
                    stdate.add(cursor.getString(2));
                    endate.add(cursor.getString(3));
                    mtime.add(cursor.getString(4));
                    atime.add(cursor.getString(5));
                    ntime.add(cursor.getString(6));
                    fromid.add(cursor.getInt(7));
                    toid.add(cursor.getInt(8));

            }catch(Exception e){
                e.printStackTrace();
            }

        }


        ArrayList<String> docname = new ArrayList<String>();
        ArrayList<String> apptdate = new ArrayList<String>();

        final ArrayList<String> appid = new ArrayList<String>();
        while(cursor1.moveToNext()){
            docname.add(cursor1.getString(0));
            apptdate.add(cursor1.getString(1));
            appid.add(cursor1.getString(2));
        }

        StringBuilder sb=new StringBuilder();
        sb.append(med+"\n");
        sb.append(doc+"\n");
        sb.append(stdate+"\n");
        sb.append(endate+"\n");
        sb.append(mtime+"\n");
        sb.append(atime+"\n");
        sb.append(ntime+"\n");
        sb.append(fromid+"\n");
        sb.append(toid+"\n");

        sb.append(docname+"\n");
        sb.append(appid+"\n");

        //tv.setText(sb);
        return sb.toString();
    }

    public void sendEmail(String message)
    {
        try{
            String [] recepient=new String[2];
            recepient[0]=emailTxt1.getText().toString().trim();
            recepient[1]=emailTxt2.getText().toString().trim();
            String subject = "My Health Data";

            if(recepient[0].isEmpty()){
                Toast.makeText(getApplicationContext(),"Please enter email address",Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL,recepient);
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,message);

            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent,"Choose an email client"));
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
}
