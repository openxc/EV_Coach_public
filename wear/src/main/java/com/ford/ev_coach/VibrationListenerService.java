package com.ford.ev_coach;

import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


public class VibrationListenerService extends WearableListenerService {

    private static final String TAG = "VibrationService";
    private final String WEAR_VIBRATE_PATH = "/test";
    private boolean REENTRANT = true;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            REENTRANT = false;
            mHandler.postDelayed(mRunnable, 10000);
            REENTRANT = true;
        }
    };

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        //TODO <BMV> Should we throw out older messages?
        // Wait at least 5 seconds before running this after a prior message throwing out older messages
        if(!REENTRANT) {
            Log.d(TAG, "Skipped a message");
            return;
        }
        mRunnable.run();

        Log.d(TAG, "Message received from " + messageEvent.getSourceNodeId());
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Get the specified number out of the message 0-RPM 1-VehicleSpeed 2-Acceleration
        byte num = messageEvent.getData().length > 0 ? messageEvent.getData()[0] : -1;

        if(vibrator.hasVibrator()) {

            // Different vibration pattern depending on the message contents
            switch(num) {
                case 0:
                    Log.d(TAG, "RPM Message");
                    vibrator.vibrate(new long[] {1000, 3000}, -1);  // long[] {how long to wait before starting, how long to vibrate for, ...}
                    break;
                case 1:
                    Log.d(TAG, "Speed Message");
                    vibrator.vibrate(new long[] {1000, 1000}, -1);
                    break;
                case 2:
                    Log.d(TAG, "Accel Message");
                    vibrator.vibrate(new long[] {1000, 2000}, -1);
                    break;
                default:
                    vibrator.vibrate(100);
                    break;
            }
        }
        super.onMessageReceived(messageEvent);
    }
}
