package com.waqarahmed.android.vtssystem;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.telephony.SmsManager;

public class security_activity extends AppCompatActivity {
    public static final String SEC_PREF = "sec_pref";
    private static final String MOBILE_NO = "Mobile_no";
    private static final String SC_NO = "Sc_no";

    EditText latitude;
    EditText longitude;
    EditText radius;
    Switch  motion_sensor;
    Switch engine_state;
    boolean motion_sensor_state;
    boolean engine_sensor_state;
    SharedPreferences sharedPreferences;
    SharedPreferences form_shared;
    SharedPreferences.Editor sec_pref_edit;

    private static final String ENGINE_STATE = "ENGINE_STATE";
    private static final String MOTION_SENSOR_STATE = "MOTION_SENSOR_STATE";


    String s_motion_sensor;
    String s_engine_state;
    String s_latitude;
    String s_longitude;
    String s_radius;

    String mobile_no;
    String sc;
    String msg;


    IntentFilter filter;
    PendingIntent sentPI;
    PendingIntent deliveredPI;
    BroadcastReceiver receive=null;
    BroadcastReceiver receive2=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_activity);

        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        form_shared=getSharedPreferences(Form.MYPREF,MODE_PRIVATE);

        sharedPreferences=getSharedPreferences(SEC_PREF,MODE_PRIVATE);
        sec_pref_edit = sharedPreferences.edit();
        latitude= (EditText) findViewById(R.id.latitude);
        longitude= (EditText) findViewById(R.id.longitude);
        radius= (EditText) findViewById(R.id.radius);

        motion_sensor= (Switch) findViewById(R.id.motion_sensor);
        motion_sensor_state=sharedPreferences.getBoolean(MOTION_SENSOR_STATE,false);
        motion_sensor.setChecked(motion_sensor_state);

        engine_state= (Switch) findViewById(R.id.engine_state);
        engine_sensor_state=sharedPreferences.getBoolean(ENGINE_STATE,false);
        engine_state.setChecked(engine_sensor_state);

    }



    public void motion_sensor(View view) {
        motion_sensor_state=sharedPreferences.getBoolean(MOTION_SENSOR_STATE,false);

        if(motion_sensor_state)
            motion_sensor_state=false;

        else
            motion_sensor_state=true;

            sec_pref_edit.putBoolean(MOTION_SENSOR_STATE,motion_sensor_state);
            sec_pref_edit.commit();
        Toast.makeText(security_activity.this,motion_sensor_state?"Sensor Activated":"Sensor Deactivated" , Toast.LENGTH_SHORT).show();


    }

    public void engine_state(View view) {
        engine_sensor_state=sharedPreferences.getBoolean(ENGINE_STATE,false);

        if(engine_sensor_state)
            engine_sensor_state = false;


        else
            engine_sensor_state=true;

        sec_pref_edit.putBoolean(ENGINE_STATE,engine_sensor_state);
        sec_pref_edit.commit();
        Toast.makeText(security_activity.this,engine_sensor_state?"Engine Released":"Engine Killed" , Toast.LENGTH_SHORT).show();
    }


    public void send(View view) {
        data_retrieval();
        sc=form_shared.getString(SC_NO," ");
        Log.d("SECURITY",s_latitude+" "+s_longitude+" "+s_radius+" "+s_engine_state+" "+s_motion_sensor+"    "+sc);
        mobile_no=form_shared.getString(MOBILE_NO," ");
        sc=form_shared.getString(SC_NO," ");
        msg="Latitude:"+s_latitude+" Longitude:"+s_longitude+" Radius:"+s_radius+" Engine_State:"+s_engine_state+" Motion_Sensor:"+s_motion_sensor;
        sms_send();
    }

    public void sms_send() {

        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";


        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);



        receive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(security_activity.this, "Data has been sended", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };


        registerReceiver(receive, new IntentFilter(SENT));


        receive2=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(security_activity.this, "Data send successfull", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "Data send not successfull", Toast.LENGTH_LONG).show();
                        break;
                }

            }
        };

        registerReceiver(receive2, new IntentFilter(DELIVERED));


        SmsManager sms = SmsManager.getDefault();

        sms.sendTextMessage(mobile_no, sc, msg, sentPI, deliveredPI);

    }




    public void data_retrieval() {
        s_latitude=latitude.getText().toString();
        s_longitude=longitude.getText().toString();
        s_radius=radius.getText().toString();

        engine_sensor_state=sharedPreferences.getBoolean(ENGINE_STATE,false);
        s_engine_state=engine_sensor_state?"true":"false";

        motion_sensor_state=sharedPreferences.getBoolean(MOTION_SENSOR_STATE,false);
        s_motion_sensor=motion_sensor_state?"true":"false";
    }

    @Override
    protected void onStop() {
        if(receive!=null)
            unregisterReceiver(receive);
        if(receive2!=null)
            unregisterReceiver(receive2);

        super.onStop();
    }
}
