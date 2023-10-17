package com.example.myvocab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.myvocab.databinding.ActivityMeaningBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MeaningActivity extends AppCompatActivity {
    ActivityMeaningBinding binding;
    Boolean booleanUrdu=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMeaningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String word = intent.getStringExtra("WORD");
        binding.txtWord.setText(word);
        String URL="https://api.dictionaryapi.dev/api/v2/entries/en/"+word;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject jsonObject=response.getJSONObject(0);
                    JSONArray jsonArray=jsonObject.getJSONArray("meanings");
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                    JSONArray jsonArray1=jsonObject1.getJSONArray("definitions");
                    JSONObject jsonObject2=jsonArray1.getJSONObject(0);
                    JSONArray jsonArray2=jsonObject2.getJSONArray("synonyms");
                    JSONObject jsonObject3=jsonArray2.getJSONObject(0);
                    String meaning=jsonObject2.getString("definition");
                    String example=jsonObject2.getString("example");
                    String synonyms=jsonObject3.getString("synonyms");


                    binding.txtMeaning.setText(meaning);
                    binding.txtExample.setText(example);
                    binding.txtSynonyms.setText(synonyms);
                    Toast.makeText(MeaningActivity.this, "Fetching Meaning ..........", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    Toast.makeText(MeaningActivity.this, "This is Exception: "+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MeaningActivity.this, ""+error, Toast.LENGTH_SHORT).show();

            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
        binding.translationCard.setOnClickListener(view -> getTranslation(word));



    }    private void getTranslation(String word){
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
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(MeaningActivity.this, "Language Model is Downloded", Toast.LENGTH_SHORT).show();
                                booleanUrdu=true;
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MeaningActivity.this, "Error:   "+e, Toast.LENGTH_SHORT).show();
                                booleanUrdu=false;
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