package com.ev_coach.ev_coach;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GraphingActivity extends Activity implements OnItemSelectedListener {

	// TextViews on activity
	private final int maxPoints = 10000;
	private double totalScore = 0;
	private double RPMScore = 0;
	private double speedScore = 0;
	private double accelScore = 0;
	private double MPGScore = 0;

	//Spinner Variable
	private Spinner canSelect;

	private GraphView graph;
	private LineGraphSeries<DataPoint> rpmSeries = new LineGraphSeries<>();
	private LineGraphSeries<DataPoint> speedSeries = new LineGraphSeries<>();
	private LineGraphSeries<DataPoint> bSCSeries = new LineGraphSeries<>();
	private LineGraphSeries<DataPoint> accSeries = new LineGraphSeries<>();

	private double mpg;

	/**
	 * Sets up the Arrays passed over from the starter activity in order to use them to graph.
	 *
	 * @param savedInstanceState - the saved instance state of the application
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Dialog dialog = new Dialog(GraphingActivity.this);

		final DecimalFormat formatter = new DecimalFormat("#0.00");
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.graphviewlayout);
		graph = (GraphView) findViewById(R.id.graph);

		/* Check type casting for data passed */
		ArrayList<Double> listRPM = null, listSpeed = null, listBatStateCharge = null, listAcc = null;
		if( getIntent().getSerializableExtra("listRPM") instanceof ArrayList<?> ) {
			listRPM = (ArrayList<Double>) getIntent().getSerializableExtra("listRPM");
		}
		if( getIntent().getSerializableExtra("listSpeed") instanceof ArrayList<?> ) {
			listSpeed = (ArrayList<Double>) getIntent().getSerializableExtra("listSpeed");
		}
		if( getIntent().getSerializableExtra("listBatStateCharge") instanceof ArrayList<?> ) {
			listBatStateCharge = (ArrayList<Double>) getIntent().getSerializableExtra("listBatStateCharge");
		}
		if( getIntent().getSerializableExtra("listAcc") instanceof ArrayList<?> ) {
			listAcc = (ArrayList<Double>) getIntent().getSerializableExtra("listAcc");
		}


		double fuelCon = (double) getIntent().getSerializableExtra("fuelCon");
		double dist = (double) getIntent().getSerializableExtra("dist");

		//new doubles created for graphing
		double percentRPM = (double) getIntent().getSerializableExtra("percentRPM");
		double percentSpeed = (double) getIntent().getSerializableExtra("percentSpeed");
		double percentAcc = (double) getIntent().getSerializableExtra("percentAcc");

		fuelCon = fuelCon * 0.264172;
		mpg = (dist * 0.621371) / fuelCon;

		RPMScore = 250 * percentRPM;
		speedScore = 250 * percentSpeed;
		accelScore = 250 * percentAcc;

		MPGScore = StarterActivity.calcMPG(dist, fuelCon, .25);


		//Calculate score here and put it into the text box
		Log.i("GraphingActivity", "Speed Score: " + speedScore);
		Log.i("GraphingActivity", "RPM Score: " + RPMScore);
		Log.i("GraphingActivity", "Accelerating Score: " + accelScore);
		Log.i("GraphingActivity", "MPG Score: " + MPGScore);

		
		totalScore = RPMScore + speedScore + accelScore + MPGScore;
		dialog.setTitle("Score Screen");
		dialog.setContentView(R.layout.userinterface);
		dialog.show();

		final TextView textView = (TextView) dialog.findViewById(R.id.Score_Field);
		final TextView gradeView = (TextView) dialog.findViewById(R.id.Grade);
		textView.setText(String.valueOf( (int) totalScore));

		Button graphButton = (Button) dialog.findViewById(R.id.Graph_Button);
		Button breakDownButton = (Button) dialog.findViewById(R.id.Break_button);
		Button coachButton = (Button) dialog.findViewById(R.id.Coach_button);
		Button dialogButton = (Button) findViewById(R.id.dialog_button);


		//TODO Make the dialog button do something
		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.show();
			}
		});

		//Calculates the letter grade associated with the user's score
		if(totalScore >= 900) {
			if(totalScore >= 975) { gradeView.setText("A+"); gradeView.setTextColor(Color.GREEN);textView.setTextColor(Color.GREEN);}
			else if (totalScore < 975 && totalScore >= 925){ gradeView.setText("A"); gradeView.setTextColor(Color.GREEN);textView.setTextColor(Color.GREEN);}
			else { gradeView.setText("A-"); gradeView.setTextColor(Color.GREEN);textView.setTextColor(Color.GREEN);}
		}
		else if(totalScore <= 899 && totalScore >= 800) {
			if(totalScore >= 875) { gradeView.setText("B+"); gradeView.setTextColor(Color.GREEN);textView.setTextColor(Color.GREEN);}
			else if (totalScore < 875 && totalScore >= 825){ gradeView.setText("B"); gradeView.setTextColor(Color.GREEN);textView.setTextColor(Color.GREEN);}
			else { gradeView.setText("B-"); gradeView.setTextColor(Color.GREEN);textView.setTextColor(Color.GREEN);}
		}
		else if(totalScore <= 799 && totalScore >= 700){
			if(totalScore >= 775) { gradeView.setText("C+"); gradeView.setTextColor(Color.YELLOW); textView.setTextColor(Color.YELLOW);}
			else if (totalScore < 775 && totalScore >= 725){ gradeView.setText("C"); gradeView.setTextColor(Color.YELLOW); textView.setTextColor(Color.YELLOW);}
			else { gradeView.setText("C-"); gradeView.setTextColor(Color.YELLOW);textView.setTextColor(Color.YELLOW); }
		}
		else if (totalScore <= 699 && totalScore >= 600) {
			if(totalScore >= 675) { gradeView.setText("D+"); gradeView.setTextColor(Color.YELLOW);textView.setTextColor(Color.YELLOW);}
			else if (totalScore < 675 && totalScore >= 625){ gradeView.setText("D"); gradeView.setTextColor(Color.YELLOW); textView.setTextColor(Color.YELLOW);}
			else { gradeView.setText("D-"); gradeView.setTextColor(Color.YELLOW); textView.setTextColor(Color.YELLOW);}
		}
		else if(totalScore <= 599 && totalScore >= 500) {
			gradeView.setText("E");
			gradeView.setTextColor(Color.RED);
			textView.setTextColor(Color.RED);
		}
		else {
			gradeView.setText("F");
			gradeView.setTextColor(Color.RED);
			textView.setTextColor(Color.RED);
		}

		graphButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.cancel();
			}
		});

		breakDownButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent startNewActivityOpen = new Intent(getApplicationContext(), BreakdownActivity.class);
				dialog.cancel();

				startNewActivityOpen.putExtra("SpeedScore", formatter.format(speedScore));
				startNewActivityOpen.putExtra("AccScore", formatter.format(accelScore));
				startNewActivityOpen.putExtra("RPMScore", formatter.format(RPMScore));
				startNewActivityOpen.putExtra("totalScore", formatter.format(totalScore));
				startNewActivityOpen.putExtra("MPGScore", formatter.format(MPGScore));
				startNewActivityOpen.putExtra("grade", gradeView.getText().toString());
				dialog.cancel();
				startActivity(startNewActivityOpen);
			}
		});

		coachButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent newActivity = new Intent(getApplicationContext(), CoachActivity.class);
				dialog.cancel();
				//TODO FUEL SCORE, ACCEL SCORE
				newActivity.putExtra("rpmScore", RPMScore);
				newActivity.putExtra("speedScore", speedScore);
				newActivity.putExtra("mpgScore", MPGScore);
				newActivity.putExtra("accelScore", accelScore);
				startActivity(newActivity);
			}
		});
		
		String text = "Start Charge: " + listBatStateCharge.get(0) + "%\nEnd Charge: " + listBatStateCharge.get(listBatStateCharge.size() - 1) + " %\nFuel Consumed: " + formatter.format(fuelCon) + "gal\nMPGe: " + formatter.format(mpg) + " mpg";
		TextView battery;
		battery = (TextView)findViewById(R.id.battery);
		battery.setText(text);

		canSelect = (Spinner) findViewById(R.id.canSelect);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.can_signals, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		canSelect.setAdapter(adapter);
		canSelect.setOnItemSelectedListener(this);

		//Gets the units preference setting and applies the calculation to the graph
		int units = Integer.parseInt(sharedPreferences.getString("unit", "0"));
		if (listRPM != null){
			for (int i = 0; i < listRPM.size(); i++){
				rpmSeries.appendData(new DataPoint(i, listRPM.get(i)), true, maxPoints);
			}
		}

		if (listSpeed != null){
			for (int i = 0; i < listSpeed.size(); i++){
				if(units == 0) {// Imperial units (mph)
					speedSeries.appendData(new DataPoint(i, listSpeed.get(i) * 0.621371), true, maxPoints);
				} else { //Metric units (km/hr)
					speedSeries.appendData(new DataPoint(i, listSpeed.get(i)), true, maxPoints);
				}
			}
		}	
		if (listBatStateCharge != null){
			for (int i = 0; i < listBatStateCharge.size(); i++){
				bSCSeries.appendData(new DataPoint(i,listBatStateCharge.get(i)), true, maxPoints);
			}
		}

		if (listAcc != null) {
			for (int i = 0; i < listAcc.size(); i++) {
				accSeries.appendData(new DataPoint(i, listAcc.get(i)), true, maxPoints);
			}
		}
	}

	/**
	 * Selects the correct graph based on a selection of tiles by the user.
	 *
	 * @param parentView - the current view being seen
	 * @param v - the view selected
	 * @param position - which position the view selected is in
	 * @param id - the id of the position selected
	 */
	@Override
	public void onItemSelected(AdapterView<?> parentView, View v, int position,
							   long id) {

		graph.removeAllSeries();

		//TODO Set the max x value by looking at the last point in each array list
		if(canSelect.getSelectedItem().toString().equals("Vehicle Speed")){
			graph.getViewport().setXAxisBoundsManual(true);
			graph.getViewport().setMinX(0.0);
			graph.getViewport().setMaxX(speedSeries.getHighestValueX() + 5);
			graph.getViewport().setYAxisBoundsManual(true);
			graph.getViewport().setMinY(0.0);
			graph.getViewport().setMaxY(110.0);
			graph.addSeries(speedSeries);
			graph.setTitle("Vehicle Speed");
		}
		else if(canSelect.getSelectedItem().toString().equals("Engine Speed")){
			graph.getViewport().setXAxisBoundsManual(true);
			graph.getViewport().setMinX(0.0);
			graph.getViewport().setMaxX(rpmSeries.getHighestValueX() + 5);
			graph.getViewport().setYAxisBoundsManual(true);
			graph.getViewport().setMinY(0.0);
			graph.getViewport().setMaxY(6000.0);
			graph.addSeries(rpmSeries);
			graph.setTitle("Engine Speed");
		}
		else if(canSelect.getSelectedItem().toString().equals("Battery State of Charge")){
			graph.getViewport().setXAxisBoundsManual(true);
			graph.getViewport().setMinX(0.0);
			graph.getViewport().setMaxX(bSCSeries.getHighestValueX() + 5);
			graph.getViewport().setYAxisBoundsManual(true);
			graph.getViewport().setMinY(0.0);
			graph.getViewport().setMaxY(100.0);
			graph.addSeries(bSCSeries);
			graph.setTitle("Battery Charge");
		}
		else if(canSelect.getSelectedItem().toString().equals("Acceleration")){
			graph.getViewport().setXAxisBoundsManual(true);
			graph.getViewport().setMinX(0.0);
			graph.getViewport().setMaxX(accSeries.getHighestValueX() + 5);
			graph.getViewport().setYAxisBoundsManual(true);
			graph.getViewport().setMinY(0.0);
			graph.getViewport().setMaxY(100.0);
			graph.addSeries(accSeries);
			graph.setTitle("Accelerator Pedal Position");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
}