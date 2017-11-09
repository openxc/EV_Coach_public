package com.ford.ev_coach;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
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
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName(); /* Logging tag for this class/activity */

    private VehicleManager mVehicleManager;              /* Vehicle manager object from OpenXC */
    private TextView connection_status;   //You Suck               /* Connection status TextView */

    /* ArrayLists to store the values of each element */
    private ArrayList<Double> listRPM = new ArrayList<>();
    private ArrayList<Double> listSpeed = new ArrayList<>();
    private ArrayList<Double> listBatStateCharge = new ArrayList<>();
    private ArrayList<Double> listAcc = new ArrayList<>();

    private double fuelCon = 0.0;       /* Keeps track of the total fuel consumed         */
    private double batCon = 0.0;        /*keeps track of the total battery consumed       */
    private double startBat = 0.0;      /*keeps track of initial battery charge           */
    private boolean firstBat = true;
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

    private final int ACCELERATION_THRESHOLD = 23; // percent pedal position
    private final int SPEED_THRESHOLD = 50; //KM/hr ~31.06mph
    private final int RPM_THRESHOLD = 1600;
    private Button loginButton;
    private GoogleApiClient googleApiClient;
    private final String WEAR_VIBRATE_PATH = "/test";
    BottomNavigationView navigationView;

    //keeps track of all wearables
    private ArrayList<Node> mNodes = new ArrayList<Node>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if(id == R.id.action_settings) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            if(id == R.id.login_Setting) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                return true;
            }
            if(id == R.id.history){
                Intent intent = new Intent(getApplicationContext(), ScoreHistoryActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        }
    };
    /**
     * OnCreate Android Activity Lifecycle.  Sets up the connection status and screen information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loginButton.setOnClickListener(this);
        /**{
            @Override
            public void onClick(View view) {
                if (view == loginButton) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
**/





        setContentView(R.layout.main_activity);
        loginButton = (Button) findViewById(R.id.loginButton);
        //connects wearable api
        if(googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .build();
        }
        //put in to setup bottomNav

        if(!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }

        /* Get the connected nodes */
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(@NonNull NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                for(Node node : getConnectedNodesResult.getNodes()) {
                    if(!mNodes.contains(node)) {
                        mNodes.add(node);
                    }
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**    @Override
    public void onClick(View view) {
     if (view == loginButton) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }**/

    //this creates setting menu
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    //this is what action will be take when an actual setting is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.login_Setting) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/


    /**
     * OnPause Android Activity Lifecycle.  Removes listeners and unbinds the OpenXC service
     * on a pause activity.
     * Runs when the application goes to the background or the screen shuts off.
     */
    @Override
    public void onPause() {
        super.onPause();
        /* Unbind all OpenXC listeners if they aren't already unbound*/
        if (mVehicleManager != null) {
            Log.i(TAG, "Unbinding from Vehicle Manager");

            /* Remove all android listeners*/
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
    }

    /**
     * onResume android Activity Lifecycle.  Creates a new VehicleManager intent and binds
     * the VehicleManager to this StarterActivity.
     *
     * Then connect all listener objects.
     */

    @Override
    public void onResume() {
        super.onResume();
        /* Reconnect to the VehicleManager object*/
        if (mVehicleManager == null) {
            Intent intent = new Intent(this, VehicleManager.class);
            bindService(intent, mConnection, Context.BIND_ADJUST_WITH_ACTIVITY);
        }
    }


    EngineSpeed.Listener mSpeedListener = new EngineSpeed.Listener() {
        @Override
        public void receive(Measurement measurement) {
            final EngineSpeed speed = (EngineSpeed) measurement;
            totalRPM++; //Add point to total RPM

            //If the RPM value is less than the default RPM value, then it is a Good RPM
            if(speed.getValue().doubleValue() == 0.) {
                return;
            }
            else if ( speed.getValue().doubleValue() <= RPM_THRESHOLD ) { //For the City
                goodRPM++; //Increase number of good RPMs
            }
            listRPM.add(speed.getValue().doubleValue());
            if(speed.getValue().doubleValue() > RPM_THRESHOLD) {
                sendMessageToDevice(new byte[]{0}, WEAR_VIBRATE_PATH);
            }
        }
    };

    IgnitionStatus.Listener mIgnitionListener = new IgnitionStatus.Listener() {

        @Override
        public void receive(Measurement measurement) {
            final IgnitionStatus status = (IgnitionStatus) measurement;
            if (status.getValue().toString().toUpperCase().equals("OFF")) {

                // Graphing activity intent
                Log.i(TAG, "IS RUNNING");
                Intent i = new Intent(getApplicationContext(), GraphingActivity.class);
                i.putExtra("listRPM", listRPM);
                i.putExtra("listSpeed", listSpeed);
                i.putExtra("listBatStateCharge", listBatStateCharge);
                i.putExtra("listAcc", listAcc);
                i.putExtra("fuelCon", fuelCon);
                i.putExtra("batCon", batCon);
                i.putExtra("dist", dist);
                i.putExtra("percentAcc", ((double)goodAccel)/((double)totalAccel));
                i.putExtra("percentSpeed", ((double)goodSpeed)/((double)totalSpeed));
                i.putExtra("percentRPM", ((double)goodRPM)/((double)totalRPM));

                // Service to launch for the local database sync
                Intent intent = new Intent(getApplicationContext(), DBSyncService.class);
                intent.putExtra(DBTableContract.OverviewTableEntry.COLUMN_ACCELERATOR_SCORE, ((double)goodAccel / (double)totalAccel) * 250);
                intent.putExtra(DBTableContract.OverviewTableEntry.COLUMN_ENGINE_SPEED_SCORE, ((double)goodRPM / (double)totalRPM) * 250);
                intent.putExtra(DBTableContract.OverviewTableEntry.COLUMN_MPGE_SCORE, calcMPG(dist, batCon, fuelCon, .25));
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
            if(speed.getValue().doubleValue() == 0.) {
                return;
            }
            else if ( speed.getValue().doubleValue() <= SPEED_THRESHOLD ) { //For the City
                goodSpeed++; //Increase the number of good speeds
            }

            listSpeed.add(speed.getValue().doubleValue());
            if(speed.getValue().doubleValue() > SPEED_THRESHOLD) {
                sendMessageToDevice(new byte[]{1}, WEAR_VIBRATE_PATH);
            }


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
            if(firstBat){
                firstBat = false;
                startBat = charge.getValue().doubleValue();
            }
            else{
                batCon = charge.getValue().doubleValue();
            }
            listBatStateCharge.add(charge.getValue().doubleValue());
        }
    };

    AcceleratorPedalPosition.Listener mAccListener = new AcceleratorPedalPosition.Listener() {
        public void receive(Measurement measurement) {
            final AcceleratorPedalPosition acc = (AcceleratorPedalPosition) measurement;
            totalAccel++; //Add point to total Acceleration

            //If the acceleration value is less than the default acceleration value, then it is a Good Acceleration
            if(acc.getValue().doubleValue() == 0.) {
                return;
            }
            else if ( acc.getValue().doubleValue() <= ACCELERATION_THRESHOLD ) {
                goodAccel++; //Increase the number of good Accelerations
            }

            listAcc.add(acc.getValue().doubleValue());
            if(acc.getValue().doubleValue() > ACCELERATION_THRESHOLD) {
                sendMessageToDevice(new byte[]{2}, WEAR_VIBRATE_PATH);
            }
        }
    };

    Odometer.Listener mDistListener = new Odometer.Listener() {
        public void receive(Measurement measurement) {
            final Odometer odo = (Odometer) measurement;
            if (firstDist) {
                firstDist = false;
                startDist = odo.getValue().doubleValue();
            } else {
                dist = odo.getValue().doubleValue() - startDist;
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


            setContentView(R.layout.main_activity);
            connection_status = (TextView) findViewById(R.id.connection_status);
            connection_status.setTextColor(Color.GREEN);
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


    public static double calcMPG(double dist,double batteryCosumption, double fuelConsumption, double weight) {
        double score = 100;
        double mpgFinal;
        double mpgBat;
        double mpgFuel;
        //converts to gallons
        fuelConsumption = fuelConsumption * 0.264172;
        //give infinite if negative fuel consumed or zero
        if (fuelConsumption <= 0 && batteryCosumption <= 0){
            mpgFinal = 999;
        }
        else {
            //km to miles
            mpgFuel = (dist * 0.621371) / fuelConsumption;
            mpgBat = (dist * 0.621371) / fuelConsumption;
            mpgFinal = mpgBat + mpgFuel;
        }

        if (mpgFinal <= 5) {
            score = 0;
        } else if (mpgFinal <= 10) {
            score = 10;
        } else if (mpgFinal <= 15) {
            score = 20;
        } else if (mpgFinal <= 20) {
            score = 30;
        } else if (mpgFinal <= 25) {
            score = 50;
        } else if (mpgFinal <= 30) {
            score = 75;
        } else if (mpgFinal <= 35) {
            score = 90;
        }

        score *= 10;

        return score * weight;

    }

    private synchronized void sendMessageToDevice(final byte[] message, final String path) {

        Log.d(TAG, "Entered Send Message");

        if(!googleApiClient.isConnected()) {
            ConnectionResult connectionResult = googleApiClient.blockingConnect(30, TimeUnit.SECONDS);
            if(!connectionResult.isSuccess()) {
                Log.d(TAG, "FAILED TO CONNECT.");
            }
        }

        /* Get the connected nodes */
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(@NonNull NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                for(Node node : getConnectedNodesResult.getNodes()) {
                    if(!mNodes.contains(node)) {
                        mNodes.add(node);
                    }
                }
            }
        });

		/* Send the message if the node is nearby */
        for(Node node : mNodes) {
            if(node.isNearby()) {
                Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), WEAR_VIBRATE_PATH, message).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: " + sendMessageResult.getStatus().getStatusCode());
                        } else {
                            Log.d(TAG, "Successfully sent message");
                        }
                    }
                });
            }
        }
    }

    public void sendReset(View v) {
        sendMessageToDevice(new byte[] {3}, WEAR_VIBRATE_PATH);
    }
}