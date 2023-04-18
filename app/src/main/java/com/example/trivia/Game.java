package com.example.trivia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Game extends AppCompatActivity {

    // Elements
    private TextView textView_question;
    private RadioGroup radioGroup_options;
    private RadioButton radioButton_optionA;
    private RadioButton radioButton_optionB;
    private RadioButton radioButton_optionC;
    private Button btn_send;

    // Values
    private String val_quiz;
    private char selectedOption;
    private boolean isCorrect;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();

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

//        btn_send.setOnClickListener(view -> {
//            isCorrect = Character.toLowerCase(selectedOption) == Character.toLowerCase(val_quiz.getCorrectOption());
//
//            Intent intent = new Intent(Game.this, AnswerResult.class);
//            intent.putExtra("win", isCorrect);
//            startActivity(intent);
//        });

        // Generate quiz question in a thread
//        new Thread(() -> {
//            val_quiz = QuizGenerator.generateQuizQuestion(QuizGenerator.getAleatoryDifficulty(), QuizGenerator.getAleatoryTopic());
//
//            // Update UI in main thread
//            new Handler(Looper.getMainLooper()).post(() -> {
//
//                System.out.println("val_quiz => " + val_quiz);
//                System.out.println("");
//                System.out.println("Option A => " + val_quiz.getOptionA());
//                System.out.println("Option B => " + val_quiz.getOptionB());
//                System.out.println("Option C => " + val_quiz.getOptionC());
//
//                // Set question and options
//                textView_question.setText(val_quiz.getQuestion());
//                radioButton_optionA.setText(val_quiz.getOptionA());
//                radioButton_optionB.setText(val_quiz.getOptionB());
//                radioButton_optionC.setText(val_quiz.getOptionC());
//            });
//        }).start();

        someStupidMethod();
    }

    private void someStupidMethod() {
        // Customization
        String level = "Difficulty: " + QuizGenerator.getAleatoryDifficulty().toString().toLowerCase();
        String subject = "Topic: " + QuizGenerator.getAleatoryTopic().toString().toLowerCase();

        String prompt = String.format("Generate a random question about general culture and provide three possible answer options classified with the letters A, B, and C, being only one the correct answer, finally provide the letter of the correct option. %s, %s." +
                "Use the following structure: Q: Some question. A) Some option. B) Some option. C) Some option. Answer: Some answer.", subject, level);
        String apiKey = System.getenv("OPENAI_API_KEY");
        int maxTokens = 200;

        System.out.println("apiKey => " + apiKey);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("prompt", prompt);
//            jsonBody.put("truncate", "END");
            jsonBody.put("model", "text-davinci-003");

            jsonBody.put("max_tokens", maxTokens);
//            jsonBody.put("return_likelihoods", "NONE");
        } catch (JSONException error) {
            error.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON) ;
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions") // https://api.cohere.ai/v1/generate |
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("onFailure => " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                System.out.println("onResponse => " + response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("jsonObject => " + jsonObject);

                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONArray("choices"); // choices | generations
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("jsonArray => " + jsonArray);

                try {
                    String result = jsonArray.getJSONObject(0).getString("text");
                    System.out.println("result => " + result);

                    String question = result.substring(result.indexOf("Q:") + 3, result.indexOf("A)")).replace("\n", "");
                    System.out.println("question => " + question);

                    // Extract answer options
                    String optionA = result.substring(result.indexOf("A)"), result.indexOf("B)")).replace("\n", "");
                    String optionB = result.substring(result.indexOf("B)"), result.indexOf("C)")).replace("\n", "");
                    String optionC = result.substring(result.indexOf("C)"), result.indexOf("Answer:")).replace("\n", "");

                    System.out.println("optionA => " + optionA);
                    System.out.println("optionB => " + optionB);
                    System.out.println("optionC => " + optionC);

                    // Extract correct answer option
                    char answer = result.charAt(result.indexOf("Answer:") + 7 + 1);

                    System.out.println("answer => " + answer);

                    textView_question.setText(question);
                    radioButton_optionA.setText(optionA);
                    radioButton_optionB.setText(optionB);
                    radioButton_optionC.setText(optionC);

                    radioGroup_options.setOnCheckedChangeListener((group, checkedId) -> {
                        if (!btn_send.isEnabled()) {
                            btn_send.setEnabled(true);
                        }
                        RadioButton selectedRadioButton = findViewById(checkedId);
                        String text = selectedRadioButton.getText().toString();
                        selectedOption = text.charAt(text.indexOf(")") - 1);
                    });

                    btn_send.setOnClickListener(view -> {
                        isCorrect = Character.toLowerCase(selectedOption) == Character.toLowerCase(answer);

                        Intent intent = new Intent(Game.this, AnswerResult.class);
                        intent.putExtra("isCorrect", isCorrect);
                        startActivity(intent);
                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}