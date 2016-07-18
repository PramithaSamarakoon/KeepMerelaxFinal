package com.project.udayanga.keepmerelax;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RecognizeActivity extends AppCompatActivity {

    String name,password,dob,gender;
    EditText low,peak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = (Button)findViewById(R.id.buttonSaveRecognize);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
                Intent intent = new Intent(RecognizeActivity.this, AddContactActivity.class);
                startActivity(intent);

            }
        });
    }
    private void saveUser(){
        Bundle bundle=getIntent().getExtras();
        name=bundle.getString("name");
        password=bundle.getString("password");
        dob=bundle.getString("dob");
        gender=bundle.getString("gender");
        low=(EditText)findViewById(R.id.editTextLow);
        peak=(EditText)findViewById(R.id.editTextPeak);

        String NAME=name,PASS=password,DOB=dob,GENDER=gender;
        int LOW= Integer.valueOf(low.getText().toString());
        int PEAK=Integer.valueOf(peak.getText().toString());

        try{
            SQLiteDatabase mydatabase = openOrCreateDatabase("KeepMeRelax",MODE_PRIVATE,null);
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS user(Name VARCHAR,Password VARCHAR,dob VARCHAR,gender VARCHAR,low INTEGER, peak INTEGER);");
            mydatabase.execSQL("INSERT INTO user VALUES('"+NAME+"','"+PASS+"','"+DOB+"','"+GENDER+"','"+LOW+"','"+PEAK+"');");
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
