package com.adg.MIN3;

public class DataPoint {
    private int day;
    private int sickToday;
    private int cost;
    private int totalSick;

    public DataPoint(int day, int sickToday, int cost, int totalSick) {
        this.day = day;
        this.sickToday = sickToday;
        this.cost = cost;
        this.totalSick = totalSick;
    }
}
