package com.example.lowicz.yourpastebins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by lowicz on 03.05.15.
 */

// wydawnictwo apress ksiazki
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

    public String insertPaste(Paste qvalues) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("url", qvalues.getUrl());
        values.put("paste_text", qvalues.getPaste_text());

        long result = db.insert("pastes", null, values);
        return String.valueOf(result);
    }

    public int updatePaste(Paste qvalues) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("url", qvalues.getUrl());
        values.put("paste_text", qvalues.getPaste_text());
        return db.update("pastes", values, "pasteID" + " = ?", new String[]{qvalues.getPasteID()});
    }

    public void deletePaste(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM pastes WHERE pasteID='" + id + "'";

        db.execSQL(query);
    }

    public ArrayList<Paste> getAll() {
        ArrayList<Paste> pastesArrayList;

        pastesArrayList = new ArrayList<>();

        String query = "SELECT * FROM pastes";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Paste pasteMap = new Paste();

                pasteMap.setPasteID(cursor.getString(0));
                pasteMap.setUrl(cursor.getString(1));
                pasteMap.setPaste_text(cursor.getString(2));

                pastesArrayList.add(pasteMap);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pastesArrayList;
    }
}
