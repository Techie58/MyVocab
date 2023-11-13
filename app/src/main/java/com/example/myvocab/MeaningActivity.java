package com.example.myvocab;

import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myvocab.Utilites.DictionaryMeaning;
import com.example.myvocab.databinding.ActivityMeaningBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MeaningActivity extends AppCompatActivity {
    ActivityMeaningBinding binding;
    int edtTxt;
    Boolean booleanUrdu = false;
    ProgressDialog progressDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeaningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String word = intent.getStringExtra("WORD");

        showProgressDialog();
        getTranslation(word);

        DictionaryMeaning dictionaryMeaning = new DictionaryMeaning(binding, word, this);
        dictionaryMeaning.getDictionary();

        binding.txtTranslation.setText(word);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Downloading...");
        progressDialog.setMessage("Language Model is downloading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }



    private void getTranslation(String word) {
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.URDU)
                        .build();
        final Translator englishGermanTranslator =
                Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(MeaningActivity.this, "Language Model is Downloaded", Toast.LENGTH_SHORT).show();
                                booleanUrdu = true;
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MeaningActivity.this, "Error:   " + e, Toast.LENGTH_SHORT).show();
                                booleanUrdu = false;
                            }
                        });

        englishGermanTranslator.translate(word).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                binding.txtTranslation.setText(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.txtTranslation.setText(String.valueOf(e));
            }
        });
    }
}
