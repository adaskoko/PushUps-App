package com.example.pushups;

public class PushUpSet {

    private int count;
    private long startTime, endTime;
    private String type;

    public PushUpSet(String type, long startTime){
        this.startTime = startTime;
        this.type = type;
    }

    @Override
    public String toString(){
        return "Count: " + count + " Start time: " + startTime + " End time: " + endTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
