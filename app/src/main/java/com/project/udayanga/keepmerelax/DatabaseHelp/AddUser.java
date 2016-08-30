package com.project.udayanga.keepmerelax.DatabaseHelp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by udayanga on 8/30/16.
 */
public class AddUser extends AsyncTask<String,Void,String> {
    private TextView statusField,roleField;
    private Context context;
    private int byGetOrPost = 0;

    public AddUser(Context context,TextView statusField,TextView roleField,int flag) {
        this.context = context;
        this.statusField = statusField;
        this.roleField = roleField;
        byGetOrPost = flag;
    }

    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... arg0) {
        if(byGetOrPost == 0){ //means by Get Method

            try{
                String contact_number = (String)arg0[0];
                String dob = (String)arg0[1];
                String email = (String)arg0[2];
                String gender = (String)arg0[3];
                String low = (String)arg0[4];
                String name = (String)arg0[5];
                String password  = (String)arg0[6];
                String peak = (String)arg0[7];
                String link = "http://myphpmysqlweb.hostei.com/login.php?username="+contact_number+"& password="+password;

                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            }

            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        else{
            try{
                String contact_number = (String)arg0[0];
                String dob = (String)arg0[1];
                String email = (String)arg0[2];
                String gender = (String)arg0[3];
                String low = arg0[4];
                String name = (String)arg0[5];
                String password  = (String)arg0[6];
                String peak = arg0[7];

                String link="http://udayanga.me/kmr/add-user.php";
                String data  = URLEncoder.encode("contact_number", "UTF-8") + "=" + URLEncoder.encode(contact_number, "UTF-8");
                data += "&" + URLEncoder.encode("dob", "UTF-8") + "=" + URLEncoder.encode(dob, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
                data += "&" + URLEncoder.encode("low", "UTF-8") + "=" + URLEncoder.encode(low, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                data += "&" + URLEncoder.encode("peak", "UTF-8") + "=" + URLEncoder.encode(peak, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
    }
    @Override
    protected void onPostExecute(String result){
        this.statusField.setText("Login Successful");
        this.roleField.setText(result);
        Toast.makeText(this.context,result,Toast.LENGTH_LONG).show();

    }
}
