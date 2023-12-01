package com.example.myvocab.Utilites;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myvocab.databinding.ActivityMeaningBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DictionaryMeaning {
    private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    private final ActivityMeaningBinding binding;
    private final String word;
    private ProgressDialog dictionaryProgressDialog;
    private final Context context;

    public DictionaryMeaning(ActivityMeaningBinding binding, String word, Context context) {
        this.context = context;
        this.binding = binding;
        this.word = word;
    }

    public void getDictionary() {
        Toast.makeText(context, "This is getDictionary's Toast", Toast.LENGTH_SHORT).show();

        dictionaryProgressDialog();
        String URL = API_URL + word;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                JSONObject jsonObject = response.getJSONObject(0);

                                String jsonWord = jsonObject.optString("word");
                                binding.txtWord.setText(jsonWord);

                                JSONArray meaningsArray = jsonObject.optJSONArray("meanings");
                                if (meaningsArray != null && meaningsArray.length() > 0) {
                                    JSONObject meaningObject = meaningsArray.getJSONObject(0);

                                    JSONArray definitionsArray = meaningObject.optJSONArray("definitions");
                                    if (definitionsArray != null && definitionsArray.length() > 0) {
                                        JSONObject definitionObject = definitionsArray.getJSONObject(0);

                                        String jsonDefinition = definitionObject.optString("definition");
                                        String jsonExample = definitionObject.optString("example");
                                        if (!jsonExample.isEmpty()){

                                            binding.txtExample.setText(jsonExample);
                                        }else {binding.txtExample.setText("There is No Example");}

                                        binding.txtSynonyms.setText("Example 2 Will be added soon! ");
                                        binding.txtMeaning.setText(jsonDefinition);
                                    }
                                } else {
                                    // Handle the case where meanings or definitions array is empty
                                    Toast.makeText(context, "No meanings or definitions available for this word", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle the case where the response array is empty
                                Toast.makeText(context, "No data available for this word", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        } finally {
                            dictionaryProgressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Please Turn On Your Internet ", Toast.LENGTH_SHORT).show();
                dictionaryProgressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);
    }

    private void dictionaryProgressDialog() {
        dictionaryProgressDialog = new ProgressDialog(context);
        dictionaryProgressDialog.setTitle("Fetching... ");
        dictionaryProgressDialog.setMessage("Fetching Dictionary......");
        dictionaryProgressDialog.show();
    }
}
