package com.ev_coach.ev_coach;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.UnsupportedEncodingException;

public class MainActivity extends WearableActivity implements MessageApi.MessageListener {

    private BoxInsetLayout mContainerView;
    private static final String TAG = "WearableActivity";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        Wearable.MessageApi.addListener(client, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Wearable.MessageApi.removeListener(client, this);
        client.disconnect();

        super.onStop();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String temp;
        try {
            temp = new String(messageEvent.getData(), "UTF-8");
        } catch(UnsupportedEncodingException ex) {
            temp = "hi";
        }
        Log.d(TAG, "Message received " + temp);
        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        if(!vibrator.hasVibrator()) {
            Log.d(TAG, "NO VIBRATOR ON THIS");
        }
        vibrator.vibrate(1000);
    }
}
