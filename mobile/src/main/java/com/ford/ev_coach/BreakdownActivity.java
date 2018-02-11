package com.ford.ev_coach;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class BreakdownActivity extends AppCompatActivity implements View.OnClickListener {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    static DatabaseReference ref = database.getReference("users/" + user.getUid() + "/");
    static DatabaseReference fbScoreObject = ref.child("Score");
    static DatabaseReference FBspeedScore = ref.child("speedScore");
    static DatabaseReference FBRPMScore = ref.child("RPMScore");
    static DatabaseReference FBaccelScore = ref.child("accelScore");
    static DatabaseReference FBtotalScore = ref.child("totalScore");
    static DatabaseReference FBMPGScore = ref.child("MPGScore");
    static DatabaseReference FBGrade = ref.child("Grade");

    private Button loginButton;

    private static String prevSpeedScore;
    private static String prevRPMscore;
    private static String prevAccelScore;
    private static String prevTotalScore;
    private static String prevMPGScore;
    private static String prevGrade;
    String speedScore;
    String RPMscore;
    String accelScore;
    String totalScore;
    String MPGScore;
    String Grade;
    int fbSpeedScore;
    int fbRPMScore;
    int fbAccelScore;
    int fbTotalScore;
    int fbMPGScore;

    // Create Object class for storage
    public class ScoreObject {
        int fbSpeedScore;
        int fbRPMScore;
        int fbAccelScore;
        int fbTotalScore;
        int fbMPGScore;
        String Grade;

        public ScoreObject(int fbSpeedScore, int fbRPMScore, int fbAccelScore, int fbTotalScore, int fbMPGScore, String Grade) {
            this.fbSpeedScore = fbSpeedScore;
            this.fbRPMScore = fbRPMScore;
            this.fbAccelScore = fbAccelScore;
            this.fbTotalScore = fbTotalScore;
            this.fbMPGScore = fbMPGScore;
            this.Grade = Grade;
        }
    } // End object class

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakdown_activity);

        //TODO Add fuel score, change hard-coded values
        Bundle extras = getIntent().getExtras();
        speedScore = extras.getString("SpeedScore");
        RPMscore = extras.getString("RPMScore");
        accelScore = extras.getString("AccScore");
        totalScore = extras.getString("totalScore");
        MPGScore = extras.getString("MPGScore");
        Grade = extras.getString("grade");


        fbSpeedScore = Integer.parseInt(speedScore) / 250;
        fbRPMScore = Integer.parseInt(RPMscore) / 250;
        fbAccelScore = Integer.parseInt(accelScore) / 250;
        fbTotalScore = Integer.parseInt(totalScore) / 1000;
        fbMPGScore = Integer.parseInt(MPGScore) / 250;



        initialize();


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

    public void initialize(){
        //read data from database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    switch (child.getKey()){
                        case "speedScore":
                            prevSpeedScore = child.getValue(String.class);
                            TextView prevSpeedText = (TextView) findViewById(R.id.prevSpeed);
                            prevSpeedText.setText(prevSpeedScore + " / 250.00");
                            break;
                        case "RPMScore":
                            prevRPMscore = child.getValue(String.class);
                            TextView prevRPMScoreText = (TextView) findViewById(R.id.prevRpm);
                            prevRPMScoreText.setText(prevRPMscore +" / 250.00");
                            break;
                        case "accelScore":
                            prevAccelScore = child.getValue(String.class);
                            TextView prevAccelScoreText = (TextView) findViewById(R.id.prevAcceleration);
                            prevAccelScoreText.setText(prevAccelScore + " / 250.00");
                            break;
                        case "totalScore":
                            prevTotalScore = child.getValue(String.class);
                            TextView prevScore = (TextView) findViewById(R.id.Prevscore);
                            prevScore.setText(prevTotalScore + " / 1000.00");
                            break;
                        case "MPGScore":
                            prevMPGScore = child.getValue(String.class);
                            TextView prevMpgText = (TextView) findViewById(R.id.prevMpg);
                            prevMpgText.setText(prevMPGScore + " / 250.00");
                            break;
                        case "Grade":
                            prevGrade = child.getValue(String.class);
                            TextView prevGradeText = (TextView) findViewById(R.id.prevGrade);
                            prevGradeText.setText(prevGrade);
                            break;

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /**
     * OnPause Android Activity Lifecycle.  Removes listeners and unbinds the OpenXC service
     * on a pause activity.
     * Runs when the application goes to the background or the screen shuts off.
     */
    @Override
    public void onPause() {
        super.onPause();

        ScoreObject myScoreObject = new ScoreObject(fbSpeedScore, fbRPMScore,fbAccelScore,fbTotalScore,fbMPGScore,Grade);


        FBMPGScore.setValue(MPGScore);
        fbScoreObject.setValue(myScoreObject);
        FBaccelScore.setValue(accelScore);
        FBRPMScore.setValue(RPMscore);
        FBspeedScore.setValue(speedScore);
        FBGrade.setValue(Grade);
        FBtotalScore.setValue(totalScore);
    }

    @Override
    public void onClick(View view) {
        if (view == loginButton) {
            Intent intent = new Intent(BreakdownActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}

