package com.example.pushups;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;

public class Counter {

    boolean inProgress;
    boolean away;
    int count;
    TextView counterTV;
    TextView typeET;
    PushUpSet set;
    Date date;
    Context context;
    ArrayList<TrainingDay> trainingDays;
    MediaPlayer tickPlayer;

    public Counter(TextView counterTV, EditText typeET, Context context){
        inProgress = false;
        away = false;
        count = 0;
        this.counterTV = counterTV;
        this.typeET = typeET;
        this.context = context;
        trainingDays = TrainingDay.loadTrainingDays(context);
        tickPlayer = MediaPlayer.create(context, R.raw.tick_sound);
    }

    public void start(){
        inProgress = true;
        count = 0;
        setCounter();
        date = new Date();
        set = new PushUpSet(typeET.getText().toString(), date.getTime());
    }

    public void stop(){
        inProgress = false;
        set.setCount(count);
        set.setEndTime(date.getTime());
        saveSet();
    }

    public void sensorNear(){
        if (away && inProgress){
            away = false;
            increaseCounter();
            setCounter();
            tickPlayer.start();
        }
    }

    public void sensorAway(){
        if (!away && inProgress){
            away = true;
        }
    }

    private void increaseCounter(){
        count++;
    }

    private void setCounter(){
        counterTV.setText(Integer.toString(count));
    }

    public void close(){
        if (inProgress){
            stop();
        }
    }

    public void saveSet(){
        addSetToTrainingDay();
        saveTrainingDays();
    }

    private void addSetToTrainingDay() {
        TrainingDay day = trainingDays.get(trainingDays.size()-1);
        day.addSet(set);
    }

    private void saveTrainingDays(){
        TrainingDay.saveTrainingDays(context, trainingDays);
    }
}
