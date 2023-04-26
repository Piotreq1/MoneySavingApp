package com.example.moneysavingapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.util.ArrayList;

public class DepositActivity extends AppCompatActivity {
    TextView moneyValTextView;
    EditText moneyEditText, accountEditText;
    Button addDepositBtn;
    String goal, daysTo, moneyValOld;
    ArrayList<String> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.deposit_activity);

        // przypisanie obiektow
        moneyValTextView = findViewById(R.id.moneyValTextView);
        moneyEditText = findViewById(R.id.moneyEditText);
        accountEditText = findViewById(R.id.accountEditText);
        addDepositBtn = findViewById(R.id.addDepositBtn);

        // pobranie wartości przesłanych z MainActivity
        Intent intent1 = getIntent();
        if (intent1.getExtras() != null) {
            goal = intent1.getStringExtra("goal");
            daysTo = intent1.getStringExtra("daysTo");
            moneyValOld = intent1.getStringExtra("moneyVal");
            achievements = (ArrayList<String>) intent1.getSerializableExtra("mAchievements");
            moneyValTextView.setText(moneyValOld);
        }

        //wydarzenie kliknięcia przycisku
        addDepositBtn.setOnClickListener(view -> {
            //pobranie kwoty podanej przez uzytkownika, sprawdzenie czy nie jest pusta i czy jest poprawna liczba
            String moneyVal = moneyEditText.getText().toString();
            if (moneyVal.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.pusta_kwota), Toast.LENGTH_SHORT).show();
            } else {
                try {
                    float val = Float.parseFloat(moneyVal);
                    if (val <= 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.kwota_zero), Toast.LENGTH_SHORT).show();
                    } else {
                        //dodanie kwoty do poprzedniego bilansu oraz odeslanie wartosci do MainActivity
                        if (!moneyValOld.equals("")) {
                            val += Float.parseFloat(moneyValOld);
                        }
                        moneyValTextView.setText(String.valueOf(val));

                        Intent intent = new Intent(DepositActivity.this, MainActivity.class);
                        intent.putExtra("source", "deposit");
                        intent.putExtra("moneyVal", String.valueOf(val));
                        intent.putExtra("mAchievements", achievements);
                        intent.putExtra("goal", goal);
                        intent.putExtra("daysTo", daysTo);
                        startActivity(intent);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Nieprawidłowy format kwoty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
