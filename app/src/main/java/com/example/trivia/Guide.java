package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

public class Guide extends AppCompatActivity {

    Intent intent = getIntent();

    // Elements
    TextView textView_welcome;
    Button btn_play;

    // Values
    String val_name = intent.getStringExtra("name");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // Find elements
        btn_play = findViewById(R.id.btn_play);
        textView_welcome = findViewById(R.id.textView_rule1);

        // Set Values & Methods
        // extView_welcome.setText(R.string.text_greeting + "" + val_name + R.string.text_presentation + ":");
        textView_welcome.setText(getString(R.string.welcome_message, getString(R.string.text_greeting), val_name, getString(R.string.text_presentation)));


        btn_play.setOnClickListener(view -> {
            Intent intent = new Intent(Guide.this, Game.class);
            startActivity(intent);
        });
    }
}