package com.waqarahmed.android.vtssystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class graph_parameters extends AppCompatActivity {


    String json_string;
    JSONArray json_Array;
    BroadcastReceiver receiver=null;
    String speed;
    float speed_x;
    private RelativeLayout mainLayout;
    private LineChart mChart;

    float max;
    String GENRE_G;
    String variable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GENRE_G=getIntent().getStringExtra(safety_activity.GENRE);
        max=getIntent().getFloatExtra(safety_activity.MAX,1000);
        variable=getIntent().getStringExtra(safety_activity.VARIABLE);
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arr0, Intent arr1) {
                try {
                    processReceiver(arr0,arr1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(receiver,filter);


        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        //Create line Chart

        mChart = new LineChart(this);
        //add to main Layout

        // mainLayout.addView(mChart);
        setContentView(mChart);


        //customize line chart
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No Data now");

        //enable value highlighting
        mChart.setHighlightPerTapEnabled(true);


        //enable touch
        mChart.setTouchEnabled(true);

        //scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);

        //enabling pinch zoom to avoid scaling x and y
        mChart.setPinchZoom(true);

        //background color
        mChart.setBackgroundColor(Color.BLACK);

        //data
        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        //add data to chart
        mChart.setData(data);

        //get legend object
        Legend l = mChart.getLegend();

        //customize legend
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis x1 = mChart.getXAxis();
        x1.setTextColor(Color.BLACK);
        x1.setDrawGridLines(true);
        x1.setAvoidFirstLastClipping(true);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setAxisMaxValue(max);
        y1.setDrawGridLines(true);

        YAxis y12 = mChart.getAxisRight();
        y12.setEnabled(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(receiver!=null)
            unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {

        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i=0;i<100;i++){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

    }

    //Values set
    private void addEntry() {
        LineData data = mChart.getData();

        if (data != null) {
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            //add data
            data.addXValue("");
            data.addEntry(new Entry(speed_x, set.getEntryCount()), 0);
            //data.addEntry(new Entry(speed_x, set.getEntryCount(),0));


            //notify on data change
            mChart.notifyDataSetChanged();

            //limit no of variable entries
            mChart.setVisibleXRangeMaximum(6);

            // scroll to last value
            mChart.moveViewToX(data.getXValCount() - 7);

        }

    }


    private LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "Values received per minute (x-axis)"+GENRE_G);
        set.setDrawCubic(true);
        set.setCubicIntensity(0.8f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(3);
        set.setFillAlpha(40);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244,117,177));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(15f);
        return set;

    }


    public void processReceiver(Context context, Intent intent) throws JSONException {
        Toast.makeText(context, "New Data received", Toast.LENGTH_LONG).show();
        Log.d("LOGING","HELLO");

        Bundle bundle = intent.getExtras();
        Object[] objArr = (Object[]) bundle.get("pdus");
        String smsContent="";
        for (int i = 0; i < objArr.length; i++) {
            SmsMessage smsMsg = SmsMessage.createFromPdu((byte[]) objArr[i]);
            String smsBody = smsMsg.getMessageBody();
            smsContent +=smsBody;
            json_string = smsContent;
            json_Array = new JSONArray(json_string);
            int count = 0;

            while(count<json_Array.length()){
                JSONObject JO = json_Array.getJSONObject(count);
                speed = JO.getString(variable);
                count++;
            }
        }
        speed_x = Float.valueOf(speed);
    }



}