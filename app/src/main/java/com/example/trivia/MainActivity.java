package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    // Elements
    Button btn_selectName;
    TextInputEditText textInput_name;

    // Values
    String val_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find elements
        btn_selectName = findViewById(R.id.btn_selectName);
        textInput_name = findViewById(R.id.textInput_name);

        // Set Values & Methods
        btn_selectName.setEnabled(false);
        btn_selectName.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Guide.class);
            intent.putExtra("name", val_name);
            startActivity(intent);
        });

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
    }
}