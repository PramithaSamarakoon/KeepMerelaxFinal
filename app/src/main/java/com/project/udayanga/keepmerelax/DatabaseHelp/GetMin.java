package com.project.udayanga.keepmerelax.DatabaseHelp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

/**
 * Created by U D A Y A N G A on 9/3/2016.
 */
public class GetMin extends AsyncTask<String,Void,String> {


    private Context context;

    public GetMin (Context context) {
        this.context = context;
    }

    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... arg0) {
        try{

            String link = "http://udayanga.me/kmr/get-min.php";

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
    @Override
    protected void onPostExecute(String result){
        //this.statusField.setText("Login Successful");
        //this.roleField.setText(result);
        //responseReturn=result;
        //Toast.makeText(this.context, result, Toast.LENGTH_LONG).show();
        //Toast.makeText(this.context,"User added successfully !",Toast.LENGTH_LONG).show();

    }


}
