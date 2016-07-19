package com.project.udayanga.keepmerelax;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditProfile extends AppCompatActivity {

    EditText name,pass,peak,low;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name=(EditText)findViewById(R.id.editUpdateName);
        pass=(EditText)findViewById(R.id.editUpdateDOB);
        peak=(EditText)findViewById(R.id.editUpdatePeak);
        low=(EditText)findViewById(R.id.editUpdateLow);

        Button buttonEdit=(Button)findViewById(R.id.buttonEdit);

        getDatabaseValue();
        name.setEnabled(false);
        pass.setEnabled(false);
        peak.setEnabled(false);
        low.setEnabled(false);
        
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setEnabled(true);
                pass.setEnabled(true);
                peak.setEnabled(true);
                low.setEnabled(true);
            }
        });
    }
    private void getDatabaseValue(){
        try {
            SQLiteDatabase mydatabase = openOrCreateDatabase("KeepMeRelax",MODE_PRIVATE,null);
            Cursor resultSet = mydatabase.rawQuery("Select * from user",null);
            resultSet.moveToFirst();
            String USERNAME = resultSet.getString(0);
            String PASSWORD = resultSet.getString(2);
            String PEAK=resultSet.getString(4);
            String LOW=resultSet.getString(5);

            name.setText(USERNAME);
            pass.setText(PASSWORD);
            peak.setText(PEAK);
            low.setText(LOW);

        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
