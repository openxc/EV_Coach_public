package com.ev_coach.ev_coach;

import android.os.Vibrator;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


public class VibrationListenerService extends WearableListenerService {

    private static final String TAG = "VibrationService";
    private final String WEAR_VIBRATE_PATH = "/ev-vibrate";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "Message received " + messageEvent.getSourceNodeId());
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        if(!vibrator.hasVibrator()) {
            Log.d(TAG, "NO VIBRATOR ON THIS");
            vibrator.vibrate(1000);
        }
        super.onMessageReceived(messageEvent);
    }
}
