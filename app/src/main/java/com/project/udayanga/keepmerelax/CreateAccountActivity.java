package com.project.udayanga.keepmerelax;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {
    EditText name,contact_number,email,pass,conPass,date,month,year;
    RadioButton male,female;
    String gender;
    RadioGroup radioGroup;
    //String date= String.valueOf(datePicker.getDayOfMonth()+datePicker.getMonth()+datePicker.getYear());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
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
    }
    public void onRecognizeButtonClick(View view){

        name = (EditText)findViewById(R.id.editTextName);
        contact_number=(EditText)findViewById(R.id.editTextContactNumber);
        email=(EditText)findViewById(R.id.editTextEmail);
        pass=(EditText)findViewById(R.id.editTextPass);
        conPass=(EditText)findViewById(R.id.editTextConfirmPass);
        date=(EditText)findViewById(R.id.editTextDate);
        month=(EditText)findViewById(R.id.editTextMonth);
        year=(EditText)findViewById(R.id.editTextYear);
        male=(RadioButton)findViewById(R.id.radioButtonMale);
        female=(RadioButton)findViewById(R.id.radioButtonFemale);
        radioGroup=(RadioGroup)findViewById(R.id.radioButtonGroup);

        String dob=date.getText().toString()+"-"+month.getText().toString()+"-"+year.getText().toString();

        Intent intent= new Intent(CreateAccountActivity.this, RecognizeActivity.class);
        if(pass.getText().toString()!=conPass.getText().toString()){
            Toast.makeText(this,"Password and confirmed password should equal !",Toast.LENGTH_LONG).show();
        }
        else {
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("contact_number",contact_number.getText().toString());
            intent.putExtra("email",email.getText().toString());
            intent.putExtra("password",pass.getText().toString());
            intent.putExtra("dob",dob);
            intent.putExtra("gender", "Male");
            startActivity(intent);
        }
    }
    public void onUnRecognizeButtonClick(View view){
        Intent intent= new Intent(CreateAccountActivity.this,UnrecognizeActivity.class);
        startActivity(intent);
    }

}
