package com.project.udayanga.keepmerelax;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandIOException;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;
import com.microsoft.band.sensors.HeartRateConsentListener;
import com.project.udayanga.keepmerelax.DatabaseHelp.AddRate;
import com.project.udayanga.keepmerelax.DatabaseHelp.AddUser;

import android.annotation.TargetApi;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class UnrecognizeActivity extends AppCompatActivity {

    private BandClient client = null;
    private Button btnStart,btnStart2,btnStart3, btnConsent,buttonSaveUn;
    private TextView txtStatus;
    TextToSpeech tts;
    private TextView tv1,tv2,tv3;
    String name,contact_number,email,password,dob,gender;

    private BandHeartRateEventListener mHeartRateEventListener = new BandHeartRateEventListener() {
        @Override
        public void onBandHeartRateChanged(final BandHeartRateEvent event) {
            if (event != null) {
                appendToUI(String.format("Heart Rate = %d beats per minute\n"
                        + "Quality = %s\n", event.getHeartRate(), event.getQuality()));
                String RATE= String.valueOf(event.getHeartRate());
                //new AddRate(UnrecognizeActivity.this,1).execute(RATE);
                setRate(RATE);
            }
        }
    };
    String rate;
    public String getRate() {
        return rate;

    }
    public void setRate(String rate) {
        this.rate = rate;
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_recognize);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAll();
            }
        });


        btnConsent = (Button) findViewById(R.id.btnConsent);

        txtStatus = (TextView) findViewById(R.id.txtStatus);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart2 = (Button) findViewById(R.id.btnStart2);
        btnStart3 = (Button) findViewById(R.id.btnStart3);
        buttonSaveUn=(Button)findViewById(R.id.buttonSaveUn);

        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);

        btnStart.setEnabled(false);
        btnStart2.setEnabled(false);
        btnStart3.setEnabled(false);


        final WeakReference<Activity> reference = new WeakReference<Activity>(this);
        btnConsent.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                new HeartRateConsentTask().execute(reference);
                btnStart.setEnabled(true);

            }
        });

        //Start button action
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("");

                new CountDownTimer(10000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tv1.setText("Seconds Remaining: " + millisUntilFinished / 1000);//Show time remaining
                        new HeartRateSubscriptionTask().execute();

                        String rate="10";//getRate();
                        String method="relax";
                        new AddRate(UnrecognizeActivity.this,1).execute(rate,method);
                    }

                    public void onFinish() {
                        tv1.setText("Done");
                        btnStart2.setEnabled(true);
                        onPause();
                    }
                }.start();

                btnStart.setEnabled(false);

            }
        });
        btnStart2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("");

                new CountDownTimer(10000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tv2.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                        new HeartRateSubscriptionTask().execute();

                        String rate="20";//getRate();
                        String method="walk";
                        new AddRate(UnrecognizeActivity.this,1).execute(rate,method);
                    }

                    public void onFinish() {
                        tv2.setText("Done");
                        btnStart3.setEnabled(true);
                        onPause();
                    }
                }.start();
                btnStart2.setEnabled(false);

            }
        });

        btnStart3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("");

                new CountDownTimer(10000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tv3.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                        new HeartRateSubscriptionTask().execute();

                        String rate="30";//getRate();
                        String method="run";
                        new AddRate(UnrecognizeActivity.this,1).execute(rate,method);
                    }

                    public void onFinish() {
                        tv3.setText("Done");
                        onPause();
                    }
                }.start();
                btnStart3.setEnabled(false);
            }
        });
        buttonSaveUn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calculationMaxMin();
            }
        });

    }
    private void calculationMaxMin(){
        //Get  max

        String max_relax = "",max_walk="",max_run="";
        String min_relax="",min_walk="",min_run="";
        try{
            com.project.udayanga.keepmerelax.DatabaseHelp.GetMax getMax = new com.project.udayanga.keepmerelax.DatabaseHelp.GetMax(this);
            getMax.execute();
            String s=getMax.get();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            JSONArray lang= (JSONArray) jsonObject.get("product");
            Iterator i = lang.iterator();

            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                max_relax=(String)innerObj.get("max_relax");
                max_walk=(String)innerObj.get("max_walk");
                max_run=(String)innerObj.get("max_run");
            }


            Toast.makeText(this,  "MAX"+max_relax+max_walk+max_run,Toast.LENGTH_LONG).show();


        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            System.out.println("Error MAX" + e.getMessage());
        }
        //GET MIN
        try{
            com.project.udayanga.keepmerelax.DatabaseHelp.GetMin getMin = new com.project.udayanga.keepmerelax.DatabaseHelp.GetMin(this);
            getMin.execute();
            String s=getMin.get();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            JSONArray lang= (JSONArray) jsonObject.get("product");
            Iterator i = lang.iterator();

            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                min_relax=(String)innerObj.get("min_relax");
                min_walk=(String)innerObj.get("min_walk");
                min_run=(String)innerObj.get("min_run");
            }


            Toast.makeText(this,  "MIN"+min_relax+min_walk+min_run,Toast.LENGTH_LONG).show();


        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            System.out.println("Error" + e.getMessage());
        }

        try{
            int MA_RELAX,MA_WALK,MA_RUN,MI_RELAX,MI_WALK,MI_RUN;

            MA_RELAX= Integer.parseInt(max_relax);
            MA_WALK= Integer.parseInt(max_walk);
            MA_RUN= Integer.parseInt(max_run);

            MI_RELAX= Integer.parseInt(min_relax);
            MI_WALK= Integer.parseInt(max_walk);
            MI_RUN= Integer.parseInt(min_run);

            double PEAK_RU=((MA_RUN-MI_RUN)*60)+MI_RUN;
            double PEAK_WA=((MA_WALK-MI_WALK)*60)+MI_WALK;
            double PEAK_RE=((MA_RELAX-MI_RELAX)*60)+MI_RELAX;

            double LOW_RU=((MA_RUN-MI_RUN)*70)+MI_RUN;
            double LOW_WA=((MA_WALK-MI_WALK)*70)+MI_WALK;
            double LOW_RE=((MA_RELAX-MI_RELAX)*70)+MI_RELAX;


            double PEAK=(PEAK_RU+PEAK_WA+PEAK_RE)/3;

            double LOW=(LOW_RU+LOW_WA+LOW_RE)/3;
            saveData(PEAK,LOW);

        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG).show();
            System.out.println(ex.getMessage());
        }
    }
    private void saveData(double peak,double low){
        Bundle bundle=getIntent().getExtras();

        name=bundle.getString("name");
        contact_number=bundle.getString("contact_number");
        email=bundle.getString("email");
        password=bundle.getString("password");
        dob=bundle.getString("dob");
        gender=bundle.getString("gender");

        try{
            String LOW= String.valueOf(low);
            String PEAK= String.valueOf(peak);
            new AddUser(this,1).execute(contact_number,dob,email,gender,LOW,name,password,PEAK);

        }
        catch(Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
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


    private class  HeartRateSubscriptionTask extends AsyncTask<Void, Void, Void> {
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
    private void resetAll(){
        new com.project.udayanga.keepmerelax.DatabaseHelp.ResetUnRecognize(this).execute();
        btnStart.setEnabled(true);
        btnStart2.setEnabled(false);
        btnStart3.setEnabled(false);
    }
}
