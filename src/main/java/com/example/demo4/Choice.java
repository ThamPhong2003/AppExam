package com.example.demo4;

public class Choice {
    private final int id;
    private final int questionId;
    private final float grade;
    private final String text;

    public Choice (int id, int questionId, String text, float grade) {
        this.id = id;
        this.questionId = questionId;
        this.grade = grade;
        this.text = text;
    }


    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public float getGrade() {
        return grade;
    }

    public String getText() {
        return text;
    }


    @Override
    public String toString() {
        return text;
    }
}