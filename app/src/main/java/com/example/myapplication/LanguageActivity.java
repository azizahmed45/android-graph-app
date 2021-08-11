package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.myapplication.GraphFragment.LANGUAGE_LOCALE;
import static com.example.myapplication.GraphFragment.LANGUAGE_PREFERENCE;

public class LanguageActivity extends AppCompatActivity {

    ListView languageListView;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        SharedPreferences sharedPreferences = getSharedPreferences(LANGUAGE_PREFERENCE, MODE_PRIVATE);

        setTitle("Language");

        languageListView = findViewById(R.id.languageList);

        List<String> languageShortList = Arrays.asList(
                Language.ENGLISH,
                Language.SPANISH,
                Language.GERMAN,
                Language.CHINESE,
                Language.JAPANESE,
                Language.PORTUGUESE,
                Language.FRENCH
        );

        List<String> languageList = Arrays.asList(
                "English",
                "Spanish",
                "German",
                "Chinese",
                "Japanese",
                "Portuguese",
                "French"
        );





        Map<String, Drawable> flagList = new HashMap<>();
        flagList.put(Language.ENGLISH, getResources().getDrawable(R.drawable.united));
        flagList.put(Language.SPANISH, getResources().getDrawable(R.drawable.spain));
        flagList.put(Language.GERMAN, getResources().getDrawable(R.drawable.germany));
        flagList.put(Language.CHINESE, getResources().getDrawable(R.drawable.china));
        flagList.put(Language.JAPANESE, getResources().getDrawable(R.drawable.japan));
        flagList.put(Language.PORTUGUESE, getResources().getDrawable(R.drawable.portugal));
        flagList.put(Language.FRENCH, getResources().getDrawable(R.drawable.france));



        LanguageListAdapter languageListAdapter = new LanguageListAdapter(this, R.layout.language_single_view,languageList, languageShortList, flagList);


        languageListView.setAdapter(languageListAdapter);

        languageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences.edit().putString(LANGUAGE_LOCALE, languageShortList.get(position)).commit();
                languageListAdapter.setSelected(sharedPreferences.getString(LANGUAGE_LOCALE, Language.ENGLISH));
            }
        });

        languageListAdapter.setSelected(sharedPreferences.getString(LANGUAGE_LOCALE, Language.ENGLISH));

    }
}