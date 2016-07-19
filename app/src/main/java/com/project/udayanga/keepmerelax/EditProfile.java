package com.project.udayanga.keepmerelax;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final EditText name=(EditText)findViewById(R.id.editText);
        final EditText pass=(EditText)findViewById(R.id.editText2);
        Button button=(Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SQLiteDatabase mydatabase = openOrCreateDatabase("KeepMeRelax",MODE_PRIVATE,null);
                    Cursor resultSet = mydatabase.rawQuery("Select * from user",null);
                    resultSet.moveToFirst();
                    String USERNAME = resultSet.getString(0);
                    String PASSWORD = resultSet.getString(1);

                    name.setText(USERNAME);
                    pass.setText(PASSWORD);

                }
                catch (Exception ex){
                    System.out.println(ex.getMessage());
                }

            }
        });
    }
}
