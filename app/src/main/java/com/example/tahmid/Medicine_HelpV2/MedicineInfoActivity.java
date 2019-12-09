package com.example.tahmid.Medicine_HelpV2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import android.support.v7.widget.Toolbar;

public class MedicineInfoActivity extends AppCompatActivity {

    //private CardView photoSearch;
    private Button searchButton;
    private AutoCompleteTextView searchText;
    //private ImageButton pht;
    private TextInputLayout med_name;
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
        setContentView(R.layout.activity_medicine_info);

        //photoSearch=findViewById(R.id.photoId);
        searchButton=findViewById(R.id.button);
        searchText=findViewById(R.id.editText3);//////----------------
        //pht=findViewById(R.id.button2);
        med_name=findViewById(R.id.medicine_name);

        myDatabaseHelper = new MyDatabaseHelper(this);
        //Toolbar
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseAccess meddata=DatabaseAccess.getInstance(getApplicationContext());
        meddata.open();

        ArrayList<String> medlist= new ArrayList<String>();
        medlist=meddata.getAllmed();
        String array[] = new String[medlist.size()];
        for(int j =0;j<medlist.size();j++){
            array[j] = medlist.get(j);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);
        searchText.setAdapter(adapter);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();

                String name=searchText.getText().toString();//---------------------------
                name=name.toLowerCase();
                //String res=databaseAccess.getInfo(name);
                ArrayList<String> res=databaseAccess.getInfo(name);

                //String mednameintput=med_name.getEditText().getText().toString().trim();
               /* if (mednameintput.isEmpty()){
                    med_name.setError("Field can't be empty");
                    //return false;
                }
                else{
                    med_name.setError(null);
                    //return true;
                }*/

                if(res.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Sorry! No information found",Toast.LENGTH_SHORT).show();
                    databaseAccess.close();
                }else{

                    databaseAccess.close();

                    Intent intent=new Intent(MedicineInfoActivity.this,ShowInfo.class);
                    intent.putStringArrayListExtra("details",res);
                    startActivity(intent);
                }
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
