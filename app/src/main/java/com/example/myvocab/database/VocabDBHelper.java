package com.example.myvocab.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.PrimaryKey;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = VocabModel.class,exportSchema = false,version = 1)
public abstract class VocabDBHelper extends RoomDatabase {
    private static final String DB_NAME="vocab_db";
    private static VocabDBHelper instacne;

    public static synchronized VocabDBHelper getInstance(Context context){
        if (instacne==null){
            instacne=Room.databaseBuilder(context,VocabDBHelper.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();

        }return instacne;
    }public abstract VocabDao vocabDao();
}
