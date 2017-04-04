package com.ford.ev_coach;


import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


public class VibrationListenerService extends WearableListenerService {

    private static final String TAG = "VibrationService";
    private static final int MINUTE_DELAY_TIME = 10;

    /* Reentrant and delay booleans */
    private boolean mReentrant = true;
    private boolean mSpeedDelay = false;
    private boolean mRpmDelay = false;
    private boolean mAccelDelay = false;

    /* Handlers for threads */
    private Handler mReentrantHandler = new Handler();
    private Handler mSpeedHandler = new Handler();
    private Handler mRpmHandler = new Handler();
    private Handler mAccelHandler = new Handler();

    private String mToastMessage = "";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.d(TAG, "Message received from " + messageEvent.getSourceNodeId());

        // Get the specified number out of the message 0-RPM 1-VehicleSpeed 2-Acceleration
        byte num = messageEvent.getData().length > 0 ? messageEvent.getData()[0] : -1;

        // Handle a vibration message (synchronized method)
        handleVibration(num);

        super.onMessageReceived(messageEvent);
    }


    private synchronized void handleVibration(byte num) {

        // Return if we are waiting on delays already
        if(num == 0 && mRpmDelay) return;
        else if(num == 1 && mSpeedDelay) return;
        else if(num == 3 && mAccelDelay) return;

        // 10 second reentrant delay between messages
        while(!mReentrant);

        // 10 second reentrant delay
        new Runnable() {
            @Override
            public void run() {
                mReentrant = !mReentrant;
                Log.d(TAG, "Can I reenter? " + mReentrant);

                // If we can reenter after 10 seconds, return otherwise we want to delay 10 seconds
                if(!mReentrant) {
                    mReentrantHandler.postDelayed(this, 10000);
                }
            }
        }.run();

        // Vibration service
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Vibration portion with delays
        if(vibrator.hasVibrator()) {

            // Different vibration pattern depending on the message contents
            switch(num) {

                case 0:
                    Log.d(TAG, "RPM Message");
                    vibrator.vibrate(new long[] {0, 750, 500, 750}, -1);  // long[] {how long to wait before starting, how long to vibrate for, ...}
                    new Runnable() {
                        @Override
                        public void run() {
                            mRpmDelay = !mRpmDelay;
                            Log.d(TAG, "Rpm delayed: " + mRpmDelay);
                            mToastMessage = "RPM Warning";
                            if(mRpmDelay) {
                                mRpmHandler.postDelayed(this, MINUTE_DELAY_TIME * 60000);
                            }
                        }
                    }.run();
                    break;

                case 1:
                    Log.d(TAG, "Speed Message");
                    vibrator.vibrate(new long[] {0, 2000}, -1);
                    new Runnable() {
                        @Override
                        public void run() {
                            mSpeedDelay = !mSpeedDelay;
                            Log.d(TAG, "Speed delayed: " + mSpeedDelay);
                            mToastMessage = "Speed Warning";
                            if(mSpeedDelay) {
                                mSpeedHandler.postDelayed(this, MINUTE_DELAY_TIME * 60000);
                            }
                        }
                    }.run();
                    break;

                case 2:
                    Log.d(TAG, "Accel Message");
                    vibrator.vibrate(new long[] {0, 750, 500, 750, 500, 750}, -1);
                    new Runnable() {
                        @Override
                        public void run() {
                            mAccelDelay = !mAccelDelay;
                            Log.d(TAG, "Accel delayed: " + mAccelDelay);
                            mToastMessage = "RPM Warning";
                            if(mAccelDelay) {
                                mAccelHandler.postDelayed(this, MINUTE_DELAY_TIME * 60000);
                            }
                        }
                    }.run();
                    break;

                default:
                    Log.d(TAG, "Invalid message id");
                    break;
            }

            // Send a toast message to the main ui thread
            Handler h = new Handler(getApplicationContext().getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), mToastMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
