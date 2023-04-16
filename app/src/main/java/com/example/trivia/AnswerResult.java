package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AnswerResult extends AppCompatActivity {

    Intent intent = getIntent();

    // Elements
    TextView textView_answerResultTitle;

    // Values
    boolean val_win = true; // intent.getBooleanExtra("win")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_result);

        // Find elements
        textView_answerResultTitle = findViewById(R.id.textView_answerResultTitle);

        // Set Values & Methods
        textView_answerResultTitle.setText(val_win ? "Correct ✅" : "Incorrect ❌");

    }
}