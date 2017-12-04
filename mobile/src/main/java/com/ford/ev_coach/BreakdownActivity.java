package com.ford.ev_coach;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class BreakdownActivity extends AppCompatActivity implements View.OnClickListener {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    static DatabaseReference ref = database.getReference("users/" + user.getUid() + "/");
    static DatabaseReference scoreRef = ref.child("scores");
    static Map<String, String> scores = new HashMap<>();
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


        scores.put("MPG", MPGScore);
        scores.put("Accel Score", accelScore);
        scores.put("RPM Score", RPMscore);
        scores.put("Speed Score", speedScore);
        scores.put("Grade", Grade);
        scores.put("Total Score", totalScore);
    }

    @Override
    public void onClick(View view) {
        if (view == loginButton) {
            Intent intent = new Intent(BreakdownActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}