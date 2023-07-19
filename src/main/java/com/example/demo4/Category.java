package com.example.demo4;

public class Category {
    private final int id;
    private final int parentId;
    private final String info;
    private final String name;

    public Category(int id, int parentId, String info, String name) {
        this.id = id;
        this.parentId = parentId;
        this.info = info;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}