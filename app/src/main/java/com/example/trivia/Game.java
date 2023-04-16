package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioButton;

public class Game extends AppCompatActivity {

    // Elements
    TextView textView_question;
    RadioGroup radioGroup_options;
    RadioButton radioButton_optionA;
    RadioButton radioButton_optionB;
    RadioButton radioButton_optionC;
    Button btn_send;

    // Values
    QuizQuestion val_quiz = QuizGenerator.generateQuizQuestion(QuizGenerator.getAleatoryDifficulty(), QuizGenerator.getAleatoryTopic());
    char selectedOption;
    boolean isCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        System.out.println("val_quiz => " + val_quiz);
        System.out.println("");
        System.out.println("Option A => " + val_quiz.getOptionA());
        System.out.println("Option B => " + val_quiz.getOptionB());
        System.out.println("Option C => " + val_quiz.getOptionC());

        // Find elements
        radioGroup_options = findViewById(R.id.radioGroup_options);
        textView_question = findViewById(R.id.textView_question);
        radioButton_optionA = findViewById(R.id.radioButton_optionA);
        radioButton_optionB = findViewById(R.id.radioButton_optionB);
        radioButton_optionC = findViewById(R.id.radioButton_optionC);
        btn_send = findViewById(R.id.button_sendAnswer);

        // Set Values & Methods
        textView_question.setText(val_quiz.getQuestion());
        radioButton_optionA.setText(val_quiz.getOptionA());
        radioButton_optionB.setText(val_quiz.getOptionB());
        radioButton_optionC.setText(val_quiz.getOptionC());

        btn_send.setEnabled(false);

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
    }
}