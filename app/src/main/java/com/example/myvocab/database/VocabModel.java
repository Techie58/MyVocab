package com.example.myvocab.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "VocabModel")
public class VocabModel {
    public VocabModel(){}

    private String Word;

    @PrimaryKey(autoGenerate = true)
    private int id;
    @Ignore
    public VocabModel(String word) {
        Word = word;
    }

    public VocabModel(String word, int id) {
        Word = word;
        this.id = id;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
