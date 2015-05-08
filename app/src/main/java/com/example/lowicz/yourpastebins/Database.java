package com.example.lowicz.yourpastebins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lowicz on 03.05.15.
 */
public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "MyPastes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE pastes ( "
                + "pasteID INTEGER PRIMARY KEY, "
                + "url TEXT, "
                + "paste_text TEXT )";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS pastes";
        db.execSQL(query);
        onCreate(db);
    }

    public void insertPaste(HashMap<String, String> qvalues) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("url", qvalues.get("url"));
        values.put("paste_text", qvalues.get("paste_text"));

        db.insert("pastes", null, values);

        db.close();
    }

    public int updatePaste(HashMap<String, String> qvalues) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("url", qvalues.get("url"));
        values.put("paste_text", qvalues.get("paste_text"));

        return db.update("pastes", values, "pasteID" + " = ?", new String[] {qvalues.get("pasteID")});
    }

    public void deletePaste(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM pastes WHERE pasteID='" + id + "'";

        db.execSQL(query);
    }

    public ArrayList<HashMap<String, String>> getAll() {
        ArrayList<HashMap<String, String>> pastesArrayList;

        pastesArrayList = new ArrayList<HashMap<String, String>>();

        String query = "SELECT * FROM pastes";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> pasteMap = new HashMap<String, String>();

                pasteMap.put("pastesID", cursor.getString(0));
                pasteMap.put("url", cursor.getString(1));
                pasteMap.put("paste_text", cursor.getString(2));

                pastesArrayList.add(pasteMap);
            } while (cursor.moveToNext());
        }

        return pastesArrayList;
    }

    public HashMap<String, String> getPasteInfo(String id) {
        HashMap<String, String> pasteMap = new HashMap<String, String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM pastes WHERE pasteID='"+ id + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                pasteMap.put("url", cursor.getString(1));
                pasteMap.put("paste_text", cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return pasteMap;
    }


}
