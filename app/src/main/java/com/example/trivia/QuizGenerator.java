package com.example.trivia;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import java.util.Collections;

public class QuizGenerator {

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

        String prompt = String.format("Generate a random question about general culture and three answer options (classified with the letters A, B, and C, being only one the correct answer), and the letter of the correct option. %s %s", subject, level);
        String apiKey = System.getenv("OPENAI_API_KEY");
        String engine = "text-curie:001"; // text-curie:001 | text-davinci-003
        int maxTokens = 100;
        int optionsNumber = 1; // number of answer options

        if (apiKey == null || apiKey.isEmpty()) {
            throw new Error("No OPEN AI Key provided");
        }

        OpenAiService service = new OpenAiService(apiKey);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .maxTokens(maxTokens)
                .n(optionsNumber)
                .stop(Collections.singletonList("\n"))
                .model(engine)
                .echo(true)
                .build();

        String response = service.createCompletion(completionRequest).getChoices().get(0).getText();
        String question = response.substring(response.indexOf("Question:") + 9, response.indexOf("A)"));

        // Extract answer options
        String optionA = response.substring(response.indexOf("A)") + 2, response.indexOf("B)"));
        String optionB = response.substring(response.indexOf("B)") + 2, response.indexOf("C)"));
        String optionC = response.substring(response.indexOf("C)") + 2, response.indexOf("Correct answer"));

        // Extract correct answer option
        char answer = response.charAt(response.indexOf("Correct answer:") + 15 + 2);

        return new QuizQuestion(question, optionA, optionB, optionC, answer);
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
}
