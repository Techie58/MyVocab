package com.example.myvocab.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvocab.MainActivity;
import com.example.myvocab.MeaningActivity;
import com.example.myvocab.R;
import com.example.myvocab.database.VocabDBHelper;
import com.example.myvocab.database.VocabModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    Context context;
    private ArrayList<VocabModel> arrVocab;
    TextToSpeech textToSpeech;

    public RVAdapter(Context context, ArrayList<VocabModel> arrVocab) {
        this.context = context;
        this.arrVocab = arrVocab;
    }
    public void setFilterList(List<VocabModel> fList){
        this.arrVocab=(ArrayList<VocabModel>) fList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.show_vocab_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.ViewHolder holder, int position) {
        holder.txtWord.setText(arrVocab.get(position).getWord());
        holder.txtMeaning.setText(arrVocab.get(position).getMeaning());
        holder.lWord.setOnClickListener(view -> spekTxt(arrVocab.get(position).getWord()));
        holder.lMeaning.setOnClickListener(view -> spekTxt(arrVocab.get(position).getMeaning()));
        if (arrVocab.get(position).getMeaning().equals("")){
            holder.meaningSpek.setVisibility(View.GONE);
            holder.txtMeaning.setVisibility(View.GONE);}
    }


    private void spekTxt(String word) {
        textToSpeech=new TextToSpeech(context, i -> {
            if (i==TextToSpeech.SUCCESS){
                Toast.makeText(context, "speaking", Toast.LENGTH_SHORT).show();
                textToSpeech.setLanguage(Locale.US);
                textToSpeech.setSpeechRate(0.5f);
                textToSpeech.speak(word,TextToSpeech.QUEUE_ADD,null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrVocab.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtWord,txtMeaning;
        ImageView meaningSpek,wordSpek;
        LinearLayoutCompat wordLayout;

        RelativeLayout lWord,lMeaning;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWord=itemView.findViewById(R.id.txtWord);
            txtMeaning=itemView.findViewById(R.id.textMeaning);
            wordSpek=itemView.findViewById(R.id.wordSpek);
            meaningSpek=itemView.findViewById(R.id.meaningSpek);
            wordLayout=itemView.findViewById(R.id.wordLayout);
            lWord=itemView.findViewById(R.id.lWord);
            lMeaning=itemView.findViewById(R.id.lMeaning);
        }
    }
}
