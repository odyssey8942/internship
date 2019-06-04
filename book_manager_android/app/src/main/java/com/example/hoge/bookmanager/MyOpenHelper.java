package com.example.hoge.bookmanager;

/**
 * Created by kazuhiro on 2017/08/04.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

    private Context context;
    public MyOpenHelper(Context context) {
        super(context, "app.db", null, 8);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table employees(name text not null," +
                "administrator text not null," +
                "password text not null);");

        db.execSQL("insert into employees(name, administrator, password) values (?, ?, ?);", new Object[]{"master", "あり", "master"});

        db.execSQL("create table books(title text not null," +
                "author text not null," +
                "publish text not null);");

        db.execSQL("create table borrows(publish text not null," +
                                            "title text not null," +
                                            "author text not null," +
                                            "borrower text not null," +
                                            "rental_date text not null," +
                                            "return_date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            context.deleteDatabase("app.db");
        }
    }

}
