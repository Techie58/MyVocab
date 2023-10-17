package com.example.myvocab.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "VocabModel")
public class VocabModel {
    public VocabModel(){}

    private String Word,Meaning;

    @PrimaryKey(autoGenerate = true)
    private int id;
    @Ignore
    public VocabModel(String word, String meaning) {
        Word = word;
        Meaning = meaning;
    }

    public VocabModel(String word, String meaning, int id) {
        Word = word;
        Meaning = meaning;
        this.id = id;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    public String getMeaning() {
        return Meaning;
    }

    public void setMeaning(String meaning) {
        Meaning = meaning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
