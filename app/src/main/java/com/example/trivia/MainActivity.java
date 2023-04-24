package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // Elements
    Button btn_selectName;
    TextInputEditText textInput_name;
    Spinner spinner_languages;
    Spinner spinner_topic;
    Spinner spinner_difficulty;

    // Values
    String val_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Global app = (Global) getApplication();

        // Find elements
        btn_selectName = findViewById(R.id.btn_selectName);
        textInput_name = findViewById(R.id.textInput_name);
        spinner_languages = findViewById(R.id.spinner_languages);
        spinner_topic = findViewById(R.id.spinner_topic);
        spinner_difficulty = findViewById(R.id.spinner_difficulty);

        // == Set Values & Methods ==
        btn_selectName.setEnabled(false);

        // Username Input
        textInput_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                val_name = textInput_name.getText().toString();
            }

            public void afterTextChanged(Editable s) {
                btn_selectName.setEnabled(!textInput_name.getText().toString().isEmpty());
            }
        });

        // Language Dropdown
        ArrayAdapter<Global.LANGUAGES> languagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Global.LANGUAGES.values());
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_languages.setAdapter(languagesAdapter);
        spinner_languages.setSelection(Arrays.asList(Global.LANGUAGES.values()).indexOf(Global.LANGUAGES.ENGLISH));

        spinner_languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Global.LANGUAGES selectedLanguage = (Global.LANGUAGES) parent.getItemAtPosition(position);
                app.setLanguage(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btn_selectName.setEnabled(false);
            }
        });

        // Topic Dropdown
        ArrayAdapter<QuizGenerator.Topic> topicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, QuizGenerator.Topic.values());
        topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_topic.setAdapter(topicAdapter);
        spinner_topic.setSelection(Arrays.asList(QuizGenerator.Topic.values()).indexOf(QuizGenerator.Topic.RANDOM));

        spinner_topic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QuizGenerator.Topic selectedTopic = (QuizGenerator.Topic) parent.getItemAtPosition(position);
                app.setTopic(selectedTopic);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btn_selectName.setEnabled(false);
            }
        });

        // Difficulty Dropdown
        ArrayAdapter<QuizGenerator.Difficulty> difficultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, QuizGenerator.Difficulty.values());
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_difficulty.setAdapter(difficultyAdapter);
        spinner_difficulty.setSelection(Arrays.asList(QuizGenerator.Difficulty.values()).indexOf(QuizGenerator.Difficulty.RANDOM));

        spinner_difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                QuizGenerator.Difficulty selectedDifficulty = (QuizGenerator.Difficulty) parent.getItemAtPosition(position);
                app.setDifficulty(selectedDifficulty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btn_selectName.setEnabled(false);
            }
        });

        // Select Button
        btn_selectName.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Guide.class);
            intent.putExtra("name", val_name);
            startActivity(intent);
        });

    }
}