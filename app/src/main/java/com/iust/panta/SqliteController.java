package com.iust.panta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SqliteController extends SQLiteOpenHelper {
    private String  TAG="sqliteController";
    private static final String LOGCAT = null;

    public SqliteController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 21);
        Log.d(TAG, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;

        query = "CREATE TABLE Me ( username TEXT PRIMARY KEY, name TEXT)";
        database.execSQL(query);

        query=  "CREATE TABLE SaveRegID ( username TEXT PRIMARY KEY, regID TEXT)";

        database.execSQL(query);
        Log.d(TAG, " Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Me";
        String query2;
        query2 ="DROP TABLE IF EXISTS SaveRegID";
        database.execSQL(query);
        database.execSQL(query2);
        onCreate(database);
    }

    public void insertMe(String user, String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user);
        values.put("name", name);
        this.deleteMe();
        database.insert("Me", null, values);
        database.close();
    }
    public void insertRegID(String user,String regID)
    {
        Log.d(TAG,"Insert");
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user);
        Log.d(TAG,user);
        Log.d(TAG,regID);

        values.put("regID",regID);

        this.deleteRegID();
        database.insert("SaveRegID",null,values);
        database.close();

    }

    public JSONObject getMe() throws JSONException {

        JSONObject mydata = new JSONObject();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT username,name FROM Me";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            mydata.put("username", cursor.getString(0));
            mydata.put("name", cursor.getString(1));
        }
        cursor.close();
        return mydata;
    }

    public void deleteMe() {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Me";
        database.execSQL(deleteQuery);
    }
    public void deleteRegID() {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM SaveRegID";
        database.execSQL(deleteQuery);
    }

    public JSONObject getRegID()throws JSONException
    {
        {
            Log.d(TAG,"in gerREGID");
            JSONObject mydata = new JSONObject();
            SQLiteDatabase database = this.getReadableDatabase();
            String selectQuery = "SELECT username,regID FROM SaveRegID";
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                mydata.put("username", cursor.getString(0));
                mydata.put("regID", cursor.getString(1));
            }

            Log.d(TAG,mydata.getString("username"));
            Log.d(TAG,mydata.getString("regID"));

            cursor.close();
            return mydata;

        }
    }
}