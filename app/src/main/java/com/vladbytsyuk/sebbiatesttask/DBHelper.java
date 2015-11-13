package com.vladbytsyuk.sebbiatesttask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VladBytsyuk on 07.11.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "tweetsTable", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tweetsTable " +
                       "(id integer primary key autoincrement, " +
                        "user_name text, " +
                        "tweet_text text, " +
                        "tweet_time text, " +
                        "avatar_url text, " +
                        "friends_count integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
