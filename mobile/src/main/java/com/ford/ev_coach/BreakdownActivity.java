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

import static java.lang.Double.NaN;

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
    

    private Button lbButton;

    private static String prevSpeedScore;
    int speedCount = 0;
    private static String prevRPMscore;
    int RPMCount = 0;
    private static String prevAccelScore;
    int AccelCount = 0;
    private static String prevTotalScore;
    int TotalCount = 0;
    private static String prevMPGScore;
    int MPGCount = 0;
    private static String prevGrade;
    int GradeCount = 0;

    //leaderboard values
    private static double LSpeed;
    private static double LSpeed1;
    private static double LSpeed2;
    private static String LSpeedName;
    private static String LSpeedName1;
    private static String LSpeedName2;
    private static double LRPM;
    private static double LRPM1;
    private static double LRPM2;
    private static String LRPMName;
    private static String LRPMName1;
    private static String LRPMName2;
    private static double LAccel;
    private static double LAccel1;
    private static double LAccel2;
    private static String LAccelName;
    private static String LAccelName1;
    private static String LAccelName2;
    private static double LTotal;
    private static double LTotal1;
    private static double LTotal2;
    private static String LTotalName;
    private static String LTotalName1;
    private static String LTotalName2;
    private static double LMPG;
    private static double LMPG1;
    private static double LMPG2;
    private static String LMPGName;
    private static String LMPGName1;
    private static String LMPGName2;
    int leaderCount = 0;
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
    String fbName = user.getEmail().substring(0, user.getEmail().indexOf('@')-1);

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

        lbButton = (Button) findViewById(R.id.LeadB_button);
        lbButton.setOnClickListener(this);


      Lref.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              for(DataSnapshot child: dataSnapshot.getChildren()){
                  switch (child.getKey()){
                      case "HighSpeed":
                          LSpeed = child.getValue(Double.class);

                          break;
                      case "SpeedName":
                          LSpeedName = child.getValue(String.class);
                          break;
                      case "HighSpeed1":
                          LSpeed1 = child.getValue(Double.class);

                          break;
                      case "SpeedName1":
                          LSpeedName1 = child.getValue(String.class);
                          break;
                      case "HighSpeed2":
                          LSpeed2 = child.getValue(Double.class);

                          break;
                      case "SpeedName2":
                          LSpeedName2 = child.getValue(String.class);
                          break;
                      case "HighRPM":
                          LRPM = child.getValue(Double.class);

                          break;
                      case "RPMName":
                          LRPMName = child.getValue(String.class);
                          break;
                      case "HighRPM1":
                          LRPM1 = child.getValue(Double.class);

                          break;
                      case "RPMName1":
                          LRPMName1 = child.getValue(String.class);
                          break;
                      case "HighRPM2":
                          LRPM2 = child.getValue(Double.class);

                          break;
                      case "RPMName2":
                          LRPMName2 = child.getValue(String.class);
                          break;
                      case "HighAccel":
                          LAccel = (double) child.getValue();

                          break;
                      case "AccelName":
                          LAccelName = child.getValue(String.class);
                          break;
                      case "HighAccel1":
                          LAccel1 = child.getValue(Double.class);

                          break;
                      case "AccelName1":
                          LAccelName1 = child.getValue(String.class);
                          break;
                      case "HighAccel2":
                          LAccel2 = child.getValue(Double.class);

                          break;
                      case "AccelName2":
                          LAccelName2 = child.getValue(String.class);
                          break;
                      case "HighTotal":
                          LTotal = child.getValue(Double.class);

                          break;
                      case "TotalName":
                          LTotalName = child.getValue(String.class);
                          break;
                      case "HighTotal1":
                          LTotal1 = child.getValue(Double.class);

                          break;
                      case "TotalName1":
                          LTotalName1 = child.getValue(String.class);
                          break;
                      case "HighTotal2":
                          LTotal2 = child.getValue(Double.class);

                          break;
                      case "TotalName2":
                          LTotalName2 = child.getValue(String.class);
                      break;
                      case "HighMPG":
                          LMPG = child.getValue(Double.class);

                          break;
                      case "MPGName":
                          LMPGName = child.getValue(String.class);
                      break;
                      case "HighMPG1":
                          LMPG1 = child.getValue(Double.class);

                          break;
                      case "MPGName1":
                          LMPGName1 = child.getValue(String.class);
                          break;
                      case "HighMPG2":
                          LMPG2 = child.getValue(Double.class);

                          break;
                      case "MPGName2":
                          LMPGName2 = child.getValue(String.class);
                          break;
                  }
              }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });


      //check to see what place the scores are in the leaderboard
        if(leaderCount == 0){
            if(fbSpeedScore > LSpeed || LSpeed == NaN){
                HighSpeed2.setValue(LSpeed1);
                SpeedName2.setValue(LSpeedName1);
                HighSpeed1.setValue(LSpeed);
                SpeedName1.setValue(LSpeedName);
                HighSpeed.setValue(fbSpeedScore);
                SpeedName.setValue(fbName);
            }else{
                if(fbSpeedScore > LSpeed1 || LSpeed1 == NaN){
                    HighSpeed2.setValue(LSpeed1);
                    SpeedName2.setValue(LSpeedName1);
                    HighSpeed1.setValue(fbSpeedScore);
                    SpeedName1.setValue(fbName);
                }else{
                    if(fbSpeedScore > LSpeed2 || LSpeed2 == NaN){
                        HighSpeed2.setValue(fbSpeedScore);
                        SpeedName2.setValue(fbName);
                    }
                }
            }
            if(fbAccelScore > LAccel || LAccel == NaN){
                HighAccel2.setValue(LAccel1);
                AccelName2.setValue(LAccelName1);
                HighAccel1.setValue(LAccel);
                AccelName1.setValue(LAccelName);
                HighAccel.setValue(fbAccelScore);
                AccelName.setValue(fbName);

            }else{
                if(fbAccelScore > LAccel1 || LAccel1 == NaN){
                    HighAccel2.setValue(LAccel1);
                    AccelName2.setValue(LAccelName1);
                    HighAccel1.setValue(fbAccelScore);
                    AccelName1.setValue(fbName);
                }else{
                    if(fbAccelScore > LAccel2 || LAccel2 == NaN){
                        HighAccel2.setValue(fbAccelScore);
                        AccelName1.setValue(fbName);
                    }
                }
            }
            if(fbMPGScore > LMPG || LMPG == NaN){
                HighMPG2.setValue(LMPG1);
                MPGName2.setValue(LMPGName1);
                HighMPG1.setValue(LMPG);
                MPGName1.setValue(LMPGName);
                HighMPG.setValue(fbMPGScore);
                MPGName.setValue(fbName);
            }else{
                if(fbMPGScore > LMPG1 || LMPG1 == NaN){
                    HighMPG2.setValue(LMPG1);
                    MPGName2.setValue(LMPGName1);
                    HighMPG1.setValue(fbMPGScore);
                    MPGName1.setValue(fbName);
                }else{
                    if(fbMPGScore > LMPG2 || LMPG2 == NaN){
                        HighMPG2.setValue(fbMPGScore);
                        MPGName.setValue(fbName);
                    }
                }
            }
            if(fbRPMScore > LRPM || LRPM == NaN){
                HighRPM2.setValue(LRPM1);
                RPMName2.setValue(LRPMName1);
                HighRPM1.setValue(LRPM);
                RPMName1.setValue(LRPMName);
                HighRPM.setValue(fbRPMScore);
                RPMName.setValue(fbName);
            }else{
                if(fbRPMScore > LRPM1 || LRPM1 == NaN){
                    HighRPM2.setValue(LRPM1);
                    RPMName2.setValue(LRPMName1);
                    HighRPM1.setValue(fbRPMScore);
                    RPMName1.setValue(fbName);
                }else{
                    if(fbRPMScore > LRPM2 || LRPM2 == NaN){
                        HighRPM2.setValue(fbRPMScore);
                        RPMName2.setValue(fbName);
                    }
                }
            }
            if(fbTotalScore > LTotal || LTotal == NaN){
                HighTotal2.setValue(LTotal1);
                TotalName2.setValue(LTotalName1);
                HighTotal1.setValue(LTotal);
                TotalName1.setValue(LTotalName);
                HighTotal.setValue(fbTotalScore);
                TotalName.setValue(fbName);
            }else{
                if(fbTotalScore > LTotal1 || LTotal1 == NaN){
                    HighTotal2.setValue(LTotal1);
                    TotalName2.setValue(LTotalName1);
                    HighTotal1.setValue(fbTotalScore);
                    TotalName1.setValue(fbName);
                }else{
                    if(fbTotalScore > LTotal2 || LTotal2 == NaN){
                        HighTotal2.setValue(fbTotalScore);
                        TotalName2.setValue(fbName);
                    }
                }
            }
            leaderCount = 1;
        }


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
                            if(speedCount == 0){
                                prevSpeedScore = child.getValue(String.class);
                            }
                            speedCount = 1;
                            TextView prevSpeedText = (TextView) findViewById(R.id.prevSpeed);
                            prevSpeedText.setText(prevSpeedScore + " / 250.00");
                            break;
                        case "RPMScore":
                            if(RPMCount == 0){
                                prevRPMscore = child.getValue(String.class);
                            }
                            RPMCount = 1;
                            TextView prevRPMScoreText = (TextView) findViewById(R.id.prevRpm);
                            prevRPMScoreText.setText(prevRPMscore +" / 250.00");
                            break;
                        case "accelScore":
                            if(AccelCount == 0){
                                prevAccelScore = child.getValue(String.class);
                            }
                            AccelCount = 1;
                            TextView prevAccelScoreText = (TextView) findViewById(R.id.prevAcceleration);
                            prevAccelScoreText.setText(prevAccelScore + " / 250.00");
                            break;
                        case "totalScore":
                            if(TotalCount == 0){
                                prevTotalScore = child.getValue(String.class);
                            }
                            TotalCount = 1;
                            TextView prevScore = (TextView) findViewById(R.id.Prevscore);
                            prevScore.setText(prevTotalScore + " / 1000.00");
                            break;
                        case "MPGScore":
                            if(MPGCount == 0){
                                prevMPGScore = child.getValue(String.class);
                            }
                            MPGCount = 1;
                            TextView prevMpgText = (TextView) findViewById(R.id.prevMpg);
                            prevMpgText.setText(prevMPGScore + " / 250.00");
                            break;
                        case "Grade":
                            if(GradeCount == 0){
                                prevGrade = child.getValue(String.class);
                            }
                            GradeCount = 1;
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
        fbScoreObject.push().setValue(myScoreObject);



        FBMPGScore.setValue(MPGScore);
        FBaccelScore.setValue(accelScore);
        FBRPMScore.setValue(RPMscore);
        FBspeedScore.setValue(speedScore);
        FBGrade.setValue(Grade);
        FBtotalScore.setValue(totalScore);


    }

    @Override
    public void onClick(View view) {
        if (view == lbButton) {
            Intent intent = new Intent(BreakdownActivity.this, LeaderBoardActivity.class);
            startActivity(intent);
        }
    }

}

