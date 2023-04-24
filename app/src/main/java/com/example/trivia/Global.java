package com.example.trivia;

import android.app.Application;

public class Global extends Application {
    private String userName;
    private QuizGenerator.Difficulty difficulty;
    private QuizGenerator.Topic topic;

    public enum LANGUAGES {
        ENGLISH,
        SPANISH,
        FRENCH,
        PORTUGUESE
    };

    private LANGUAGES language;

    private int score = 0;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public QuizGenerator.Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(QuizGenerator.Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public QuizGenerator.Topic getTopic() {
        return topic;
    }

    public void setTopic(QuizGenerator.Topic topic) {
        this.topic = topic;
    }

    public LANGUAGES getLanguage() {
        return language;
    }

    public void setLanguage(LANGUAGES language) {
        this.language = language;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
