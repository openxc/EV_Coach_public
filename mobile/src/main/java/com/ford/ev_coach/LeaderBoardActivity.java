package com.ford.ev_coach;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by andrew on 3/17/18.
 */

public class LeaderBoardActivity extends AppCompatActivity implements OnItemSelectedListener {
    private Spinner leadB;
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference Lref = database.getReference("leaderboard");

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

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        leadB = (Spinner) findViewById(R.id.leadB);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.history_graphs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leadB.setAdapter(adapter);
        leadB.setOnItemSelectedListener(this);

        Lref.addValueEventListener(new ValueEventListener() {
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
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View v, int position, long id){
        TextView title = (TextView) findViewById(R.id.Ltitle);
        TextView name = (TextView) findViewById(R.id.Lname);
        TextView name1 = (TextView) findViewById(R.id.Lname1);
        TextView name2 = (TextView) findViewById(R.id.Lname2);
        TextView score = (TextView) findViewById(R.id.Lscore);
        TextView score1 = (TextView) findViewById(R.id.Lscore1);
        TextView score2 = (TextView) findViewById(R.id.Lscore2);

        if(leadB.getSelectedItem().toString().equals("Total Score")){
            title.setText("Total High Scores");
            name.setText(LTotalName);
            name1.setText(LTotalName1);
            name2.setText(LTotalName2);
            score.setText(Double.toString(LTotal));
            score1.setText(Double.toString(LTotal1));
            score2.setText(Double.toString(LTotal2));
        }else if(leadB.getSelectedItem().toString().equals("Vehicle Speed Score")){
            title.setText("Speed High Scores");
            name.setText(LSpeedName);
            name1.setText(LSpeedName1);
            name2.setText(LSpeedName2);
            score.setText(Double.toString(LSpeed));
            score1.setText(Double.toString(LSpeed1));
            score2.setText(Double.toString(LSpeed2));
        }else if(leadB.getSelectedItem().toString().equals("Engine Speed Score")){
            title.setText("RPM High Scores");
            name.setText(LRPMName);
            name1.setText(LRPMName1);
            name2.setText(LRPMName2);
            score.setText(Double.toString(LRPM));
            score1.setText(Double.toString(LRPM1));
            score2.setText(Double.toString(LRPM2));
        }else if(leadB.getSelectedItem().toString().equals("Acceleration Score")){
            title.setText("Accel High Scores");
            name.setText(LAccelName);
            name1.setText(LAccelName1);
            name2.setText(LAccelName2);
            score.setText(Double.toString(LAccel));
            score1.setText(Double.toString(LAccel1));
            score2.setText(Double.toString(LAccel2));
        }else if(leadB.getSelectedItem().toString().equals("MPGe Score")){
            title.setText("MPGe High Scores");
            name.setText(LMPGName);
            name1.setText(LMPGName1);
            name2.setText(LMPGName2);
            score.setText(Double.toString(LMPG));
            score1.setText(Double.toString(LMPG1));
            score2.setText(Double.toString(LMPG2));
        }
    }

    public void onNothingSelected(AdapterView<?> view){}
}
