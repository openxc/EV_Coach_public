package com.ford.ev_coach;

import android.os.Vibrator;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


public class VibrationListenerService extends WearableListenerService {

    private static final String TAG = "VibrationService";
    private final String WEAR_VIBRATE_PATH = "/test";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "Message received " + messageEvent.getSourceNodeId());
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        String message = new String(messageEvent.getData());

        if(vibrator.hasVibrator()) {
            Log.d(TAG, "NO VIBRATOR ON THIS");

            // Different vibration pattern depending on the message contents
            switch(message) {
                case "RPM":
                    vibrator.vibrate(new long[] {200, 500}, -1);
                    break;
                case "SPEED":
                    vibrator.vibrate(new long[] {200, 100}, -1);
                    break;
                case "ACCELERATION":
                    vibrator.vibrate(new long[] {200, 250}, -1);
                    break;
                default:
                    break;
            }
        }
        super.onMessageReceived(messageEvent);
    }
}
