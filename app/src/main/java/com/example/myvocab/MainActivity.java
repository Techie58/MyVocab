package com.example.myvocab;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.example.myvocab.Utilites.StackView;
import com.example.myvocab.Utilites.StackViewService;
import com.example.myvocab.Utilites.SwipeFile;
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


        SwipeFile swipeFile=new SwipeFile(this,arrVocab);

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
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(swipeFile.simpleCallbackFunction());
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
            updateWidget();
        }else Toast.makeText(this, "Please Add Any Word", Toast.LENGTH_SHORT).show();
    }
    private void initShowDialog(){
        Dialog dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.add_vocab_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditText edtWord=dialog.findViewById(R.id.etdWord);
        Button edtAddButton=dialog.findViewById(R.id.edtAddBtn);

        edtAddButton.setOnClickListener(view -> {
            String Word=edtWord.getText().toString();
            if (!Word.equals("")){
                vocabDBHelper.vocabDao().addVocab(new VocabModel(Word));
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
        Button edtAddButton=dialog.findViewById(R.id.edtAddBtn);
        edtAddButton.setText("Update Vocab");

        edtWord.setText(arrVocab.get(position).getWord());

        edtAddButton.setOnClickListener(view -> {
            String Word=edtWord.getText().toString();
            if (!Word.equals("")){
                VocabDBHelper.getInstance(this).vocabDao().updateVocab(new VocabModel(Word,arrVocab.get(position).getId()));
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                showVocab();
                dialog.dismiss();

            }else Toast.makeText(this, "Please Enter Any Word", Toast.LENGTH_SHORT).show();
        });
    }
    public void updateWidget() {
        Toast.makeText(this, "Update method is working", Toast.LENGTH_SHORT).show();
        // Get the AppWidgetManager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        // Get the ComponentName for your StackView widget
        ComponentName componentName = new ComponentName(this, StackView.class);

        // Get the AppWidgetIds that are active for your StackView widget
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        StackView stackView = new StackView();

        // Update the StackView widget
        for (int appWidgetId : appWidgetIds) {
            // Update the data by calling the RemoteViewsService's onDataSetChanged
            Intent serviceIntent = new Intent(this, StackViewService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stackView);

            // Call updateAppWidget to refresh the widget
            StackView.updateAppWidget(this, appWidgetManager, appWidgetId, serviceIntent);


        }
    }
}