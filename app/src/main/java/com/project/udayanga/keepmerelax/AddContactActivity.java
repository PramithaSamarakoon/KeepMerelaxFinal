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
import android.widget.Toast;

import com.project.udayanga.keepmerelax.DatabaseHelp.AddUser;

public class AddContactActivity extends AppCompatActivity {
    Button save,view;
    EditText name,number,relation;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        save= (Button)findViewById(R.id.buttonSave);
        view=(Button)findViewById(R.id.buttonView);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setNumStars(5);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {actionBarAction();}
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClick();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewButtonClick();
            }
        });
    }
    public void onSaveButtonClick(){
        name=(EditText)findViewById(R.id.editTextName);
        number=(EditText)findViewById(R.id.editTextNumber);
        relation=(EditText)findViewById(R.id.editTextRelationship);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);

        String NAME=name.getText().toString();
        String NUMBER=number.getText().toString();
        String RELATION=relation.getText().toString();
        String RATE= String.valueOf(ratingBar.getNumStars());

        try{
            new com.project.udayanga.keepmerelax.DatabaseHelp.AddContact(this,1).execute(NAME,NUMBER,RELATION,RATE);
            //new AddContact(this,1).execute(NAME,NUMBER,RELATION,RATE);

        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        //SQLite DAtabase
//        name=(EditText)findViewById(R.id.editTextName);
//        number=(EditText)findViewById(R.id.editTextNumber);
//        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
//
//        String NAME=name.getText().toString(),NUMBER=number.getText().toString();
//        int RATING=ratingBar.getNumStars();
//        try{
//            SQLiteDatabase mydatabase = openOrCreateDatabase("KeepMeRelax",MODE_APPEND,null);
//            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Contact(Name VARCHAR,Number VARCHAR,rate INTEGER);");
//            mydatabase.execSQL("INSERT INTO Contact VALUES('"+NAME+"','"+NUMBER+"','"+RATING+"');");
//
//            Toast toast = Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT);
//            toast.show();
//
//        }
//        catch (Exception ex){
//            System.out.println(ex.getMessage());
//        }

    }
    public void onViewButtonClick(){
        try{
            new com.project.udayanga.keepmerelax.DatabaseHelp.AddContact(this,0).execute("","","","");
            //new AddContact(this,1).execute(NAME,NUMBER,RELATION,RATE);

        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        //SQLite Database
//        SQLiteDatabase mydatabase = openOrCreateDatabase("KeepMeRelax",MODE_APPEND,null);
//        Cursor resultSet = mydatabase.rawQuery("Select * from User",null);
//        resultSet.moveToFirst();
//        String username = resultSet.getString(0);
//        String password = resultSet.getString(1);
        //System.out.println(username+"\n"+password);

    }
    private void actionBarAction(){
        name=(EditText)findViewById(R.id.editTextName);
        number=(EditText)findViewById(R.id.editTextNumber);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        name.setText("");
        number.setText("");
        ratingBar.setNumStars(0);
    }
}
