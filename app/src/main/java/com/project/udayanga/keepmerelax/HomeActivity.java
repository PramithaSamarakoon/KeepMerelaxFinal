package com.project.udayanga.keepmerelax;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Iterator;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        String count= isRegistered();
        /*Disable button  when
        * User alredy created,
        *   Register button will disable
        * User not added
        *   Log in button will disable
        * */

        Button buttonCreate=(Button)findViewById(R.id.buttonCreateAccount);
        Button buttonLogin=(Button)findViewById(R.id.buttonLogin);
        buttonCreate.setEnabled(false);
        buttonLogin.setEnabled(false);
        if(Objects.equals(count, "0")){
            buttonCreate.setEnabled(true);
            buttonLogin.setEnabled(false);
        }
        else if(Objects.equals(count, "1")){
            buttonCreate.setEnabled(false);
            buttonLogin.setEnabled(true);
        }
        else {Toast.makeText(this,count,Toast.LENGTH_LONG).show();}

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(HomeActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
    private String isRegistered(){
        String responseReturn = null;
        try{
            com.project.udayanga.keepmerelax.DatabaseHelp.GetUser getUser= new com.project.udayanga.keepmerelax.DatabaseHelp.GetUser(this,responseReturn,1);
            getUser.execute();
            String s=getUser.get();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            JSONArray lang= (JSONArray) jsonObject.get("product");

            Iterator i = lang.iterator();
            String number = "";
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                number= (String) innerObj.get("cou");
            }
            return number;

        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            System.out.println("Error" + e.getMessage());
            return e.getMessage();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
