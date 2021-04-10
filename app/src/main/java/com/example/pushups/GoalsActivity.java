package com.example.pushups;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class GoalsActivity extends AppCompatActivity {

    TextView goalTV;
    int goalValue;
    ArrayList<TrainingDay> trainingDays;
    TrainingDay lastTrainingDay;
    TextView progressPercentTV;
    TextView progressRatioTV;
    TextView passTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        goalTV = findViewById(R.id.goalTV);
        progressPercentTV = findViewById(R.id.progressTV);
        progressRatioTV = findViewById(R.id.progressRatioTV);
        passTV = findViewById(R.id.passTV);

        trainingDays = TrainingDay.loadTrainingDays(this);
        lastTrainingDay = trainingDays.get(trainingDays.size() - 1);
        loadGoal();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TrainingDay.saveTrainingDays(this, trainingDays);
    }

    public void close(View view) {
        finish();
    }

    public void decreaseGoal(View view) {
        goalValue -= 5;
        setGoalUpdatePass();
    }

    public void increaseGoal(View view) {
        goalValue += 5;
        setGoalUpdatePass();
    }

    private void loadGoal(){
        goalValue = lastTrainingDay.getGoal();
        setGoalUpdatePass();
    }

    private void setGoalUpdatePass(){
        goalTV.setText(Integer.toString(goalValue));
        lastTrainingDay.setGoal(goalValue);
        setProgressTV();
        updatePass();
    }

    private void setProgressTV(){
        progressPercentTV.setText(lastTrainingDay.getProgressPercent() + "%");
        progressRatioTV.setText(lastTrainingDay.getProgress() + "/" + lastTrainingDay.getGoal());
    }

    private void updatePass(){
        int pass = calculatePass();
        passTV.setText(Integer.toString(pass));
    }

    private int calculatePass(){
        int pass = 0;
        for(int i=trainingDays.size()-1; i>=0; i--){
            if(trainingDays.get(i).getProgressPercent() >= 100)
                pass++;
            else
                break;
        }
        return pass;
    }
}