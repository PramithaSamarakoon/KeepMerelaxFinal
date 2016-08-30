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

public class CreateAccountActivity extends AppCompatActivity {
    EditText name,pass,conPass;
    RadioButton male,female;
    DatePicker datePicker;
    String gender;
    RadioGroup radioGroup;
    Button recognize,unrecognize;
    //String date= String.valueOf(datePicker.getDayOfMonth()+datePicker.getMonth()+datePicker.getYear());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText)findViewById(R.id.editTextName);
        pass=(EditText)findViewById(R.id.editTextPass);
        conPass=(EditText)findViewById(R.id.editTextConfirmPass);
        male=(RadioButton)findViewById(R.id.radioButtonMale);
        female=(RadioButton)findViewById(R.id.radioButtonFemale);
        //radioGroup =(RadioGroup)findViewById(R.id.)

        //datePicker =(DatePicker)findViewById(R.id.datePicker);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recognize=(Button)findViewById(R.id.buttonRecognize);
        unrecognize=(Button)findViewById(R.id.buttonUnRecognize);



        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(pass.getText()==conPass.getText()){
                    Intent intent= new Intent(CreateAccountActivity.this, RecognizeActivity.class);

                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("password",pass.getText().toString());
                    intent.putExtra("dob","19910305");
                    intent.putExtra("gender","Male");
                    startActivity(intent);
            //   }
            }
        });
        unrecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CreateAccountActivity.this,UnrecognizeActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void RecognizeButtonClick(){





    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?

    }
}
