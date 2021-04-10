package com.example.pushups;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class TrainingDay {

    private int goal;
    private ArrayList<PushUpSet> setsList;
    private String date;
    private long dateTime;
    private int progress;
    private static int lastGoal = 50;
    private static final int DAY_IN_MILLIS = 1000 * 60 * 60 * 24;


    public TrainingDay(){
        setsList = new ArrayList<>();
        dateTime = new Date().getTime();
        date = (String) DateFormat.format("dd/MM/yyyy", dateTime);
        goal = lastGoal;
        progress = 0;
    }

    public static ArrayList<TrainingDay> loadTrainingDays(Context context){
        ArrayList<TrainingDay> trainingDays = new ArrayList<>();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String jsonGet = prefs.getString("trainingDays", null);
        Type type = new TypeToken<ArrayList<TrainingDay>>() {}.getType();
        if (gson.fromJson(jsonGet, type) != null)
            trainingDays = gson.fromJson(jsonGet, type);

        TrainingDay day;
        if (trainingDays.size() != 0) {
            day = trainingDays.get(trainingDays.size() - 1);
            if (!day.isToday()) {
                trainingDays.add(new TrainingDay());
            }
        }else
            trainingDays.add(new TrainingDay());

        return fillMissingDays(trainingDays);
    }

    public static void saveTrainingDays(Context context, ArrayList<TrainingDay> trainingDays){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String jsonSet = gson.toJson(trainingDays);
        editor.putString("trainingDays", jsonSet);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public static ArrayList<TrainingDay> fillMissingDays(ArrayList<TrainingDay> trainingDays){
        int size = trainingDays.size();
        TrainingDay today = trainingDays.get(size - 1);

        if(size > 1){
            TrainingDay secondLastDay = trainingDays.get(trainingDays.size() - 2);
            while(!isYesterday(secondLastDay, today)){
                long time = secondLastDay.getDateTime() + DAY_IN_MILLIS;

                secondLastDay = new TrainingDay();
                secondLastDay.setDateTime(time);
                trainingDays.add(size - 2, secondLastDay);

                size++;
            }
        }

        if(size < 7){
            TrainingDay firstDay = trainingDays.get(0);
            long time = firstDay.getDateTime() - DAY_IN_MILLIS;
            while(size < 7){
                TrainingDay day = new TrainingDay();
                day.setDateTime(time);
                trainingDays.add(0, day);

                time -= DAY_IN_MILLIS;
                size ++;
            }
        }

        return trainingDays;
    }

    private static boolean isYesterday(TrainingDay yesterday, TrainingDay today){
        return ((String) DateFormat.format("dd/MM/yyyy", yesterday.getDateTime())).equals(
                (String) DateFormat.format("dd/MM/yyyy", today.getDateTime() - DAY_IN_MILLIS));
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long time){
        dateTime = time;
        date = (String) DateFormat.format("dd/MM/yyyy", dateTime);
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
        lastGoal = goal;
    }

    public ArrayList<PushUpSet> getSetsList() {
        return setsList;
    }

    public void addSet(PushUpSet set) {
        setsList.add(set);
        progress += set.getCount();
    }

    public boolean isToday(){
        return date.equals((String) DateFormat.format("dd/MM/yyyy", new Date().getTime()));
    }

    public int getProgressPercent() {
        return 100 * progress / goal;
    }

    public int getProgress() {
        return progress;
    }

    public String getDate() {
        return date;
    }
}
