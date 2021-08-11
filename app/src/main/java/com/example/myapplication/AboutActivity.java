package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView aboutTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        aboutTextView = findViewById(R.id.aboutText);

        setTitle("About");

        String aboutText = getResources().getString(R.string.about_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutTextView.setText(Html.fromHtml(aboutText, Html.FROM_HTML_MODE_COMPACT));
        } else {
            aboutTextView.setText(Html.fromHtml(aboutText));
        }
    }
}