package com.example.myvocab.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VocabDao {

    @Query("Select * From VocabModel")
        public List<VocabModel> getAll();

    @Insert
    void addVocab(VocabModel vocabModel);
    @Update
    void updateVocab(VocabModel vocabModel);
    @Delete
    void deleteVocab(VocabModel vocabModel);
}
