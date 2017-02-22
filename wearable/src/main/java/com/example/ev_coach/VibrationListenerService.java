package com.example.ev_coach;

import android.os.Vibrator;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;


public class VibrationListenerService extends WearableListenerService {

    private GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Wearable.API)
            .build();
    private static final String TAG = "VibrationService";
    private boolean nodeConnected = false;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        Log.d(TAG, "Message received " + messageEvent.getSourceNodeId());
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        if(!vibrator.hasVibrator()) {
            Log.d(TAG, "NO VIBRATOR ON THIS");
        }
    }
}
