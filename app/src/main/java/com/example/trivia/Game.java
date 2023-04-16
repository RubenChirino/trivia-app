package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioButton;

public class Game extends AppCompatActivity {

    // Elements
    private TextView textView_question;
    private RadioGroup radioGroup_options;
    private RadioButton radioButton_optionA;
    private RadioButton radioButton_optionB;
    private RadioButton radioButton_optionC;
    private Button btn_send;

    // Values
    private QuizQuestion val_quiz;
    private char selectedOption;
    private boolean isCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

//        val_quiz = QuizGenerator.generateQuizQuestion(QuizGenerator.getAleatoryDifficulty(), QuizGenerator.getAleatoryTopic());
//
//        System.out.println("val_quiz => " + val_quiz);
//        System.out.println("");
//        System.out.println("Option A => " + val_quiz.getOptionA());
//        System.out.println("Option B => " + val_quiz.getOptionB());
//        System.out.println("Option C => " + val_quiz.getOptionC());

        // Find elements
        radioGroup_options = findViewById(R.id.radioGroup_options);
        textView_question = findViewById(R.id.textView_question);
        radioButton_optionA = findViewById(R.id.radioButton_optionA);
        radioButton_optionB = findViewById(R.id.radioButton_optionB);
        radioButton_optionC = findViewById(R.id.radioButton_optionC);
        btn_send = findViewById(R.id.button_sendAnswer);

        // Set Values & Methods
        btn_send.setEnabled(false);

//        textView_question.setText(val_quiz.getQuestion());
//        radioButton_optionA.setText(val_quiz.getOptionA());
//        radioButton_optionB.setText(val_quiz.getOptionB());
//        radioButton_optionC.setText(val_quiz.getOptionC());

        radioGroup_options.setOnCheckedChangeListener((group, checkedId) -> {
            if (!btn_send.isEnabled()) {
                btn_send.setEnabled(true);
            }
            RadioButton selectedRadioButton = findViewById(checkedId);
            String text = selectedRadioButton.getText().toString();
            selectedOption = text.charAt(text.indexOf(")") - 1);
        });

        btn_send.setOnClickListener(view -> {
            isCorrect = Character.toLowerCase(selectedOption) == Character.toLowerCase(val_quiz.getCorrectOption());

            Intent intent = new Intent(Game.this, AnswerResult.class);
            intent.putExtra("win", isCorrect);
            startActivity(intent);
        });

        // Generate quiz question in a thread
        new Thread(() -> {
            val_quiz = QuizGenerator.generateQuizQuestion(QuizGenerator.getAleatoryDifficulty(), QuizGenerator.getAleatoryTopic());

            // Update UI in main thread
            new Handler(Looper.getMainLooper()).post(() -> {

                System.out.println("val_quiz => " + val_quiz);
                System.out.println("");
                System.out.println("Option A => " + val_quiz.getOptionA());
                System.out.println("Option B => " + val_quiz.getOptionB());
                System.out.println("Option C => " + val_quiz.getOptionC());

                // Set question and options
                textView_question.setText(val_quiz.getQuestion());
                radioButton_optionA.setText(val_quiz.getOptionA());
                radioButton_optionB.setText(val_quiz.getOptionB());
                radioButton_optionC.setText(val_quiz.getOptionC());
            });
        }).start();
    }
}