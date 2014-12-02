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
    private static final String LOGCAT = null;

    public SqliteController(Context applicationcontext) {
        super(applicationcontext, "androidsqlite.db", null, 1);
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE Me ( username TEXT PRIMARY KEY, name TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT, " Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Me";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertMe(String user, String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user);
        values.put("name", name);
        database.insert("Me", null, values);
        database.close();
    }

    public JSONObject getMe() throws JSONException {

        JSONObject mydata = new JSONObject();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT username FROM Me";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {

            mydata.put("username", cursor.getString(0));
        }
        return mydata;
    }

    public void deleteMe() {
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Me";
        database.execSQL(deleteQuery);
    }

}