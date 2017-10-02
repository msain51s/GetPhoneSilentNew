package com.get_phone_silent.db;

/**
 * Created by Administrator on 9/26/2017.
 */

public class DB_Member {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "get_phone_silent.db";
    private static final String TABLE_LOCATION_DATA = "LocationData";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_RADIUS_DISTANCE = "radius_distance";

    public static String getColumnRadiusDistance() {
        return COLUMN_RADIUS_DISTANCE;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public static String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getTableLocationData() {
        return TABLE_LOCATION_DATA;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnAddress() {
        return COLUMN_ADDRESS;
    }

    public static String getColumnLatitude() {
        return COLUMN_LATITUDE;
    }

    public static String getColumnLongitude() {
        return COLUMN_LONGITUDE;
    }

    public static String getColumnStatus() {
        return COLUMN_STATUS;
    }

    public static String getCreateLocationDataTableQuery() {
        return CREATE_LOCATION_DATA_TABLE;
    }

    public static String CREATE_LOCATION_DATA_TABLE = "CREATE TABLE " + TABLE_LOCATION_DATA
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_ADDRESS + " TEXT," + COLUMN_LATITUDE
            + " TEXT," + COLUMN_LONGITUDE + " TEXT,"
            + COLUMN_RADIUS_DISTANCE + " TEXT,"
            + COLUMN_STATUS + " TEXT" +")";

    public static String getDropTableLocationData() {
        return dropTableLocationData;
    }

    public static String dropTableLocationData="DROP TABLE IF EXISTS " + TABLE_LOCATION_DATA;

    public static String getRegiesteredLocationData_query() {
        return get_query;
    }

    public static String get_query = "Select distinct * FROM " + TABLE_LOCATION_DATA
            + " order by _id desc";
}
