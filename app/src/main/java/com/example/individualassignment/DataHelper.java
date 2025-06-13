package com.example.individualassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electricity_bills.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "bill_table";

    public DataHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT, " +
                "kwh INTEGER, " +
                "rebate REAL, " +
                "total_charges REAL, " +
                "final_cost REAL);";
        Log.d("DataHelper", "onCreate: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert data
    public boolean insertData(String month, int kwh, double rebate, double totalCharges, double finalCost){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("month", month);
        values.put("kwh", kwh);
        values.put("rebate", rebate);
        values.put("total_charges", totalCharges);
        values.put("final_cost", finalCost);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    // Get all data
    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Get detail by ID
    public Cursor getDataById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(id)});
    }
}
