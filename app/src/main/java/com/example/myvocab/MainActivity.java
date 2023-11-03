package com.example.myvocab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myvocab.adapter.RVAdapter;
import com.example.myvocab.database.VocabDBHelper;
import com.example.myvocab.database.VocabModel;
import com.example.myvocab.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    VocabDBHelper vocabDBHelper;
    ActivityMainBinding binding;
    RVAdapter adapter;
    ArrayList<VocabModel> arrVocab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showVocab();
        binding.searchWord.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });


        binding.addBtn.setOnClickListener(view -> initShowDialog());
        //Swipe
        ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position=viewHolder.getAdapterPosition();
                if (direction==ItemTouchHelper.LEFT){
                    AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Delete").setMessage("Do you wanna delete this Word")
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                vocabDBHelper.vocabDao().deleteVocab(new VocabModel(arrVocab.get(position).getWord(),arrVocab.get(position).getMeaning(),arrVocab.get(position).getId()));
                                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                showVocab();
                            }).setNegativeButton("No", (dialogInterface, i) -> {
                                showVocab();
                                Toast.makeText(MainActivity.this, "Swipe Right To Refresh App", Toast.LENGTH_SHORT).show();
                            }).show();
                }else {
                    Intent wordIntent=new Intent(MainActivity.this,MeaningActivity.class);
                    wordIntent.putExtra("WORD",arrVocab.get(position).getWord());
                    startActivity(wordIntent);
                    showVocab();
                }
            }
        };
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView2);

    }

    private void filterList(String newText) {
        ArrayList<VocabModel> fList=new ArrayList<>();
        for (VocabModel item : arrVocab){
            if (item.getWord().toLowerCase().contains(newText.toLowerCase())){
                fList.add(item);}
        }if (fList.isEmpty()){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }else {
            adapter.setFilterList(fList);
        }
    }

    public void showVocab(){
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        vocabDBHelper=VocabDBHelper.getInstance(this);
        arrVocab=(ArrayList<VocabModel>) vocabDBHelper.vocabDao().getAll();
        if (arrVocab.size()>0) {
            adapter = new RVAdapter(this, arrVocab);
            binding.recyclerView2.setAdapter(adapter);
        }else Toast.makeText(this, "Please Add Any Word", Toast.LENGTH_SHORT).show();
    }
    private void initShowDialog(){
        Dialog dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.add_vocab_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditText edtWord=dialog.findViewById(R.id.etdWord);
        EditText edtMeaning=dialog.findViewById(R.id.edtMeaning);
        Button edtAddButton=dialog.findViewById(R.id.edtAddBtn);

        edtAddButton.setOnClickListener(view -> {
            String Word=edtWord.getText().toString();
            String Meaning=edtMeaning.getText().toString();
            if (!Word.equals("")){
                vocabDBHelper.vocabDao().addVocab(new VocabModel(Word,Meaning));
                Toast.makeText(this, "Saved in DataBase", Toast.LENGTH_SHORT).show();
                showVocab();
                dialog.dismiss();

            }else Toast.makeText(this, "Please Enter Any Word", Toast.LENGTH_SHORT).show();
        });
    }
    private void updateVocab(int position) {
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.add_vocab_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        EditText edtWord=dialog.findViewById(R.id.etdWord);
        EditText edtMeaning=dialog.findViewById(R.id.edtMeaning);
        Button edtAddButton=dialog.findViewById(R.id.edtAddBtn);
        edtAddButton.setText("Update Vocab");

        edtWord.setText(arrVocab.get(position).getWord());
        edtMeaning.setText(arrVocab.get(position).getMeaning());

        edtAddButton.setOnClickListener(view -> {
            String Word=edtWord.getText().toString();
            String Meaning=edtMeaning.getText().toString();
            if (!Word.equals("")){
                VocabDBHelper.getInstance(this).vocabDao().updateVocab(new VocabModel(Word,Meaning,arrVocab.get(position).getId()));
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                showVocab();
                dialog.dismiss();

            }else Toast.makeText(this, "Please Enter Any Word", Toast.LENGTH_SHORT).show();
        });
    }
}