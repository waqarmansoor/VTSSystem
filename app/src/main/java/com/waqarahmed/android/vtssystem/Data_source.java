package com.waqarahmed.android.vtssystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by waqar on 5/19/2016.
 */
public class Data_source {
    private static final String LOGTAG = "LOGTAG";
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    String query="SELECT * FROM "+DB_helper.TABLE_NAME;

    Data_source(Context context){
        sqLiteOpenHelper=new DB_helper(context);

    }

    public void open(){
        sqLiteDatabase=sqLiteOpenHelper.getWritableDatabase();
        Log.d(LOGTAG,"Database opened");

    }

    public void close(){
        sqLiteOpenHelper.close();
        Log.d(LOGTAG,"Database closed");
    }

    public void insert(safety_model model_obj){
        ContentValues values=new ContentValues();
        values.put(DB_helper.ENGINE_RPM,model_obj.getEngine_rpm());
        values.put(DB_helper.VEHICLE_SPEED,model_obj.getVehicle_speed());
        values.put(DB_helper.TEMPERATURE,model_obj.getTemperature());
        values.put(DB_helper.THROTTLE_LEVEL,model_obj.getThrottle());
        values.put(DB_helper.DATE,model_obj.getDate());
        values.put(DB_helper.TIME,model_obj.getTime());


        long insert=sqLiteDatabase.insert(DB_helper.TABLE_NAME,null,values);

    }

    public ArrayList<safety_model> getdata() {
        ArrayList<safety_model> list;
        list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                safety_model temp_obj=new safety_model();
                temp_obj.setEngine_rpm(cursor.getString(cursor.getColumnIndex(DB_helper.ENGINE_RPM)));
                temp_obj.setVehicle_speed(cursor.getString(cursor.getColumnIndex(DB_helper.VEHICLE_SPEED)));
                temp_obj.setTemperature(cursor.getString(cursor.getColumnIndex(DB_helper.TEMPERATURE)));
                temp_obj.setThrottle(cursor.getString(cursor.getColumnIndex(DB_helper.THROTTLE_LEVEL)));
                temp_obj.setDate(cursor.getString(cursor.getColumnIndex(DB_helper.DATE)));
                temp_obj.setTime(cursor.getString(cursor.getColumnIndex(DB_helper.TIME)));
                list.add(temp_obj);
            }
        }
       return list;
    }

    public void delete(Context context){
        sqLiteDatabase.execSQL("Delete from "+ DB_helper.TABLE_NAME+";");
    }

}
