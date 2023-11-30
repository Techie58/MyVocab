package com.example.myvocab.Utilites;

import android.content.Context;
import android.icu.text.Transliterator;
import android.util.Log;
import android.widget.Toast;

import com.example.myvocab.database.VocabDBHelper;
import com.example.myvocab.database.VocabModel;

import java.util.ArrayList;
import java.util.Locale;

public class TextToSpeech {
    android.speech.tts.TextToSpeech textToSpeech;

    // In your TextToSpeech class
// In your TextToSpeech class
    public void position(int position, Context context) {
        ArrayList<VocabModel> myWord = (ArrayList<VocabModel>) VocabDBHelper.getInstance(context).vocabDao().getAll();

        String word = myWord.get(position).getWord();

        Log.d("stack", "Position method " + word);
        if (position >= 0) {
            // Use the application context to initialize TextToSpeech
            textToSpeech = new android.speech.tts.TextToSpeech(context.getApplicationContext(), i -> {
                if (i == android.speech.tts.TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setSpeechRate(0.5f);
                    textToSpeech.speak(word, android.speech.tts.TextToSpeech.QUEUE_ADD, null);
                }
            });
            Toast.makeText(context, "text to speech position is " + position, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(context, "There is an error in getting position", Toast.LENGTH_SHORT).show();
    }
}
