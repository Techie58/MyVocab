package com.example.myvocab.Utilites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myvocab.MainActivity;
import com.example.myvocab.MeaningActivity;
import com.example.myvocab.database.VocabDBHelper;
import com.example.myvocab.database.VocabModel;
import com.example.myvocab.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class SwipeFile {
    private ArrayList<VocabModel> arrVocab;
    private MainActivity mainActivity;
    public SwipeFile(MainActivity mainActivity,ArrayList<VocabModel> arrVocab){
        this.arrVocab=arrVocab;
        this.mainActivity=mainActivity;

    }
    VocabDBHelper vocabDBHelper=VocabDBHelper.getInstance(mainActivity);

    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position=viewHolder.getAdapterPosition();
            if (direction==ItemTouchHelper.LEFT){
                AlertDialog alertDialog=new AlertDialog.Builder(mainActivity)
                        .setTitle("Delete").setMessage("Do you wanna delete this Word")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            vocabDBHelper.vocabDao().deleteVocab(new VocabModel(arrVocab.get(position).getWord(),arrVocab.get(position).getMeaning(),arrVocab.get(position).getId()));
                            Toast.makeText(mainActivity, "Deleted", Toast.LENGTH_SHORT).show();
                            mainActivity.showVocab();
                        }).setNegativeButton("No", (dialogInterface, i) -> {
                            mainActivity.showVocab();
                            Toast.makeText(mainActivity, "Swipe Right To Refresh App", Toast.LENGTH_SHORT).show();
                        }).show();
            }else {
                Intent wordIntent=new Intent(mainActivity, MeaningActivity.class);
                wordIntent.putExtra("WORD",arrVocab.get(position).getWord());
                mainActivity.startActivity(wordIntent);
                mainActivity.showVocab();
            }
        }
    };

        public ItemTouchHelper.SimpleCallback simpleCallbackFunction(){
            return simpleCallback;
        }
}
