package com.project.udayanga.keepmerelax;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class AddContactActivity extends AppCompatActivity {
    Button save,view;
    EditText name,number;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        save= (Button)findViewById(R.id.buttonSave);
        view=(Button)findViewById(R.id.buttonSave);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser();
            }
        });
    }
    public void database(){
        String NAME=name.getText().toString(),NUMBER=number.getText().toString();
        int RATING=ratingBar.getNumStars();
        try{
            SQLiteDatabase mydatabase = openOrCreateDatabase("KeepMeRelax",MODE_APPEND,null);
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Contact(Name VARCHAR,Number VARCHAR,rate INTEGER);");
            mydatabase.execSQL("INSERT INTO User VALUES('"+NAME+"','"+NUMBER+"','"+RATING+"');");

        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
    public void getUser(){
        SQLiteDatabase mydatabase = openOrCreateDatabase("KeepMeRelax",MODE_APPEND,null);
        Cursor resultSet = mydatabase.rawQuery("Select * from User",null);
        resultSet.moveToFirst();
        String username = resultSet.getString(0);
        String password = resultSet.getString(1);
        //System.out.println(username+"\n"+password);

    }
}
