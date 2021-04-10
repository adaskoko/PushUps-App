package com.example.pushups;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Collections;

public class StatisticsActivity extends AppCompatActivity {

    CombinedChart chart;
    ArrayList<TrainingDay> lastTrainingDays;
    ArrayList<TrainingDay> trainingDays;
    ListView dayStatisticsLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        chart = findViewById(R.id.chart);
        dayStatisticsLV = findViewById(R.id.dayStatisticsLV);

        trainingDays = TrainingDay.loadTrainingDays(this);
        setLastTrainingDays();

        setChartData();

        createLastTrainingDaysAdapter();
    }

    public void close(View view) {
        finish();
    }

    private void setChartData(){
        ArrayList<BarEntry> progressesPercents = new ArrayList<>();
        for(int i=0; i<lastTrainingDays.size(); i++) {
            progressesPercents.add(new BarEntry(i, lastTrainingDays.get(i).getProgressPercent()));
        }

        ArrayList<String> dates = new ArrayList<>();
        for(TrainingDay day: lastTrainingDays) {
            dates.add(day.getDate());
        }

        BarDataSet barDataSet = new BarDataSet(progressesPercents, "Progress %");
        barDataSet.setColor(Color.parseColor("#ffffff"));
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barData.setDrawValues(false);
        barData.setBarWidth(0.5f);

        ArrayList<Entry> lineDataList = new ArrayList<>();
        for(int i=-1; i<=7; i++){
            lineDataList.add(new Entry(i, 100));
        }

        LineDataSet lineDataSet = new LineDataSet(lineDataList, "100%");
        lineDataSet.setColor(Color.parseColor("#00ff00"));
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        lineDataSet.setDrawCircles(false);
        lineData.setDrawValues(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setTextColor(Color.parseColor("#ffffff"));
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(-60);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setAxisMinimum(-1);
        xAxis.setAxisMaximum(7);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.parseColor("#ffffff"));
        leftAxis.setAxisMinimum(0);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        Description description = chart.getDescription();
        description.setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        CombinedData data = new CombinedData();
        data.setData(barData);
        data.setData(lineData);

        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false);
        chart.setData(data);
        chart.animateY(1000);
    }

    private void setLastTrainingDays(){
        lastTrainingDays = new ArrayList<>();
        for(int i=0; i<7; i++){
            lastTrainingDays.add(trainingDays.get(trainingDays.size() - i - 1));
        }
        Collections.reverse(lastTrainingDays);
    }

    private void createLastTrainingDaysAdapter(){
        ArrayAdapter<TrainingDay> trainingDayAdapter =
                new ArrayAdapter<TrainingDay>(this, 0, lastTrainingDays){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        TrainingDay currentTrainingDay = lastTrainingDays.get(lastTrainingDays.size() - position - 1);

                        if(convertView == null){
                            convertView = getLayoutInflater().inflate(R.layout.day_statistics_layout, null, false);
                        }
                        TextView dateTV = convertView.findViewById(R.id.dateTV);
                        TextView progressPercentTV = convertView.findViewById(R.id.progressPercentTV);
                        TextView progressRatioTV = convertView.findViewById(R.id.progressRatioTV);

                        dateTV.setText(currentTrainingDay.getDate());
                        progressPercentTV.setText(currentTrainingDay.getProgressPercent() + "%");
                        progressRatioTV.setText(currentTrainingDay.getProgress() + " z celu " + currentTrainingDay.getGoal());

                        return convertView;
                    }
                };

        dayStatisticsLV.setAdapter(trainingDayAdapter);
    }
}