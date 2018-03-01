package com.ford.ev_coach;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    //previous drive database info
    static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    static DatabaseReference ref = database.getReference("users/" + user.getUid() + "/");
    static DatabaseReference fbScoreObject = ref.child("ScoreObject");
    static DatabaseReference FBspeedScore = ref.child("speedScore");
    static DatabaseReference FBRPMScore = ref.child("RPMScore");
    static DatabaseReference FBaccelScore = ref.child("accelScore");
    static DatabaseReference FBtotalScore = ref.child("totalScore");
    static DatabaseReference FBMPGScore = ref.child("MPGScore");
    static DatabaseReference FBGrade = ref.child("Grade");
    //leaderboard database info
    static DatabaseReference Lref = database.getReference("leaderboard");
    static DatabaseReference HighSpeed = Lref.child("HighSpeed");
    static DatabaseReference HighSpeed1 = Lref.child("HighSpeed1");
    static DatabaseReference HighSpeed2 = Lref.child("HighSpeed2");
    static DatabaseReference SpeedName = Lref.child("SpeedName");
    static DatabaseReference SpeedName1 = Lref.child("SpeedName1");
    static DatabaseReference SpeedName2 = Lref.child("SpeedName2");
    static DatabaseReference HighRPM = Lref.child("HighRPM");
    static DatabaseReference HighRPM1 = Lref.child("HighRPM1");
    static DatabaseReference HighRPM2 = Lref.child("HighRPM2");
    static DatabaseReference RPMName = Lref.child("RPMName");
    static DatabaseReference RPMName1 = Lref.child("RPMName1");
    static DatabaseReference RPMName2 = Lref.child("RPMName2");
    static DatabaseReference HighAccel = Lref.child("HighAccel");
    static DatabaseReference HighAccel1 = Lref.child("HighAccel1");
    static DatabaseReference HighAccel2 = Lref.child("HighAccel2");
    static DatabaseReference AccelName = Lref.child("AccelName");
    static DatabaseReference AccelName1 = Lref.child("AccelName1");
    static DatabaseReference AccelName2 = Lref.child("AccelName2");
    static DatabaseReference HighTotal = Lref.child("HighTotal");
    static DatabaseReference HighTotal1 = Lref.child("HighTotal1");
    static DatabaseReference HighTotal2 = Lref.child("HighTotal2");
    static DatabaseReference TotalName = Lref.child("TotalName");
    static DatabaseReference TotalName1 = Lref.child("TotalName1");
    static DatabaseReference TotalName2 = Lref.child("TotalName2");
    static DatabaseReference HighMPG = Lref.child("HighMPG");
    static DatabaseReference HighMPG1 = Lref.child("HighMPG1");
    static DatabaseReference HighMPG2 = Lref.child("HighMPG2");
    static DatabaseReference MPGName = Lref.child("MPGName");
    static DatabaseReference MPGName1 = Lref.child("MPGName1");
    static DatabaseReference MPGName2 = Lref.child("MPGName2");
    

    private Button loginButton;

    private static String prevSpeedScore;
    private static String prevRPMscore;
    private static String prevAccelScore;
    private static String prevTotalScore;
    private static String prevMPGScore;
    private static String prevGrade;
    private static double LSpeed;
    private static double LRPM;
    private static
    String speedScore;
    String RPMscore;
    String accelScore;
    String totalScore;
    String MPGScore;
    String Grade;
    double fbSpeedScore;
    double fbRPMScore;
    double fbAccelScore;
    double fbTotalScore;
    double fbMPGScore;

    // Create Object class for storage
    public static class ScoreObject {
        double fbSpeedScore;
        double fbRPMScore;
        double fbAccelScore;
        double fbTotalScore;
        double fbMPGScore;
        String Grade;


        public ScoreObject(double fbSpeedScore, double fbRPMScore, double fbAccelScore, double fbTotalScore, double fbMPGScore, String Grade) {
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


        fbSpeedScore = Double.parseDouble(speedScore);
        fbRPMScore = Double.parseDouble(RPMscore);
        fbAccelScore = Double.parseDouble(accelScore);
        fbTotalScore = Double.parseDouble(totalScore);
        fbMPGScore = Double.parseDouble(MPGScore);



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
        //String key = ref.push().getKey();
        fbScoreObject.push().setValue(myScoreObject);



     //   ref.child(key).child("Score Object").setValue(myScoreObject);

        FBMPGScore.setValue(MPGScore);
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

