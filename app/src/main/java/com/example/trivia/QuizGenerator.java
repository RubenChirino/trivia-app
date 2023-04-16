package com.example.trivia;

import androidx.annotation.NonNull;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QuizGenerator {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    static OkHttpClient client = new OkHttpClient();

    enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
    }

    enum Topic {
        HISTORY,
        SCIENCE,
        LITERATURE,
        GEOGRAPHY,
        ART,
        SPORTS,
        MUSIC,
        MOVIES,
        TV_SHOWS,
        OTHER
    }

    public static QuizQuestion generateQuizQuestion(Difficulty difficulty, Topic topic) {

        // Customization
        String level = "Difficulty: " + difficulty.toString().toLowerCase();
        String subject = "Topic: " + topic.toString().toLowerCase();

        String prompt = String.format("Generate a random question about general culture and three answer options (classified with the letters A, B, and C, being only one the correct answer), and the letter of the correct option. %s, %s", subject, level);
        String apiKey = "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"; // System.getenv("OPENAI_API_KEY")
        String model = "text-curie:001"; // text-curie:001 | text-davinci-003
        int maxTokens = 200;
        int optionsNumber = 1; // number of answer options
        int temperature = 0;

        if (apiKey == null || apiKey.isEmpty()) {
            throw new Error("No OPEN AI Key provided");
        }

//        String response = openAiLibrary(apiKey, prompt, model, maxTokens, optionsNumber);
        String response = openAiRequest(apiKey, prompt, model, maxTokens, temperature);

        System.out.println("response (OPEN AI) => " + response);

//        String question = response.substring(response.indexOf("Question:") + 9, response.indexOf("A)"));
//
//        // Extract answer options
//        String optionA = response.substring(response.indexOf("A)") + 2, response.indexOf("B)"));
//        String optionB = response.substring(response.indexOf("B)") + 2, response.indexOf("C)"));
//        String optionC = response.substring(response.indexOf("C)") + 2, response.indexOf("Correct answer"));
//
//        // Extract correct answer option
//        char answer = response.charAt(response.indexOf("Correct answer:") + 15 + 2);

        return new QuizQuestion("Test", "Test A", "Test B", "Test C", 'A'); // new QuizQuestion(question, optionA, optionB, optionC, answer);
    }

    public static Topic getAleatoryTopic() {
        Topic[] topics = Topic.values();
        int randomIndex = (int) (Math.random() * topics.length);
        return topics[randomIndex];
    }

    public static Difficulty getAleatoryDifficulty() {
        Difficulty[] difficulties = Difficulty.values();
        int randomIndex = (int) (Math.random() * difficulties.length);
        return difficulties[randomIndex];
    }

    private static String openAiLibrary(String apiKey, String prompt, String model, int maxTokens, int optionsNumber) {
        OpenAiService service = new OpenAiService(apiKey);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .maxTokens(maxTokens)
                .n(optionsNumber)
                .stop(Collections.singletonList("\n"))
                .model(model)
                .echo(true)
                .build();
        CompletionResult completion = service.createCompletion(completionRequest);
        return completion.getChoices().get(0).getText();
    }

    private static String openAiRequest(String apiKey, String prompt, String model, int maxTokens, int temperature) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("prompt", prompt);
            jsonBody.put("model", model);
            jsonBody.put("max_tokens", maxTokens);
            jsonBody.put("temperature", temperature);
        } catch (JSONException error) {
            error.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON) ;
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        final String[] result = new String[1];
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                result[0] = e.getMessage();
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
                    jsonArray = jsonObject.getJSONArray("choices");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("jsonArray => " + jsonArray);

                try {
                    result[0] = jsonArray.getJSONObject(0).getString("text");

                    System.out.println("THEN => " + result[0]);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return result[0];
    }
}
