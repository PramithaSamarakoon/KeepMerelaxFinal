package com.project.udayanga.keepmerelax;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {
    NotificationManager manager;
    Notification myNotication;
    private BandClient client = null;
    private Button btnStart, btnConsent;
    private TextView txtStatus;
    TextToSpeech tts;

    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private Boolean flag = false;
    static final String HIGH_HEART = " Your heart rate has reached a critical state. A message will be send to the necessary parties ";
    static final String LOW_HEART = "Your heart rate has reached a critical state. A message will be send to the necessary parties ";

    String low,peak;

    private BandHeartRateEventListener mHeartRateEventListener = new BandHeartRateEventListener() {
        @Override
        public void onBandHeartRateChanged(final BandHeartRateEvent event) {
            int LOW,PEAK;
            String S_LOW=getLow();
            String S_PEAK=getPeak();

            LOW= Integer.parseInt(S_LOW);
            PEAK= Integer.parseInt(S_PEAK);
                    /*
                    * TODO check get actual PEAK value and LOW value
                    * */

            if (event != null) {
                appendToUI(String.format("Heart Rate = %d beats per minute\n"
                        + "Quality = %s\n", event.getHeartRate(), event.getQuality()));
                //speakText(HIGH_HEART);

            }
            if (PEAK < event.getHeartRate()) {
                try {
                    speakText(HIGH_HEART);
                    sendAlert();
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (LOW > event.getHeartRate()) {
                speakText(LOW_HEART);
            }
        }
    };

    private void speakText(String text) {
        if (!tts.isSpeaking() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(text);
        } else if (!tts.isSpeaking()) {
            ttsUnder20(text);
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        flag = isGPSOn();
        if (flag) {
            locationListener = new MyLocationListener();
            locationMangaer.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10, locationListener);

        }else {
            gpsAlert();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditProfile.class);
                startActivity(intent);
            }
        });
        getPeakLow();

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("");
                new HeartRateSubscriptionTask().execute();
            }
        });

        final WeakReference<Activity> reference = new WeakReference<Activity>(this);

        btnConsent = (Button) findViewById(R.id.btnConsent);
        btnConsent.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                new HeartRateConsentTask().execute(reference);
            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });


    }

    private Boolean isGPSOn() {
       ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    protected void gpsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("Gps Status")
                .setPositiveButton("Turn On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private class MyLocationListener implements LocationListener {
        String s="";
        @Override
        public void onLocationChanged(Location loc) {
            String longitude = "Longitude: " +loc.getLongitude();
            String latitude = "Latitude: " +loc.getLatitude();

    //City-Name
            String cityName=null;
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName=addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            TextView lat=(TextView)findViewById(R.id.lat);
            TextView lng=(TextView)findViewById(R.id.lng);
            TextView location=(TextView)findViewById(R.id.loc);

            lat.setText(latitude);
            lng.setText(longitude);
            location.setText("City Name: "+cityName);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this,"GPS Disabled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this,"GPS Enabled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            Toast.makeText(MainActivity.this,"GPS State Changed",Toast.LENGTH_SHORT).show();
        }
    }

    public String getLow() {
        return low;
    }

    public String getPeak() {
        return peak;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public void setPeak(String peak) {
        this.peak = peak;
    }

    private void getPeakLow(){
        String LOW = null,PEAK = null;
        try{
            com.project.udayanga.keepmerelax.DatabaseHelp.GetLowHigh getMax = new com.project.udayanga.keepmerelax.DatabaseHelp.GetLowHigh(this);
            getMax.execute();
            String s=getMax.get();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            JSONArray lang= (JSONArray) jsonObject.get("product");
            Iterator i = lang.iterator();

            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                LOW=(String)innerObj.get("low");
                PEAK=(String)innerObj.get("peak");
            }

            setLow(LOW);setPeak(PEAK);
            Toast.makeText(this,  LOW +"\n"+PEAK,Toast.LENGTH_LONG).show();


        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            System.out.println("Error MAX" + e.getMessage());
        }
    }
    private void sendSMS(String number){

        try{
            TextView lat=(TextView)findViewById(R.id.lat);
            TextView lng=(TextView)findViewById(R.id.lng);
            TextView location=(TextView)findViewById(R.id.loc);

            String LAT=lat.getText().toString();
            String LNG=lng.getText().toString();
            String LOCATION= location.getText().toString();


            String message="The SMS sender is in a critical state.Please contact immediately. His current location is  " +LAT+ " " + ""+LNG+" " +LOCATION;
            System.out.println("SMS Send to" + number + "\n" + message);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(number, null, message, null, null);
            Toast.makeText(MainActivity.this,"Successfully Send", Toast.LENGTH_SHORT).show();
            Notification(number);
        }
        catch(Exception e){
            Toast.makeText(MainActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }
    private void Notification(String number){

        /*
        * TODO create proper notification
        * */

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        try{

            Intent intent = new Intent(MainActivity.this,ResponseToMessage.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, 0);
            Notification.Builder builder = new Notification.Builder(MainActivity.this);


            intent.putExtra("number", number);
            builder.setAutoCancel(false);
            builder.setTicker("this is ticker text");
            builder.setContentTitle("Keep Me Relax Alert");
            builder.setContentText("The text message is sent to "+ number );
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(true);
            builder.setSubText("Tap here to make a response to message");   //API level 16
            builder.setNumber(1);
            builder.build();

            myNotication = builder.getNotification();
            manager.notify(11, myNotication);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }    }
    private void sendAlert(){
        try{
            String number,location;double rating;
            String state;
            TextView loc=(TextView)findViewById(R.id.loc);
            String LOC=loc.getText().toString();
            com.project.udayanga.keepmerelax.DatabaseHelp.GetContact getContact= new com.project.udayanga.keepmerelax.DatabaseHelp.GetContact(this);
            getContact.execute(LOC, "", "", "");
            String s=getContact.get();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            JSONArray lang= (JSONArray) jsonObject.get("products");
            Iterator i = lang.iterator();
            //int[] rating = new int[lang.size()];

            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                //System.out.println("language "+ innerObj.get("rating") +" with level " + innerObj.get("contact_number"));
                number= (String) innerObj.get("contact_number");
                //location=(String) innerObj.get("loc");
                //state=(String)lang.get(Integer.parseInt("success"));
                //if(location==LOC){
                    sendSMS(number);
                //}else if(state=="0"){
                //    sendSMS();
                //}

                //Thread.sleep(10000);
                break;
                //Time delay to send message. First message will send to a person who have most valued rating.
            }
        }
        catch(Exception e){
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtStatus.setText("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (client != null) {
            try {
                client.getSensorManager().unregisterHeartRateEventListener(mHeartRateEventListener);
            } catch (BandIOException e) {
                appendToUI(e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (client != null) {
            try {
                client.disconnect().await();
            } catch (InterruptedException e) {
                // Do nothing as this is happening during destroy
            } catch (BandException e) {
                // Do nothing as this is happening during destroy
            }
        }
        super.onDestroy();
    }

    private class HeartRateSubscriptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                        client.getSensorManager().registerHeartRateEventListener(mHeartRateEventListener);
                    } else {
                        appendToUI("You have not given this application consent to access heart rate data yet."
                                + " Please press the Heart Rate Consent button.\n");
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }
    private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
        @Override
        protected Void doInBackground(WeakReference<Activity>... params) {
            try {
                if (getConnectedBandClient()) {

                    if (params[0].get() != null) {
                        client.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
                            @Override
                            public void userAccepted(boolean consentGiven) {
                            }
                        });
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }
    private void appendToUI(final String string) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText(string);
            }
        });
    }

    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.\n");
                return false;
            }
            client = BandClientManager.getInstance().create(getBaseContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        appendToUI("Band is connecting...\n");
        return ConnectionState.CONNECTED == client.connect().await();
    }
}
