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
    private char selectedOption;
    private boolean isCorrect;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Find elements
        radioGroup_options = findViewById(R.id.radioGroup_options);
        textView_question = findViewById(R.id.textView_question);
        radioButton_optionA = findViewById(R.id.radioButton_optionA);
        radioButton_optionB = findViewById(R.id.radioButton_optionB);
        radioButton_optionC = findViewById(R.id.radioButton_optionC);
        btn_send = findViewById(R.id.button_sendAnswer);

        // Set Values & Methods
        btn_send.setEnabled(false);

        createQuiz();
    }

    private void createQuiz() {

        Global app = (Global) getApplication();

        // Customization
        QuizGenerator.Difficulty selectedDifficulty = app.getDifficulty();
        QuizGenerator.Topic selectedTopic = app.getTopic();
        Global.LANGUAGES language = app.getLanguage();

        String prompt = QuizGenerator.generatePrompt(selectedDifficulty, selectedTopic, language);
        String apiKey = "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"; // System.getenv("OPENAI_API_KEY");
        int maxTokens = 200;

        System.out.println("apiKey => " + apiKey);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("prompt", prompt);
            jsonBody.put("model", "text-davinci-003");

            jsonBody.put("max_tokens", maxTokens);
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

                    String questionKey = QuizGenerator.QuestionKeyDictionary.getKey(language).getValue() + ":";
                    boolean isQuestionKeyOnResponse = (result.indexOf(questionKey) == -1 ? false : true);
                    String finalQuestionKey = isQuestionKeyOnResponse ? questionKey : QuizGenerator.QuestionKeyDictionary.ENGLISH.getValue() + ":";

                    String answerKey = QuizGenerator.AnswerKeyDictionary.getKey(language).getValue() + ":";
                    boolean isAnswerKeyOnResponse = (result.indexOf(answerKey) == -1 ? false : true);
                    String finalAnswerKey = isAnswerKeyOnResponse ? answerKey : QuizGenerator.AnswerKeyDictionary.ENGLISH.getValue() + ":";

                    String question = result.substring(result.indexOf(finalQuestionKey) + finalQuestionKey.length() + 1, result.indexOf("A)")).replace("\n", "");
                    System.out.println("question => " + question);

                    // Extract answer options
                    String optionA = result.substring(result.indexOf("A)"), result.indexOf("B)")).replace("\n", "");
                    String optionB = result.substring(result.indexOf("B)"), result.indexOf("C)")).replace("\n", "");
                    String optionC = result.substring(result.indexOf("C)"), result.indexOf(finalAnswerKey)).replace("\n", "");

                    System.out.println("optionA => " + optionA);
                    System.out.println("optionB => " + optionB);
                    System.out.println("optionC => " + optionC);

                    // Extract correct answer option
                    char answer = result.charAt(result.indexOf(finalAnswerKey) + finalAnswerKey.length() + 1);

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