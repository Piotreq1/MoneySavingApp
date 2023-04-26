package com.example.moneysavingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AchievementsActivity extends AppCompatActivity {
    //deklaracja zmiennych, widoku listy osiągnięć i listy osiągnieć
    ListView achievementsList;
    private ArrayList<String> mAchievements = new ArrayList<>();
    String goal, daysTo, moneyVal;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.achievements_activity);
        achievementsList = findViewById(R.id.lv_achievements);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mAchievements);
        achievementsList.setAdapter(adapter);

        // pobieranie danych z innej aktywności
        Intent intent1 = getIntent();
        if (intent1.getExtras() != null) {
            daysTo = intent1.getStringExtra("daysTo");
            goal = intent1.getStringExtra("goal");
            moneyVal = intent1.getStringExtra("moneyVal");
            ArrayList<String> achievements = (ArrayList<String>) intent1.getSerializableExtra("mAchievements");

            if (achievements != null) {
                // odświeżanie listy osiągnięć
                mAchievements.addAll(achievements);
                adapter.notifyDataSetChanged();
            }
        }

    }


    @Override
    public void onBackPressed() {   //obsługa przycisku Wstecz
        //przeslanie odpowiednich wartosci do MainActivity
        Intent intent = new Intent(AchievementsActivity.this, MainActivity.class);
        intent.putExtra("source", "achievements");
        intent.putExtra("goal", goal);
        intent.putExtra("daysTo", daysTo);
        intent.putExtra("moneyVal", moneyVal);
        intent.putExtra("mAchievements", mAchievements);
        startActivity(intent);
        finish();
    }

}
