package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AnswerResult extends AppCompatActivity {

    // Elements
    TextView textView_answerResultTitle;
    TextView textView_score;
    Button button_playAgain;
    Button button_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_result);

        Global app = (Global) getApplication();
        int score = app.getScore();

        // Find elements
        textView_answerResultTitle = findViewById(R.id.textView_answerResultTitle);
        textView_score = findViewById(R.id.textView_score);

        // Get the intent and extract the name
        Intent intent = getIntent();
        boolean isCorrect = intent.getBooleanExtra("isCorrect", false);

        // Set Values & Methods
        if (isCorrect) {
            app.setScore(score+=1);
            textView_answerResultTitle.setText("Correct ✅");

        } else {
            app.setScore(0);
            textView_answerResultTitle.setText("Incorrect ❌");
        }

        textView_score.setText("Your Score: " + score);

        // Find elements
        button_playAgain = findViewById(R.id.button_playAgain);
        button_home = findViewById(R.id.button_home);

        // Set Values & Methods
        button_playAgain.setOnClickListener(view -> {
            Intent next = new Intent(AnswerResult.this, Game.class);
            startActivity(next);
        });

        button_home.setOnClickListener(view -> {
            Intent next = new Intent(AnswerResult.this, MainActivity.class);
            startActivity(next);
        });

    }
}