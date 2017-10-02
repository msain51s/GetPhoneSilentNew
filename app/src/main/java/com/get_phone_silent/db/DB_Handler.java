package com.get_phone_silent.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.get_phone_silent.model.LocationDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 9/26/2017.
 */

public class DB_Handler extends SQLiteOpenHelper {

    public DB_Handler(Context context) {
        super(context, DB_Member.getDatabaseName(), null, DB_Member.getDatabaseVersion());

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DB_Member.getCreateLocationDataTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DB_Member.getDropTableLocationData());
        onCreate(db);
    }


    public void insertLocationData(LocationDataModel model) {
        ContentValues values = new ContentValues();

        values.put(DB_Member.getColumnAddress(), model.getAddress());
        values.put(DB_Member.getColumnLatitude(), model.getLatitude());
        values.put(DB_Member.getColumnLongitude(), model.getLongitude());
        values.put(DB_Member.getColumnStatus(),model.getStatus());
        values.put(DB_Member.getColumnRadiusDistance(),model.getAreaRadius());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DB_Member.getTableLocationData(), null, values);
        db.close();
    }

    public ArrayList<LocationDataModel> getRegisteredLocationDataList() {                      //Done
        ArrayList<LocationDataModel> list = new ArrayList<LocationDataModel>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(DB_Member.getRegiesteredLocationData_query(), null);

        LocationDataModel model =null;

        if (cursor.moveToFirst()) {
            do {
                model = new LocationDataModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setAddress(cursor.getString(1));
                model.setLatitude(cursor.getString(2));
                model.setLongitude(cursor.getString(3));
                model.setStatus(cursor.getString(5));
                model.setAreaRadius(cursor.getString(4));

                list.add(model);
            } while (cursor.moveToNext());

            for (int i = list.size(); i > cursor.getCount(); i--) {
                list.remove(i);
            }
            cursor.close();
        } else {
            model = null;
        }
        db.close();

        return list;
    }

    public void updateLocationDataStatus(String status,int id) {                      //Done
        ContentValues values = new ContentValues();
        values.put(DB_Member.getColumnStatus(), status);

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(DB_Member.getTableLocationData(), values, DB_Member.getColumnId() + " = ? ",
                new String[] {String.valueOf(id)});

        db.close();
    }

    public void deleteLocationData(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DB_Member.getTableLocationData(), DB_Member.getColumnId() + " = ?",
                    new String[] { String.valueOf(id) });
        db.close();
    }

    public void deleteAllLocationData(){

            SQLiteDatabase db = this.getWritableDatabase();

            db.delete(DB_Member.getTableLocationData(),null, null);
            db.close();
         }
}
