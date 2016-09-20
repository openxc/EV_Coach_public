package com.openxc.openxcstarter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.openxcplatform.openxcstarter.R;

public class BreakdownActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakdown_layout);

        //TODO Add fuel score, change hard-coded values
        Bundle extras = getIntent().getExtras();
        String speedScore = extras.getString("SpeedScore");
        String RPMscore = extras.getString("RPMScore");
        String accelScore = extras.getString("AccScore");
        String totalScore = extras.getString("totalScore");
        String MPGScore = extras.getString("MPGScore");
        String Grade = extras.getString("grade");

        //rest of the code
        TextView accelScoreText = (TextView) findViewById(R.id.acceleration);
        accelScoreText.setText(accelScore + " / 250.00");

        TextView RPMScore = (TextView) findViewById(R.id.rpm);
        RPMScore.setText(RPMscore + " / 250.00");

        TextView speed = (TextView) findViewById(R.id.speed);
        speed.setText(speedScore + " / 250.00");

        TextView mpg = (TextView) findViewById(R.id.mpg);
        mpg.setText(MPGScore + " / 250.00");

        TextView grade = (TextView) findViewById(R.id.grade);
        grade.setText(Grade);

        TextView score = (TextView) findViewById(R.id.score);
        score.setText(totalScore + " / 1000.00");
    }
}