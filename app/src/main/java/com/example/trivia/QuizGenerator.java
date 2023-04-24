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

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
        RANDOM,
    }

    public enum Topic {
        HISTORY,
        SCIENCE,
        LITERATURE,
        GEOGRAPHY,
        ART,
        SPORTS,
        MUSIC,
        MOVIES,
        TV_SHOWS,
        RANDOM
    };

    public enum AnswerKeyDictionary {
        ENGLISH("Answer"),
        SPANISH("Respuesta"),
        FRENCH("Réponse"),
        PORTUGUESE("Resposta");

        private final String value;

        AnswerKeyDictionary(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AnswerKeyDictionary getKey(Global.LANGUAGES language) {
            for (AnswerKeyDictionary key : AnswerKeyDictionary.values()) {
                if (key.name().equalsIgnoreCase(language.toString().toUpperCase())) {
                    return key;
                }
            }
            return null;
        }
    }

    public enum QuestionKeyDictionary {
        ENGLISH("Question"),
        SPANISH("Pregunta"),
        FRENCH("Demander"),
        PORTUGUESE("Questão");

        private final String value;

        QuestionKeyDictionary(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static QuestionKeyDictionary getKey(Global.LANGUAGES language) {
            for (QuestionKeyDictionary key : QuestionKeyDictionary.values()) {
                if (key.name().equalsIgnoreCase(language.toString().toUpperCase())) {
                    return key;
                }
            }
            return null;
        }
    };

    public static String generatePrompt(Difficulty difficulty, Topic topic, Global.LANGUAGES language) {
        String level = "Difficulty: " + (difficulty.equals(Difficulty.RANDOM) ? QuizGenerator.getAleatoryDifficulty() : difficulty.toString().toLowerCase());
        String subject = "Topic: " + (topic.equals(Topic.RANDOM) ? QuizGenerator.getAleatoryTopic() : topic.toString().toLowerCase()) ;
        String idiom = "Language: " + language.toString().toLowerCase();
        String responseStructure = "In your response, use the following structure: Question: Question. A) Option. B) Option. C) Option. Answer: Answer. Please make sure the provide the whole response using the selected language.";
        return String.format("Generate a random question about general culture and provide three possible answer options classified with the letters A, B, and C, being only one the correct answer, finally provide the letter of the correct option. %s %s, %s. \n" + responseStructure, idiom, subject, level);
    }

    public static QuizQuestion generateQuizQuestion(Difficulty difficulty, Topic topic, Global.LANGUAGES language) {

        // Customization
        String prompt = generatePrompt(difficulty, topic, language);

        String apiKey = System.getenv("OPENAI_API_KEY_2");
        String model = "text-curie:001"; // text-curie:001 | text-davinci-003
        int maxTokens = 200;
        int optionsNumber = 1; // number of answer options
        int temperature = 0;

        if (apiKey == null || apiKey.isEmpty()) {
            throw new Error("No OPEN AI Key provided");
        }

        System.out.println("apiKey => " + apiKey);

//        String response = openAiLibrary(apiKey, prompt, model, maxTokens, optionsNumber);
//        String response = openAiRequest(apiKey, prompt, model, maxTokens, temperature);
        String response = cohereAiRequest(apiKey, prompt, maxTokens);

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

    private static String cohereAiRequest(String apiKey, String prompt, int maxTokens) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("prompt", prompt);
            jsonBody.put("truncate", "END");
            jsonBody.put("max_tokens", maxTokens);
            jsonBody.put("return_likelihoods", "NONE");
        } catch (JSONException error) {
            error.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON) ;
        Request request = new Request.Builder()
                .url("https://api.cohere.ai/v1/generate")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        final String[] result = new String[1];
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                System.out.println("onFailure => " + e.getMessage());

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
                    jsonArray = jsonObject.getJSONArray("generations");
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
