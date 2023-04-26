package com.example.moneysavingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SetGoalActivity extends AppCompatActivity {

    EditText goalEditText, dateEditText;
    Button addGoalBtn;
    String moneyVal;
    ArrayList<String> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.set_goal_activity);
        //przypisanie obiektow
        goalEditText = findViewById(R.id.goalEditText);
        dateEditText = findViewById(R.id.dateEditText);
        addGoalBtn = findViewById(R.id.addGoalBtn);

        // pobranie wartości przesłanych z MainActivity
        Intent intent1 = getIntent();
        if (intent1.getExtras() != null) {
            moneyVal = intent1.getStringExtra("moneyVal");
            achievements = (ArrayList<String>) intent1.getSerializableExtra("mAchievements");
        }

        addGoalBtn.setOnClickListener(view -> {
            // pobranie wartosci wprowadzonych przez uzytkownika (celu i daty koncowej)
            String goalText = goalEditText.getText().toString();
            String dateText = dateEditText.getText().toString();
            // sprawdzenie czy data jest poprawna
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = dateFormat.parse(dateText);

                Float abc = Float.parseFloat(goalText);

                // odeslanie wartosci
                Intent intent = new Intent(SetGoalActivity.this, MainActivity.class);
                intent.putExtra("source", "setgoal");
                intent.putExtra("goal", goalText);
                intent.putExtra("daysTo", dateText);
                intent.putExtra("moneyVal", moneyVal);
                intent.putExtra("mAchievements", achievements);
                startActivity(intent);

            } catch (ParseException | NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Niepoprawny format danych!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
