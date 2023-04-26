package com.example.moneysavingapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText login,password;
    Button log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //ustawienie sceny
        setContentView(R.layout.login_activity);

        login = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        log = findViewById(R.id.loginButton);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login1 = login.getText().toString();
                String password1 = password.getText().toString();

                if(login1.equals("1") && password1.equals("12345")){
                    // wprowadzono poprawny login i haslo
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),getString(R.string.logowanie),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
