package com.waqarahmed.android.vtssystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class track extends FragmentActivity implements OnMapReadyCallback {

    public static final String TRACK_ID = "TRACK_ID";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    String json_string;
    //    JSONObject jsonObject;
    JSONArray json_Array;
    private GoogleMap mMap;
    String lat,lon;
    BroadcastReceiver receiver=null;

    SharedPreferences sharedPreferences;




    SharedPreferences.Editor shared_pref_edit;





    private static final float DEFAULTZOOM = 8;
    private static double ini_Lati ;
    private static double ini_Lngi ;
    Marker marker;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        sharedPreferences = getSharedPreferences(TRACK_ID, MODE_PRIVATE);
        shared_pref_edit = sharedPreferences.edit();
        String latitude=sharedPreferences.getString(LATITUDE,"24.8615");
        String longitude=sharedPreferences.getString(LONGITUDE,"67.0099");
        Log.d("TRACK",latitude+" I AM FROM MAP"+ longitude+" ");
        ini_Lati=Double.parseDouble(latitude);
        ini_Lngi=Double.parseDouble(longitude);


        filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
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


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gotoLocation(ini_Lati,ini_Lngi, DEFAULTZOOM);
    }

    public void processReceiver(Context context, Intent intent) throws JSONException {
        Toast.makeText(context, "New Coordinates received", Toast.LENGTH_LONG).show();

        Bundle bundle = intent.getExtras();
        Object[] objArr = (Object[]) bundle.get("pdus");
        String smsContent="";
        for (int i = 0; i < objArr.length; i++) {
            SmsMessage smsMsg = SmsMessage.createFromPdu((byte[]) objArr[i]);
            String smsBody = smsMsg.getMessageBody();
            //String senderNumber = smsMsg.getDisplayOriginatingAddress();
            smsContent +=smsBody;
            json_string = smsContent;
            json_Array = new JSONArray(json_string);
            int count = 0;

            while(count<json_Array.length()){
                JSONObject JO = json_Array.getJSONObject(count);
                lat = JO.getString("lat");
                lon = JO.getString("lon");
                count++;
            }
        }

        shared_pref_edit.putString(LATITUDE,lat);
        shared_pref_edit.putString(LONGITUDE,lon);
        shared_pref_edit.commit();

        ini_Lati = Double.valueOf(lat);
        ini_Lngi = Double.valueOf(lon);



        gotoLocation(ini_Lati,ini_Lngi,DEFAULTZOOM);



    }

    private void gotoLocation(double lt, double lng, float zoom){
        LatLng ll = new LatLng(lt,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,zoom);
        mMap.moveCamera(update);
        if (marker != null){
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(ll).title("Current location"));
    }

    @Override
    protected void onStop() {
        if(receiver!=null)
            unregisterReceiver(receiver);
        super.onStop();
    }
}
