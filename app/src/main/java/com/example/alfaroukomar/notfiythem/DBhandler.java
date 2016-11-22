package com.example.alfaroukomar.notfiythem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Alfarouk omar on 11/21/2016.
 */
public class DBhandler extends SQLiteOpenHelper
{
    static final int version =1;
    static  final String DBname ="Notfiy.db";
    public DBhandler(Context context) {
        super(context, DBname, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE IF NOT EXISTS news (ID TEXT  UNIQUE ,name TEXT,phone TEXT ,type TEXT,other TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("");
    }


    public void insertnewdata (Newsobject data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID",data.getID());
        contentValues.put("name",data.getName());
        contentValues.put("phone",data.getPhone());
        contentValues.put("type",data.getType());
        contentValues.put("other",data.getOther());
        db.insertWithOnConflict("news",null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);


    }
    public ArrayList getallwithnew() {
        final String TABLE_NAME = "news";
        String selectQuery = "select * from " + TABLE_NAME +" ;";
        Newsobject n =new Newsobject();
        ArrayList<Newsobject> arrayList = new ArrayList<>();
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String Id = cursor.getString(0);
                String name = cursor.getString(1);
                String Phone = cursor.getString(2);
                String type = cursor.getString(3);
                String other = cursor.getString(4);
                n = new Newsobject();
                n.setID(Id);n.setName(name);n.setPhone(Phone);n.setType(type);n.setOther(other);

                arrayList.add(n);
            }
            while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return arrayList;

    }


    public String getlastid()
    {

        final String TABLE_NAME = "news";
        String selectQuery = "SELECT row from "+ TABLE_NAME +" ORDER BY ID DESC LIMIT 1;"  ;
        String ID;

        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);
        cursor.moveToFirst();
        ID=cursor.getColumnName(0);
        cursor.moveToNext();
        db.close();
        cursor.close();
        if (ID!="" )
        {
            return  ID;
        }
        else
        {
            return "0";
        }

    }
}
