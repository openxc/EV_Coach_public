package com.ev_coach.ev_coach;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.openxc.VehicleManager;
import com.openxc.measurements.AcceleratorPedalPosition;
import com.openxc.measurements.BatteryStateOfCharge;
import com.openxc.measurements.EngineSpeed;
import com.openxc.measurements.FuelConsumed;
import com.openxc.measurements.IgnitionStatus;
import com.openxc.measurements.Measurement;
import com.openxc.measurements.Odometer;
import com.openxc.measurements.VehicleSpeed;

import java.util.ArrayList;
import java.util.Locale;

/**
 * StarterActivity class.
 *
 * Sets up the listeners for the openxc and stores them.
 *
 *
 */
public class StarterActivity extends Activity {
    private static final String TAG = "StarterActivity"; /* Logging tag for this class/activity */

	private VehicleManager mVehicleManager;              /* Vehicle manager object from OpenXC */
	//private final int moduloValue = 15;                  /* Number of datapoints to accept */
	private TextToSpeech ttobj;                          /* Text to speech object for verbal
	                                                        feedback */
	private TextView connection_status;                  /* Connection status TextView */

    /* TODO <BMV> - Add in with live feedback
    // Watch settings - specific to pebble
	private static final int VIBE_KEY = 0;
	private static final int LOGO_KEY = 1;
	private final static UUID VIBE_UUID = UUID.fromString("7dd8789d-3bb2-4596-ac10-fbe15196419d");

    //Vibrate settings
	private static final int SHORT_PULSE = 0;
	private static final int LONG_PULSE = 1;
	private static final int DOUBLE_PULSE = 2;
    */

    /* ArrayLists to store the values of each element */
	ArrayList<Double> listRPM = new ArrayList<>();
	ArrayList<Double> listSpeed = new ArrayList<>();
	ArrayList<Double> listBatStateCharge = new ArrayList<>();
	ArrayList<Double> listAcc = new ArrayList<>();

    private double fuelCon = 0.0;       /* Keeps track of the total fuel consumed         */
	private double startFuel = 0.0;     /* Keeps track of the initial fuel                */
	private boolean firstFuel = true;
	private double startDist = 0.0;     /* Keeps track of the starting odometer distance  */
	private double dist = 0.0;          /* Keeps track of the distance travelled thus far */
	private boolean firstDist = true;

	// Variables to find the average RPM, Speed, and Acceleration score
	private int goodRPM = 0; // Number of good RPM points
	private int goodAccel = 0; //Number of good Acceleration points
	private int goodSpeed = 0; // Number of good Speed points
	private int totalRPM = 0; // Total number of RPM points
	private int totalAccel = 0; //Total number of Acceleration points
	private int totalSpeed = 0; //Total number of Speed points

    /**
     * OnCreate Android Activity Lifecycle.  Sets up the connection status and screen information.
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

        /* Displays the correct message for the connection status of the service */
        //TODO - <BMV> Update so this fixes itself when it isn't connected correctly
		connection_status = (TextView) findViewById(R.id.connection_status);
		connection_status.setTextColor(Color.RED);
        //TODO - <BMV> Use a string resource for this value.
		connection_status.setText("Not connected");

        /* Set up the TextToSpeech object for verbal feedback */
        ttobj = new TextToSpeech(getApplicationContext(),
				new TextToSpeech.OnInitListener() {
					@Override
					public void onInit(int status) {
						if (status != TextToSpeech.ERROR) {
							ttobj.setLanguage(Locale.UK);
						}
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

        /* Unbind all OpenXC listeners if they aren't already unbound */
		if (mVehicleManager != null) {
			Log.i(TAG, "Unbinding from Vehicle Manager");

            /* Remove all android listeners */
			mVehicleManager.removeListener(EngineSpeed.class, mSpeedListener);
			mVehicleManager.removeListener(IgnitionStatus.class, mIgnitionListener);
			mVehicleManager.removeListener(FuelConsumed.class, mFuelListener);
			mVehicleManager.removeListener(VehicleSpeed.class, mSpeedVehicleListener);
			mVehicleManager.removeListener(BatteryStateOfCharge.class, mBatteryStateOfChargeListener);
			mVehicleManager.removeListener(AcceleratorPedalPosition.class, mAccListener);
			mVehicleManager.removeListener(Odometer.class, mDistListener);
			unbindService(mConnection);
			mVehicleManager = null;
		}

        /* Stop the TextToSpeech object */
		if (ttobj != null) {
			ttobj.stop();
			ttobj.shutdown();
		}
	}

    /**
     * onResume android Activity LIfecycle.  Creates a new VehicleManager intent and binds
     * the VehicleManager to this StarterActivity.
     *
     * Then connect all listener objects.
     */
	@Override
	public void onResume() {
		super.onResume();

        /* Reconnect to the VehicleManager object */
		if (mVehicleManager == null) {
			Intent intent = new Intent(this, VehicleManager.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}
	}


	EngineSpeed.Listener mSpeedListener = new EngineSpeed.Listener() {
		@Override
		public void receive(Measurement measurement) {
			final EngineSpeed speed = (EngineSpeed) measurement;
			totalRPM++; //Add point to total RPM

			//If the RPM value is less than the default RPM value, then it is a Good RPM
			if ( speed.getValue().doubleValue() <= 500 ) { //For the City
				goodRPM++; //Increase number of good RPMs
			}
			listRPM.add(speed.getValue().doubleValue());
		}
	};

	IgnitionStatus.Listener mIgnitionListener = new IgnitionStatus.Listener() {

		@Override
		public void receive(Measurement measurement) {
			final IgnitionStatus status = (IgnitionStatus) measurement;
			if (status.getValue().toString().equals("OFF")) {

				// Graphing activity intent
				Log.i(TAG, "IS RUNNING");
				Intent i = new Intent(getApplicationContext(), GraphingActivity.class);
				i.putExtra("listRPM", listRPM);
				i.putExtra("listSpeed", listSpeed);
				i.putExtra("listBatStateCharge", listBatStateCharge);
				i.putExtra("listAcc", listAcc);
				i.putExtra("fuelCon", fuelCon);
				i.putExtra("dist", dist);
				i.putExtra("percentAcc", ((double)goodAccel)/((double)totalAccel));
				i.putExtra("percentSpeed", ((double)goodSpeed)/((double)totalSpeed));
				i.putExtra("percentRPM", ((double)goodRPM)/((double)totalRPM));

				// Service to launch for the local database sync
				Intent intent = new Intent(getApplicationContext(), DBSyncService.class);
				intent.putExtra(DBTableContract.OverviewTableEntry.COLUMN_ACCELERATOR_SCORE, ((double)goodAccel / (double)totalAccel) * 250);
				intent.putExtra(DBTableContract.OverviewTableEntry.COLUMN_ENGINE_SPEED_SCORE, ((double)goodRPM / (double)totalRPM) * 250);
				intent.putExtra(DBTableContract.OverviewTableEntry.COLUMN_MPGE_SCORE, calcMPG(dist, fuelCon, .25));
				intent.putExtra(DBTableContract.OverviewTableEntry.COLUMN_VEHICLE_SPEED_SCORE, ((double)goodSpeed / (double)totalSpeed) * 250);

				/* Log out percentage information */
				Log.d(TAG, "Percentage of good acceleration: " + 100 * (((double)goodAccel) / totalAccel));
				Log.d(TAG, "Percentage of good speed: " + 100 * (((double)goodSpeed) / totalSpeed));
				Log.d(TAG, "Percentage of good RPM: " + 100 * (((double)goodRPM) / totalRPM));

				/* Reentrant on the main application screen */
				firstDist = true;
				firstFuel = true;

				/* Start the corresponding local database sync and the GraphingActivity */
				startService(intent);
				startActivity(i);
			}
		}

	};

	VehicleSpeed.Listener mSpeedVehicleListener = new VehicleSpeed.Listener() {
		public void receive(Measurement measurement) {
			final VehicleSpeed speed = (VehicleSpeed) measurement;
			totalSpeed++; //Add point to total Speed

            //If the Speed is less than the default Speed, then it is a Good Speed
			if ( speed.getValue().doubleValue() <= 73 ) { //For the City
				goodSpeed++; //Increase the number of good speeds
			}

            listSpeed.add(speed.getValue().doubleValue());


		}
	};

	FuelConsumed.Listener mFuelListener = new FuelConsumed.Listener() {
		public void receive(Measurement measurement) {
			final FuelConsumed fuel = (FuelConsumed) measurement;

			if (firstFuel) {
				firstFuel = false;
				startFuel = fuel.getValue().doubleValue();
			} else {
				fuelCon = fuel.getValue().doubleValue() - startFuel;
			}
        }
	};

	BatteryStateOfCharge.Listener mBatteryStateOfChargeListener = new BatteryStateOfCharge.Listener() {
		public void receive(Measurement measurement) {
			final BatteryStateOfCharge charge = (BatteryStateOfCharge) measurement;
  	    	listBatStateCharge.add(charge.getValue().doubleValue());
		}
	};

	AcceleratorPedalPosition.Listener mAccListener = new AcceleratorPedalPosition.Listener() {
		public void receive(Measurement measurement) {
			final AcceleratorPedalPosition acc = (AcceleratorPedalPosition) measurement;
			totalAccel++; //Add point to total Acceleration

			//If the acceleration value is less than the default acceleration value, then it is a Good Acceleration
			if ( acc.getValue().doubleValue() <= 15 ) { //For the City
				goodAccel++; //Increase the number of good Accelerations
			}

			listAcc.add(acc.getValue().doubleValue());
        }
	};

	Odometer.Listener mDistListener = new Odometer.Listener() {
		public void receive(Measurement measurement) {
			final Odometer odo = (Odometer) measurement;
			double MPGScore;
			if (firstDist) {
				firstDist = false;
				startDist = odo.getValue().doubleValue();
			} else {
				dist = odo.getValue().doubleValue() - startDist;
				MPGScore = calcMPG(dist, fuelCon, .25 );
            }
		}
	};

	private ServiceConnection mConnection = new ServiceConnection() {
		// Called when the connection with the VehicleManager service is
		// established, i.e. bound.
		public void onServiceConnected(ComponentName className,
									   IBinder service) {
			Log.i(TAG, "Bound to VehicleManager");

            /* Get the VehicleManager object */
			mVehicleManager = ((VehicleManager.VehicleBinder) service)
					.getService();


			setContentView(R.layout.splash_screen);
			connection_status = (TextView) findViewById(R.id.connection_status);
			connection_status.setTextColor(Color.GREEN);
            //TODO <BMV> - Use a string resource for this value
			connection_status.setText("Connected");

            /* Add all the listeners to the vehicle manager object when the service connects */
			mVehicleManager.addListener(EngineSpeed.class, mSpeedListener);
			mVehicleManager.addListener(IgnitionStatus.class, mIgnitionListener);
			mVehicleManager.addListener(VehicleSpeed.class, mSpeedVehicleListener);
			mVehicleManager.addListener(FuelConsumed.class, mFuelListener);
			mVehicleManager.addListener(BatteryStateOfCharge.class, mBatteryStateOfChargeListener);
			mVehicleManager.addListener(AcceleratorPedalPosition.class, mAccListener);
			mVehicleManager.addListener(Odometer.class, mDistListener);
		}

		// Called when the connection with the service disconnects unexpectedly
		public void onServiceDisconnected(ComponentName className) {
			Log.w(TAG, "VehicleManager Service disconnected unexpectedly");
			mVehicleManager = null;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.starter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public static double calcMPG(double dist,double fuelConsumption, double weight) {
		double score = 100;
		double mpg;
		//converts to gallons
		fuelConsumption = fuelConsumption * 0.264172;
		//give infinite if negative fuel consumed or zero
		if (fuelConsumption <= 0){
			mpg = 999;
		}
		else {
			//km to miles
			mpg = (dist * 0.621371) / fuelConsumption;
		}

		if (mpg <= 5) {
			score = 0;
		} else if (mpg <= 10) {
			score = 10;
		} else if (mpg <= 15) {
			score = 20;
		} else if (mpg <= 20) {
			score = 30;
		} else if (mpg <= 25) {
			score = 50;
		} else if (mpg <= 30) {
			score = 75;
		} else if (mpg <= 35) {
			score = 90;
		}

		score *= 10;

		return score * weight;

	}
}
/*
	public boolean isPebbleConnected() {
		boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
		Log.i(getLocalClassName(), "Pebble is " + (connected ? "connected" : "not connected"));
		return connected;
	}

	public void sendDataToWatch() {

		// Build dictionary with vibration data
		PebbleDictionary data = new PebbleDictionary();

		// replace tempVibeVar with SHORT_PULSE, LONG_PULSE, DOUBLE_PULSE, CUSTOM_PULSE
		data.addUint8(VIBE_KEY, (byte) tempVibeVar);
		data.addString(1, "A string");

		//send the dictionary
		PebbleKit.sendDataToPebble(getApplicationContext(), VIBE_UUID, data);
	}
}
*/