package com.project.udayanga.keepmerelax;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Iterator;

public class ResponseToMessage extends AppCompatActivity {
    EditText message;
    Button send,reset,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_to_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        send=(Button)findViewById(R.id.buttonSend);
        reset=(Button)findViewById(R.id.buttonReset);
        cancel=(Button)findViewById(R.id.buttonCancel);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reset();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cancel();
            }
        });
    }
    public void Reset(){
        message=(EditText)findViewById(R.id.editTextMessage);
        message.setText("");
    }
    /*
    * TODO Get previous number to response message.
    * */
    public void Cancel(){
        finish();
    }
    public void Send(){
        message=(EditText)findViewById(R.id.editTextMessage);
        String MESSAGE=message.getText().toString();
        String number;
        try {
            com.project.udayanga.keepmerelax.DatabaseHelp.GetContact getContact= new com.project.udayanga.keepmerelax.DatabaseHelp.GetContact(this);
            getContact.execute("", "", "", "");
            String s=getContact.get();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            JSONArray lang= (JSONArray) jsonObject.get("products");
            Iterator i = lang.iterator();
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                //System.out.println("language "+ innerObj.get("rating") +" with level " + innerObj.get("contact_number"));
                number = (String) innerObj.get("contact_number");
                sendSMS(number);
                break;
            }
        }catch (Exception ex){
        }
    }
    private void sendSMS(String number) {

        try {
            String message = "The SMS sender ";
            System.out.println("SMS Send to" + number + "\n" + message);
            //SmsManager sms = SmsManager.getDefault();
            //sms.sendTextMessage(number, null, message, null, null);
            //Toast.makeText(MainActivity.this,"Successfully Send", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ResponseToMessage.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
