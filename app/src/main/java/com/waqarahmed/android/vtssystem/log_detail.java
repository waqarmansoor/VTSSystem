package com.waqarahmed.android.vtssystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class log_detail extends AppCompatActivity {

    TextView t_engine_rpm;
    TextView t_vehicle_speed;
    TextView t_temperature;
    TextView t_throttle_pos_level;
    TextView update;

    ArrayList<safety_model> list;
    Data_source data_source;
    safety_model model_temp;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_detail);

        model_temp=new safety_model();
        data_source=new Data_source(this);
        data_source.open();

        list=new ArrayList();
        list=data_source.getdata();

        t_engine_rpm= (TextView) findViewById(R.id.engine_rpm_val);
        t_vehicle_speed= (TextView) findViewById(R.id.vehicle_speed_val);
        t_temperature= (TextView) findViewById(R.id.temperature_val);
        t_throttle_pos_level= (TextView) findViewById(R.id.throttle_level_val);
        update= (TextView) findViewById(R.id.update);

        position=getIntent().getIntExtra(safety_list.POSITION,0);
        setttingView();
        Log.d("LOG_CLASS",String .valueOf(position));
    }

    public void setttingView() {
        model_temp=list.get(position);
        t_engine_rpm.setText(model_temp.getEngine_rpm()+" RPM");
        t_vehicle_speed.setText(model_temp.getVehicle_speed()+" M/hr");
        t_throttle_pos_level.setText(model_temp.getThrottle());
        t_temperature.setText(model_temp.getTemperature()+" C");
        update.setText("Logged on:"+model_temp.getDate()+" "+model_temp.getTime());
    }

    @Override
    protected void onPause() {
        super.onPause();
        data_source.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        data_source.open();
    }
}
