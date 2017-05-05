package com.waqarahmed.android.vtssystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

public class Form extends AppCompatActivity {

    private static final String NAME = "NAME";
    private static final String MOBILE_NO = "Mobile_no";
    private static final String EMAIL = "Email";
    private static final String SC_NO = "Sc_no";
    private static final String PASSWORD = "password";
    public static final String MYPREF = "mypref";
    private static final String ENGINE_STATE = "ENGINE_STATE";
    private static final String MOTION_SENSOR_STATE = "MOTION_SENSOR_STATE";
    Spinner spinner;
    String s_name;
    String s_email;
    String s_mobile_no;
    String s_password;
    String s_re_password;
    String sc_no;

    EditText name;
    EditText email;
    EditText mobile_no;
    EditText password;
    EditText re_password;
    Intent intent;
    boolean edit=false;


    public SharedPreferences sharedprefrences;
    SharedPreferences sec_prefrences;
    SharedPreferences track_prefrences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        edit=getIntent().getBooleanExtra(menu.EDIT,false);
        Log.d("HELLO",edit?"true externel":"false externel");
        sharedprefrences=getSharedPreferences(MYPREF, MODE_PRIVATE);
        sec_prefrences=getSharedPreferences(security_activity.SEC_PREF,MODE_PRIVATE);
        track_prefrences=getSharedPreferences(track.TRACK_ID,MODE_PRIVATE);

        show();
        if(getData()&&!edit){
            Log.d("HELLO",getData()?"true internel":"false internel");
            intent=new Intent(this,Login.class);
            startActivity(intent);
        }





        spinner = (Spinner) findViewById(R.id.spinner);
        name=(EditText)findViewById(R.id.name);
        email= (EditText) findViewById(R.id.email);
        mobile_no= (EditText) findViewById(R.id.phoneno);
        password= (EditText) findViewById(R.id.password);
        re_password= (EditText) findViewById(R.id.re_password);

        if(edit){
            edit_function();
        }

        List<String> categories = new ArrayList<String>();
        categories.add("Warid");
        categories.add("Ufone");
        categories.add("Telenor");
        categories.add("Mobilink");
        categories.add("Zong");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text, categories);
        spinner.setAdapter(dataAdapter);




    }

    public void edit_function() {

        String cc=sharedprefrences.getString(SC_NO,"NOT FOUND");
        String cc1=sharedprefrences.getString(NAME,"NOT FOUND");
        String cc2=sharedprefrences.getString(EMAIL,"NOT FOUND");
        String cc3=sharedprefrences.getString(MOBILE_NO,"NOT FOUND");
        String cc4=sharedprefrences.getString(PASSWORD,"NOT FOUND");

        name.setText(cc1);
        email.setText(cc2);
        mobile_no.setText(cc3);

    }

    public String getName(){
        String cc1=sharedprefrences.getString(NAME,"NOT FOUND");
        return NAME;
    }




    public void register(View view) {
       s_password=password.getText().toString();
        s_re_password=re_password.getText().toString();

        if(s_password.compareTo(s_re_password)==0)
            send();
        else
        Toast.makeText(this,"Password not matches", Toast.LENGTH_SHORT).show();



    }

    public void send() {
        service_center();
        s_name=name.getText().toString();
        s_email=email.getText().toString();
        s_mobile_no=mobile_no.getText().toString();

        SharedPreferences.Editor sec_pref_edit=sec_prefrences.edit();
        sec_pref_edit.putBoolean(ENGINE_STATE,false);
        sec_pref_edit.putBoolean(MOTION_SENSOR_STATE,false);
        sec_pref_edit.commit();

        SharedPreferences.Editor track_pref_edit=track_prefrences.edit();
        track_pref_edit.putString("LATITUDE","24.8615");
        track_pref_edit.putString("LONGITUDE","67.0099");
        track_pref_edit.commit();

        Log.d("TRACK",track_prefrences.getString("LATITUDE","NOT FOUND"));
        Log.d("TRACK",track_prefrences.getString("LONGITUDE","NOT FOUND"));


        SharedPreferences.Editor sharedprefrences_edit =sharedprefrences.edit();
        sharedprefrences_edit.putString(NAME,s_name);
        sharedprefrences_edit.putString(MOBILE_NO,s_mobile_no);
        sharedprefrences_edit.putString(EMAIL,s_email);
        sharedprefrences_edit.putString(SC_NO,sc_no);
        sharedprefrences_edit.putString(PASSWORD,s_password);
        sharedprefrences_edit.commit();

      show();
        intent=new Intent(this,menu.class);
        startActivity(intent);



    }

    public  void show() {
        String cc=sharedprefrences.getString(SC_NO,"NOT FOUND");
        String cc1=sharedprefrences.getString(NAME,"NOT FOUND");
        String cc2=sharedprefrences.getString(EMAIL,"NOT FOUND");
        String cc3=sharedprefrences.getString(MOBILE_NO,"NOT FOUND");
        String cc4=sharedprefrences.getString(PASSWORD,"NOT FOUND");
        Log.d("HELLO",cc1+cc2+cc3+cc4+cc);
    }

    public void service_center() {
        String sc= (String) spinner.getSelectedItem();
        Log.d("HELLO","    I AM"+sc);
        switch(sc){
            case "Warid": {
                sc_no = "+9232100006001";
                break;
            }
            case "Ufone": {
                sc_no = "+923330005150";
                break;
            }
            case "Telenor": {
                sc_no = "+923455000010 ";
                break;
            }
            case "Zong": {
                sc_no = "+923040000011";
                break;
            }
            case "Mobilink": {
                sc_no = "+92300000042";
                break;
            }

        }
        Log.d("HELLO","    MY SC_NO"+sc_no);
    }

    public boolean getData(){
        String str1=sharedprefrences.getString(SC_NO,"null");
        if(str1.compareTo("null")==0)

            return false;
        else
        return true;
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}
