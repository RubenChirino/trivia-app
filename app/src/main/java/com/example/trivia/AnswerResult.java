package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AnswerResult extends AppCompatActivity {

    // Elements
    TextView textView_answerResultTitle;
    Button button_playAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_result);

        // Find elements
        textView_answerResultTitle = findViewById(R.id.textView_answerResultTitle);

        // Get the intent and extract the name
        Intent intent = getIntent();
        boolean isCorrect = intent.getBooleanExtra("isCorrect", false);
        System.out.println("isCorrect => " + "isCorrect");

        // Set Values & Methods
        textView_answerResultTitle.setText(isCorrect ? "Correct ✅" : "Incorrect ❌");


        // Find elements
        button_playAgain = findViewById(R.id.button_playAgain);

        // Set Values & Methods
        button_playAgain.setOnClickListener(view -> {
            Intent next = new Intent(AnswerResult.this, Game.class);
            startActivity(next);
        });

    }
}