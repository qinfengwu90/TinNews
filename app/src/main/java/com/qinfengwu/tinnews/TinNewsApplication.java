package com.qinfengwu.tinnews;

import android.app.Application;

import androidx.room.Room;

import com.qinfengwu.tinnews.database.TinNewsDatabase;

public class TinNewsApplication extends Application {
    private static TinNewsDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(this, TinNewsDatabase.class, "tinnews_DB").build();
    }

    public  static TinNewsDatabase getDatabase() {
        return database;
    }
}
