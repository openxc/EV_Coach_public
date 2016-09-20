package com.openxc.openxcstarter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.openxcplatform.openxcstarter.R;

import java.text.DecimalFormat;


public class CoachActivity extends Activity {

    private double rpmScore;
    private double speedScore;
    private double mpgScore;
    private double totalScore;
    private double accelScore;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coach_activity);
        DecimalFormat formatter = new DecimalFormat("#0.00");


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Bundle bundle = getIntent().getExtras();
        rpmScore = bundle.getDouble("rpmScore");
        speedScore = bundle.getDouble("speedScore");
        mpgScore = bundle.getDouble("mpgScore");
        accelScore = bundle.getDouble("accelScore");
        totalScore = rpmScore + speedScore + mpgScore + accelScore;

        Log.i("CoachActivity", "RPM Score " + rpmScore);
        Log.i("CoachActivity", "Speed Score " + speedScore);
        Log.i("CoachActivity", "MPG Score " + mpgScore);
        Log.i("CoachActivity", "Accel Score " + accelScore);


        String rpmGrade = calculateGrade(rpmScore * 4);
        String speedGrade = calculateGrade(speedScore * 4);
        String mpgGrade = calculateGrade(mpgScore * 4);
        String accelGrade = calculateGrade(accelScore * 4);
        String totalGrade = calculateGrade(totalScore);

        double rpmPercent = 100 * (rpmScore / 250.);
        double speedPercent = 100 * (speedScore / 250.);
        double mpgPercent = 100 * (mpgScore / 250.);
        double accelPercent = 100 * (accelScore / 250.);
        double totalPercent = 100 * (totalScore / 1000.);

        //Find the TextViews
        TextView totalScoreView = (TextView) findViewById(R.id.coach_totalScore);
        TextView totalGradeView = (TextView) findViewById(R.id.coach_grade);
        TextView rpmScoreView = (TextView) findViewById(R.id.coach_rpmScore);
        TextView rpmGradeView = (TextView) findViewById(R.id.coach_rpmGrade);
        TextView rpmMessageView = (TextView) findViewById(R.id.coach_rpmContents);
        TextView speedScoreView = (TextView) findViewById(R.id.coach_speedScore);
        TextView speedGradeView = (TextView) findViewById(R.id.coach_speedGrade);
        TextView speedMessageView = (TextView) findViewById(R.id.coach_speedContents);
        TextView mpgScoreView = (TextView) findViewById(R.id.coach_gasScore);
        TextView mpgGradeView = (TextView) findViewById(R.id.coach_gasGrade);
        TextView mpgMessageView = (TextView) findViewById(R.id.coach_gasContents);
        TextView accelScoreView = (TextView) findViewById(R.id.coach_accelScore);
        TextView accelGradeView = (TextView) findViewById(R.id.coach_accelGrade);
        TextView accelMessageView = (TextView) findViewById(R.id.coach_accelContents);

        //Update the TextViews
        totalScoreView.append(totalGrade + " ");
        totalGradeView.setText(formatter.format(totalPercent) + "%");
        rpmScoreView.append(rpmGrade + " ");
        rpmGradeView.setText(formatter.format(rpmPercent) + "%");
        speedScoreView.append(speedGrade + " ");
        speedGradeView.setText(formatter.format(speedPercent) + "%");
        mpgScoreView.append(mpgGrade + " ");
        mpgGradeView.setText(formatter.format(mpgPercent) + "%");
        accelScoreView.append(accelGrade + " ");
        accelGradeView.setText(formatter.format(accelPercent) + "%");


        //Display messages
        //TODO - Move these into the strings.xml file
        String rpmMessage = "Try to not accelerate as rapidly when driving.";
        String speedMessage;
        String mpgMessage = "Make sure you have a fully charged battery, and to keep your speed and RPMs low";
        String accelMessage = "Try to not accelerate as rapidly when driving.";

        if(Integer.parseInt(sharedPreferences.getString("road", "0")) == 2) {
            speedMessage = "Try slowing down when traveling on the highway.";
            rpmMessage = "Try using cruise control when traveling on the highway.";
        } else if(Integer.parseInt(sharedPreferences.getString("road", "0")) == 1) {
            speedMessage = "Try slowing down when traveling on rural roads.";
        } else {
            speedMessage = "Try slowing down when traveling through the city.";
        }

        if(rpmPercent < 80.0) {
            rpmMessageView.setText(rpmMessage);
        } else {
            rpmMessageView.setText("Good job!");
        }

        if(mpgPercent < 80.0) {
            mpgMessageView.setText(mpgMessage);
        } else {
            mpgMessageView.setText("Good job!");
        }

        if(speedPercent < 80.0) {
            speedMessageView.setText(speedMessage);
        } else {
            speedMessageView.setText("Good job!");
        }

        if(accelPercent < 80.0) {
            accelMessageView.setText(accelMessage);
        } else {
            accelMessageView.setText("Good job!");
        }
    }


    private String calculateGrade(double score) {

        if(score < 500) {
            return "F";
        } else if(score < 600) {
            return "E";
        } else if(score < 625) {
            return "D-";
        } else if(score < 675) {
            return "D";
        } else if(score < 700) {
            return "D+";
        } else if(score < 725) {
            return "C-";
        } else if(score < 775) {
            return "C";
        } else if(score < 800) {
            return "C+";
        } else if(score < 825) {
            return "B-";
        } else if(score < 875) {
            return "B";
        } else if(score < 900) {
            return "B+";
        } else if(score < 925) {
            return "A-";
        } else if(score < 975) {
            return "A";
        } else {
            return "A+";
        }
    }
}
