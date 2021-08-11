package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {

    LinearLayout languageButton;
    LinearLayout aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setTitle("Configuration");

        languageButton = findViewById(R.id.languageButton);
        aboutButton = findViewById(R.id.aboutButton);


        languageButton.setOnClickListener( v -> {
            startActivity(new Intent(this, LanguageActivity.class));
        });

        aboutButton.setOnClickListener( v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });
    }
}