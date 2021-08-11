package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class GraphFragment extends Fragment {

    Button multiplyButton;
    Button clearButton;

    EditText xValueField;
    EditText yValueField;

    TextView productView;

    ImageButton voiceXButton;
    ImageButton voiceYButton;

    Graph.SliderMode sliderMode;

    final int voiceXRequest = 1000;
    final int voiceYRequest = 2000;

    Graph graph;

    public static String LANGUAGE_PREFERENCE = "LANGUAGE";
    public static String LANGUAGE_LOCALE = "LOCALE";

    public  GraphFragment(){

    }

    public GraphFragment(Graph.SliderMode sliderMode){
        this.sliderMode = sliderMode;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        graph = view.findViewById(R.id.graph);
        graph.setSliderMode(sliderMode);

        xValueField = view.findViewById(R.id.xValue);
        yValueField = view.findViewById(R.id.yValue);

        multiplyButton = view.findViewById(R.id.multiplyButton);
        clearButton = view.findViewById(R.id.clearButton);

        productView = view.findViewById(R.id.product);

        voiceXButton = view.findViewById(R.id.voiceX);

        voiceYButton = view.findViewById(R.id.voiceY);

        multiplyButton.setOnClickListener(v -> {

            try {
                int x = Integer.parseInt(xValueField.getText().toString());
                int y = Integer.parseInt(yValueField.getText().toString());

                graph.setAxis(x, y);

            } catch (Exception e) {
                Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
            }


        });


        clearButton.setOnClickListener(v -> {
            graph.setAxis(0, 0);
        });


        graph.setAxisDataChangeListener((x, y) -> {
            xValueField.setText(String.valueOf(x));
            yValueField.setText(String.valueOf(y));

            productView.setText(String.valueOf(x * y));
        });


        voiceXButton.setOnClickListener(v -> {
            try {
                startActivityForResult(getVoiceIntent(), voiceXRequest);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        voiceYButton.setOnClickListener(v -> {
            try {
                startActivityForResult(getVoiceIntent(), voiceYRequest);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @SuppressLint("QueryPermissionsNeeded")
    private Intent getVoiceIntent() throws Exception {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LANGUAGE_PREFERENCE, MODE_PRIVATE);
        Locale locale;


        String localeShort = sharedPreferences.getString(LANGUAGE_LOCALE, Language.ENGLISH);

        switch (localeShort) {
            case Language.SPANISH:
                locale = new Locale("es", "");
                break;
            case Language.GERMAN:
                locale = Locale.GERMAN;
                break;
            case Language.CHINESE:
                locale = Locale.CHINESE;
                break;
            case Language.JAPANESE:
                locale = Locale.JAPANESE;
                break;
            case Language.PORTUGUESE:
                locale = new Locale("pt", "");
                break;
            case Language.FRENCH:
                locale = Locale.FRENCH;
                break;
            default:
                locale = Locale.ENGLISH;
        }


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        PackageManager packageManager = getContext().getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale);

            return intent;
        } else {
            throw new Exception("Google app not installed.");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            try {
                List<String> voiceData = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if (voiceData.size() > 0) {

                    int number = Integer.parseInt(voiceData.get(0));

                    if (requestCode == voiceXRequest) {
                        if(number > 0 && number <= 10 ){
                            xValueField.setText(String.valueOf(number));
                        } else {
                            Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                        }

                    } else if (requestCode == voiceYRequest) {
                        if(number > 0 && number <= 15 ){
                            yValueField.setText(String.valueOf(number));
                        } else {
                            Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(getContext(), "Failed to get number input.", Toast.LENGTH_SHORT).show();
                }
            }catch (NumberFormatException e){
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }



        } else {
            Toast.makeText(getContext(), "Failed to get number input.", Toast.LENGTH_SHORT).show();
        }
    }
}
