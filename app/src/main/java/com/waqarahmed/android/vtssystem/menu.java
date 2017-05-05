package com.waqarahmed.android.vtssystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class menu extends AppCompatActivity {
    public static final String EDIT = "EDIT";
    TextView name;
    String a_name;
    SharedPreferences sharedPreferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sharedPreferences=getSharedPreferences(Form.MYPREF,MODE_PRIVATE);
        a_name=sharedPreferences.getString("NAME","Null");
        name= (TextView) findViewById(R.id.name);
        name.setText(a_name);
    }

    public void edit_profile(View view) {
        intent=new Intent(this,Form.class);
        intent.putExtra(EDIT,true);
        startActivity(intent);

    }

    public void security(View view) {
        intent=new Intent(this,security_activity.class);
        startActivity(intent);
    }

    public void track(View view) {
        intent=new Intent(this,track.class);
        startActivity(intent);
    }

    public void safety(View view) {
        intent=new Intent(this,safety_activity.class);
        startActivity(intent);
    }
}
