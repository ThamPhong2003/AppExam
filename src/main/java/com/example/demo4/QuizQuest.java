package com.example.demo4;

public class QuizQuest {
    private final int quizID;
    private final int QuestionID;

    public QuizQuest(int quizID, int QuestionID) {
        this.quizID = quizID;
        this.QuestionID = QuestionID;
    }

    public int getQuizId() {
        return quizID;
    }
    public int getQuestionId() {
        return QuestionID;
    }
}

