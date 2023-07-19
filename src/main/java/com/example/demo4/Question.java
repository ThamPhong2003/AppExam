package com.example.demo4;

public class Question {
    private final int id;
    private final int CateGoryID;
    private final String text;
    private final String name;
    private final float Mark;
    private boolean deleted; // Thuộc tính để đánh dấu câu hỏi đã bị xóa


    public Question(int id, int CateGoryID, String text, String name, float Mark) {
        this.id = id;
        this.CateGoryID = CateGoryID;
        this.text = text;
        this.name = name;
        this.Mark = Mark;
        this.deleted = false;

    }
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    public int getId() {
        return id;
    }
    public int getCateGoryID() {
        return CateGoryID;
    }
    public String getText() {
        return text;
    }
    public String getName() {
        return name;
    }
    public float getMark() {
        return Mark;
    }

    @Override
    public String toString() {
        return text;
    }
}