package com.waqarahmed.android.vtssystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class safety_activity extends AppCompatActivity {

    private static final String LOGTAG = "LOGTAG";
    public static final String GENRE = "GENRE";
    public static final String MAX = "MAX";
    public static final String VARIABLE = "VARIABLE";
    TextView t_engine_rpm;
    TextView t_vehicle_speed;
    TextView t_temperature;
    TextView t_throttle_pos_level;
    TextView update;

    BroadcastReceiver receiver=null;
    IntentFilter filter;
    static String Sp_num;
    String json_string;
    JSONArray json_Array;
    String smsContent="";

    String engine_rpm;
    String vehicle_speed;
    String temperature;
    String throttle_level;

    String time;
    String date;



    Data_source data_source;
    safety_model safety_model_obj;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_activity);

        data_source=new Data_source(this);
        data_source.open();

        t_engine_rpm= (TextView) findViewById(R.id.engine_rpm_val);
        t_vehicle_speed= (TextView) findViewById(R.id.vehicle_speed_val);
        t_temperature= (TextView) findViewById(R.id.temperature_val);
        t_throttle_pos_level= (TextView) findViewById(R.id.throttle_level_val);
        update= (TextView) findViewById(R.id.update);

        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arr0, Intent arr1) {
                Log.d(LOGTAG,"RECEIVEd");

                try {
                    processReceiver(arr0,arr1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
        registerReceiver(receiver,filter);
    }


    public void processReceiver(Context context, Intent intent) throws JSONException {
        Toast.makeText(context,"Data Received",Toast.LENGTH_LONG).show();

        Bundle bundle = intent.getExtras();
        Object[] objArr  = (Object[]) bundle.get("pdus");
        smsContent="";

        for(int i=0; i<objArr.length; i++){
            SmsMessage smsMsg = SmsMessage.createFromPdu((byte[])objArr[i]);
            String smsBody = smsMsg.getMessageBody();
            String senderNumber = smsMsg.getDisplayOriginatingAddress();
            smsContent +=smsBody;
            Sp_num = senderNumber;
        }
        //if(Sp_num.equals("+923")|| Sp_num.equals("03222541231")) {
            json_parsing();
            set_view();
            save_database();

       // }
    }

    public void set_view() {
        Log.d(LOGTAG," Setting view");
        t_engine_rpm.setText(engine_rpm+" RPM");
        t_vehicle_speed.setText(vehicle_speed+" M/hr");
        t_temperature.setText(temperature+" C");
        t_throttle_pos_level.setText(throttle_level);
        update.setText("Last Update:"+DateFormat.getDateTimeInstance().format(new Date()));
    }

    public void save_database() {
        get_date_time();
        safety_model_obj=new safety_model(engine_rpm,vehicle_speed,temperature,throttle_level,date,time);
        data_source.insert(safety_model_obj);

    }

    public void json_parsing() throws JSONException {
        json_string = smsContent;
        json_Array = new JSONArray(json_string);
        int count = 0;

        while(count<json_Array.length()){
            JSONObject JO = json_Array.getJSONObject(count);
            engine_rpm = JO.getString("engine_rpm");
            vehicle_speed = JO.getString("vehicle_speed");
            temperature = JO.getString("temperature");
            throttle_level = JO.getString("throttle_level");
            count++;

        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        if(receiver!=null)
            unregisterReceiver(receiver);
        data_source.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver,filter);
        data_source.open();
    }



    public void check_log(View view) {
        intent=new Intent(this,safety_list.class);
        startActivity(intent);
    }

    public void get_date_time() {
        Locale locale=new Locale("en_US");
        Locale.setDefault(locale);

        String format="dd/MM/yyyy";
        String format2="HH:mm:ss" ;
        SimpleDateFormat formatter=new SimpleDateFormat(format);
        SimpleDateFormat formatter2=new SimpleDateFormat(format2);

        time = formatter2.format(new Date());

        date = formatter.format(new Date());

        Log.d(LOGTAG,"DATE:" +date+" TIME:"+time);
    }

    public void engine_graph(View view) {
        intent=new Intent(this,graph_parameters.class);
        intent.putExtra(GENRE," Engine RPM graph");
        intent.putExtra(MAX,3000);
        intent.putExtra(VARIABLE,"engine_rpm");
        startActivity(intent);

    }

    public void vehicle_graph(View view) {
        intent=new Intent(this,graph_parameters.class);
        intent.putExtra(GENRE," Vehicle Speed graph");
        intent.putExtra(MAX,100);
        intent.putExtra(VARIABLE,"vehicle_speed");
        startActivity(intent);
    }

    public void temp_graph(View view) {
        intent=new Intent(this,graph_parameters.class);
        intent.putExtra(GENRE," Temperature graph");
        intent.putExtra(MAX,137);
        intent.putExtra(VARIABLE,"temperature");
        startActivity(intent);
    }


    public void throttle_graph(View view) {
        intent=new Intent(this,graph_parameters.class);
        intent.putExtra(GENRE," Throttle graph");
        intent.putExtra(MAX,13);
        intent.putExtra(VARIABLE,"throttle_level");
        startActivity(intent);
    }
}
