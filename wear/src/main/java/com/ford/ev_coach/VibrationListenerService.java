package com.ford.ev_coach;

import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


public class VibrationListenerService extends WearableListenerService {

    private static final String TAG = "VibrationService";
    private static final int MINUTE_DELAY_TIME = 10;

    /* Reentrant and delay booleans */
    private boolean reentrant = true;
    private boolean speedDelay = false;
    private boolean rpmDelay = false;
    private boolean accelDelay = false;

    private Handler mHandler = new Handler();

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            reentrant = !reentrant;
            Log.d(TAG, "Reentrancy? " + reentrant);
            mHandler.postDelayed(mRunnable, 10000);
            if(reentrant) mHandler.removeCallbacks(mRunnable);
        }
    };

    /* Delay for speed message to reoccur */
    private Runnable mSpeedRunnable = new Runnable() {
        @Override
        public void run() {
            speedDelay = !speedDelay;
            Log.d(TAG, "Speed delay " + speedDelay);
            mHandler.postDelayed(mSpeedRunnable, MINUTE_DELAY_TIME * 60 * 1000);
            if(!speedDelay) mHandler.removeCallbacks(mSpeedRunnable);
        }
    };

    /* Delay for rpm message to reoccur */
    private Runnable mRpmRunnable = new Runnable() {
        @Override
        public void run() {
            rpmDelay = !rpmDelay;
            Log.d(TAG, "Rpm delay " + rpmDelay);
            mHandler.postDelayed(mRpmRunnable, MINUTE_DELAY_TIME * 60 * 1000);
            if(!rpmDelay) mHandler.removeCallbacks(mRpmRunnable);
        }
    };

    /* Delay for accel message to reoccur */
    private Runnable mAccelRunnable = new Runnable() {
        @Override
        public void run() {
            accelDelay = !accelDelay;
            Log.d(TAG, "Accel delay " + accelDelay);
            mHandler.postDelayed(mAccelRunnable, MINUTE_DELAY_TIME * 60 * 1000);
            if(!accelDelay) mHandler.removeCallbacks(mAccelRunnable);
        }
    };

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        // Wait at least 10 seconds before running this after a prior message throwing out older messages
        while(!reentrant) {
            int num = messageEvent.getData().length > 0 ? messageEvent.getData()[0] : -1;
            if(num == 0 && rpmDelay) return;
            else if(num == 1 && speedDelay) return;
            else if(num == 3 && accelDelay) return;
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
                    if(rpmDelay) return;
                    mRpmRunnable.run();
                    Log.d(TAG, "RPM Message");
                    vibrator.vibrate(new long[] {0, 750, 500, 750}, -1);  // long[] {how long to wait before starting, how long to vibrate for, ...}
                    break;
                case 1:
                    if(speedDelay) return;
                    mSpeedRunnable.run();
                    Log.d(TAG, "Speed Message");
                    vibrator.vibrate(new long[] {0, 2000}, -1);
                    break;
                case 2:
                    if(accelDelay) return;
                    mAccelRunnable.run();
                    Log.d(TAG, "Accel Message");
                    vibrator.vibrate(new long[] {0, 750, 500, 750, 500, 750}, -1);
                    break;
                default:
                    //vibrator.vibrate(100);
                    break;
            }
        }
        super.onMessageReceived(messageEvent);
    }
}
