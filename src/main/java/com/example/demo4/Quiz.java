package com.example.demo4;

public class Quiz {
    private  int id;
    private  String name;
    private  double time;
    private  boolean shuffle;
    private  double  totalmark;

    public Quiz(int id, String name, double time, boolean shuffle, double totalmark) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.shuffle = shuffle;
        this.totalmark = totalmark;
    }
    public void setId(int id) {
        this.id = id;
    }

    /*public int getId() {
        return id;
    }
    */

    public int getId() {
        return this.id;
    }
    public String getName() {
        return name;
    }

    public double time() {
        return time;
    }

    public  boolean getShuffle() {
        return shuffle ;
    }
    public double getTotalmark() {
        return totalmark;
    }


    @Override
    public String toString() {
        return name;
    }
}