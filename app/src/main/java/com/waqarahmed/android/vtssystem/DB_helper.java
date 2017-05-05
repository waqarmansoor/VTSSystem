package com.waqarahmed.android.vtssystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by waqar on 5/19/2016.
 */
public class DB_helper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "VTSS";
    private static final String VTSS_DB = TABLE_NAME + "_DB";
    private static final int DB_VERSION =1;
    public static final String COL_ID = "COL_ID";
    public static final String ENGINE_RPM = "ENGINE_RPM";
    public static final String VEHICLE_SPEED = "VEHICLE_SPEED";
    public static final String THROTTLE_LEVEL = "THROTTLE_LEVEL";
    public static final String DATE = "DATE";
    public static final String TEMPERATURE = "TEMPERATURE";
    public static final String TIME = "TIME";
    private static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ENGINE_RPM + " TEXT," +
            VEHICLE_SPEED + " TEXT," +
            TEMPERATURE + " TEXT," +
            THROTTLE_LEVEL + " TEXT,"+
            DATE + " TEXT," +
            TIME + " TEXT );";

    public DB_helper(Context context) {
        super(context, VTSS_DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("LOGTAG","Table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }

}
