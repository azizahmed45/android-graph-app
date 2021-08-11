package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

public class LanguageListAdapter extends ArrayAdapter {

    Context context;
    List<String> languageShortList;
    List<String> languageList;
    Map<String, Drawable> flags;

    String selectedShortLanguage = Language.ENGLISH;

    public LanguageListAdapter(@NonNull Context context, int resource, List<String> languageList, List<String> languageShortList, Map<String, Drawable> flags) {
        super(context, resource);
        this.context = context;
        this.languageShortList = languageShortList;
        this.languageList = languageList;
        this.flags = flags;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(context).inflate(R.layout.language_single_view, null, false);

        ImageView flagView = view.findViewById(R.id.flag);
        TextView languageView = view.findViewById(R.id.language);
        ImageView selectedView = view.findViewById(R.id.selected);

        flagView.setImageDrawable(flags.get(languageShortList.get(position)));
        languageView.setText(languageList.get(position));

        if(!languageShortList.get(position).equals(selectedShortLanguage)){
            selectedView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public void setSelected(String selectedShortLanguage){
        this.selectedShortLanguage = selectedShortLanguage;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return languageShortList.size();
    }
}
