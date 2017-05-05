package com.waqarahmed.android.vtssystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private static final String NAME = "NAME";
    private static final String PASSWORD = "password";
    String name;
    String password;
    EditText e_name;
    EditText e_password;
    SharedPreferences sharedPreferences;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent=new Intent(this,menu.class);
        e_name= (EditText) findViewById(R.id.name);
        e_password= (EditText) findViewById(R.id.password);
        sharedPreferences=getSharedPreferences(Form.MYPREF,MODE_PRIVATE);
        name = sharedPreferences.getString(NAME,"NOT FOUND");
        password = sharedPreferences.getString(PASSWORD,"NOT FOUND");


    }


    public void login(View view) {
        if(name.compareTo(e_name.getText().toString())==password.compareTo(e_password.getText().toString())){
            startActivity(intent);

        }else
            Toast.makeText(this,"Name or Password is wrong!!",Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}
