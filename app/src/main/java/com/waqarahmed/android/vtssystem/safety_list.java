package com.waqarahmed.android.vtssystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class safety_list extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String POSITION = "POSITION";
    ArrayList<safety_model> list;
    Data_source data_source;
    ArrayAdapter<safety_model> arrayAdapter;
    ListView listView;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_list);

        listView= (ListView) findViewById(R.id.listView);
        populate_list();
        listView.setOnItemClickListener(this);
    }

    public void populate_list() {
        data_source=new Data_source(this);
        data_source.open();
        list=new ArrayList();
        list=data_source.getdata();
        arrayAdapter=new ArrayAdapter(this,R.layout.model_text_list,list);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        data_source.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        data_source.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent=new Intent(this,log_detail.class);
        int pos=position;
        intent.putExtra(POSITION,position);
        startActivity(intent);
    }

    public void clear_log(View view) {
        data_source.delete(this);
       populate_list();
    }
}
