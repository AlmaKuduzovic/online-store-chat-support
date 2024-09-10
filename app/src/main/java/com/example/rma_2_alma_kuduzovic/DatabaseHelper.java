package com.example.rma_2_alma_kuduzovic;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {CardModel.class, UserCartModel.class}, exportSchema = false, version =10)
public abstract class DatabaseHelper extends RoomDatabase {

    private static final String DB_NAME = "oilinformationdb";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDB(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, DatabaseHelper.class, DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract CardDao cardDao();
    public abstract UserCartDao userCartDao();

}
