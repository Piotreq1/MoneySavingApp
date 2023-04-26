package com.example.moneysavingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView goalTextView, daysToTextView, moneyPerDayTextView, moneyValTextView1;
    Button setGoal, deposit, exit, achievements;
    ProgressBar progressBar;
    String date;
    ArrayList<String> mAchievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //ustawienie sceny
        setContentView(R.layout.activity_main);

        //wyszyszczenie celu, osiagniec itp po resetowaniu aplikacji
        // -- odkomentowac i uruchomic zeby wyczyscic
        /*
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        */

        // przypisanie obiektów do obiektów layoutu
        setGoal = findViewById(R.id.setGoalBtn);
        deposit = findViewById(R.id.depositBtn);
        exit = findViewById(R.id.button_exit);
        achievements = findViewById(R.id.achievements);
        goalTextView = findViewById(R.id.goalTextView);
        daysToTextView = findViewById(R.id.daysToTextView);
        moneyPerDayTextView = findViewById(R.id.moneyPerDayTextView);
        moneyValTextView1 = findViewById(R.id.moneyValTextView1);
        progressBar = findViewById(R.id.progress_bar);

        setGoal.setOnClickListener(this);
        deposit.setOnClickListener(this);
        exit.setOnClickListener(this);
        achievements.setOnClickListener(this);

        retrieveSharedValues();

        handleIntent();
    }

    private void handleIntent() {
        // zapisywanie wartosci po zmianach okein
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String from = intent.getStringExtra("source");
            if ("deposit".equals(from)) {
                // po powrocie z okna Depozytu
                String daysTo = intent.getStringExtra("daysTo");
                String goalS = intent.getStringExtra("goal");
                String moneyValS = intent.getStringExtra("moneyVal");
                mAchievements = (ArrayList<String>) intent.getSerializableExtra("mAchievements");
                setTextViewsValues(daysTo, goalS, moneyValS);

            } else if ("setgoal".equals(from)) {
                // po powrocie z okna Ustawiania celu
                String daysTo = intent.getStringExtra("daysTo");
                String goalS = intent.getStringExtra("goal");
                String moneyValS = intent.getStringExtra("moneyVal");
                mAchievements = (ArrayList<String>) intent.getSerializableExtra("mAchievements");
                setTextViewsValues(daysTo, goalS, moneyValS);
            } else if ("achievements".equals(from)) {
                // po powrocie z okna Osiagniec
                String daysTo = intent.getStringExtra("daysTo");
                String goalS = intent.getStringExtra("goal");
                String moneyValS = intent.getStringExtra("moneyVal");
                mAchievements = (ArrayList<String>) intent.getSerializableExtra("mAchievements");
                setTextViewsValues(daysTo, goalS, moneyValS);
            }
        }
    }

    private void setTextViewsValues(String daysTo, String goalS, String moneyValS) {
        // sprawdzenie czy dane nie sa puste
        if (daysTo == null || goalS == null || moneyValS == null) {
            return;
        }
        if (daysTo.equals("") || goalS.equals("") || moneyValS.equals("")) {
            return;
        }
        int days = calculateDaysBetween(daysTo);
        if (days != 0) {
            // ustawienie wartosci w widoku, obliczenie wplacanej kwoty na dzien,
            // liczby dni do konca, ustawienie progress bar
            float goal = Float.parseFloat(goalS);
            float moneyVal = Float.parseFloat(moneyValS);
            date = daysTo;
            daysToTextView.setText(String.valueOf(days));
            goalTextView.setText(String.valueOf(goal));
            moneyValTextView1.setText(String.valueOf(moneyVal));

            float perDay = (goal - moneyVal) / days;
            progressBar.setProgress((int) ((moneyVal / goal) * 100));
            if (perDay <= 0) {
                //osiagniecie zalozonego celu - powiadomienie, zresetowanie wartosci na widokach
                //dodanie osiagniecia
                Toast.makeText(getApplicationContext(), "Brawo! Udało Ci się osiągnąć cel!", Toast.LENGTH_LONG).show();

                if (mAchievements == null) {
                    mAchievements = new ArrayList<>();
                }
                mAchievements.add("Data końcowa: " + date + "  " + "Cel:" + goal + " Status: Osiagniety!");

                daysToTextView.setText("0");
                goalTextView.setText("0");
                moneyValTextView1.setText(String.valueOf(moneyVal - goal));
                moneyPerDayTextView.setText("0");
                progressBar.setProgress(100);
                date = "0";

                return;
            }
            String perDayFormatted = String.format("%.2f", perDay);
            moneyPerDayTextView.setText(perDayFormatted);
        }
    }

    private int calculateDaysBetween(String daysTo) {
        // obliczenie liczby dni pomiedzy zalożoną datą końcową a aktualną datą
        Calendar today = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date yourDate = null;
        try {
            yourDate = dateFormat.parse(daysTo);
        } catch (ParseException e) {
            return 0;
        }
        Calendar yourCalendar = Calendar.getInstance();
        if (yourDate != null) {
            yourCalendar.setTime(yourDate);
            long differenceInMillis = yourCalendar.getTimeInMillis() - today.getTimeInMillis();
            return (int) (differenceInMillis / (1000 * 60 * 60 * 24));
        }
        return 0;
    }

    private void retrieveSharedValues() {
        // pobranie wartosci i ich ustawienie po wyjsciu i ponownym wejsciu do aplikacji
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String goal = sharedPref.getString("goal", "");
        String daysTo = sharedPref.getString("daysTo", "");
        String moneyVal = sharedPref.getString("moneyVal", "");

        String json = sharedPref.getString("mAchievements", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            mAchievements = gson.fromJson(json, type);
        } else {
            mAchievements = new ArrayList<>();
        }
        setTextViewsValues(daysTo, goal, moneyVal);
    }


    @Override
    public void onClick(View v) {
        // Wydarzenia klikniecia przyciskow
        switch (v.getId()) {
            case R.id.setGoalBtn:
                // przeniesienie do widoku Ustawiania Celu wraz z przeslaniem wartosci
                Intent intent = new Intent(this, SetGoalActivity.class);
                intent.putExtra("moneyVal", moneyValTextView1.getText().toString());
                intent.putExtra("mAchievements", mAchievements);
                startActivity(intent);
                break;
            case R.id.depositBtn:
                // przeniesienie do widoku Depozytu wraz z przeslaniem wartosci
                intent = new Intent(this, DepositActivity.class);
                intent.putExtra("goal", goalTextView.getText().toString());
                intent.putExtra("daysTo", date);
                intent.putExtra("moneyVal", moneyValTextView1.getText().toString());
                intent.putExtra("mAchievements", mAchievements);
                startActivity(intent);
                break;
            case R.id.achievements:
                // przeniesienie do widoku Osiagniec wraz z przeslaniem wartosci
                intent = new Intent(this, AchievementsActivity.class);
                intent.putExtra("goal", goalTextView.getText().toString());
                intent.putExtra("daysTo", date);
                intent.putExtra("moneyVal", moneyValTextView1.getText().toString());
                intent.putExtra("mAchievements", mAchievements);
                startActivity(intent);
            case R.id.button_exit:
                // Wyjscie z aplikacji oraz zapisanie wartosci, aby byly dostepne po ponownym uruchomieniu
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("goal", goalTextView.getText().toString());
                editor.putString("daysTo", date);
                editor.putString("moneyVal", moneyValTextView1.getText().toString());

                Gson gson = new Gson();
                String json = gson.toJson(mAchievements);
                editor.putString("mAchievements", json);
                editor.apply();
                finishAffinity();
                break;
        }
    }

}