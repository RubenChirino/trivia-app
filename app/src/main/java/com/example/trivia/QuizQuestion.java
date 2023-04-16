package com.example.trivia;

public class QuizQuestion {
    private final String question;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final char correctOption;

    public QuizQuestion(String question, String optionA, String optionB, String optionC, char correctOption) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.correctOption = correctOption;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public char getCorrectOption() {
        return correctOption;
    }
}
