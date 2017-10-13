package com.ford.ev_coach;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BreakdownActivity extends AppCompatActivity implements View.OnClickListener {


    private Button loginButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakdown_activity);

        //TODO Add fuel score, change hard-coded values
        Bundle extras = getIntent().getExtras();
        String speedScore = extras.getString("SpeedScore");
        String RPMscore = extras.getString("RPMScore");
        String accelScore = extras.getString("AccScore");
        String totalScore = extras.getString("totalScore");
        String MPGScore = extras.getString("MPGScore");
        String Grade = extras.getString("grade");
        loginButton = (Button) findViewById(R.id.loginButton);

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

    @Override
    public void onClick(View view) {
        if (view == loginButton) {
            Intent intent = new Intent(BreakdownActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}