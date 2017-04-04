package com.ford.ev_coach;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A intent service launched by the StarterActivity and then saves all the data processed
 * to the local database.
 */
public class DBSyncService extends IntentService {

    private static final String TAG = DBSyncService.class.getSimpleName();

    public DBSyncService() {
        super("DBSyncService");
    }

    /**
     * Processes database information.
     *
     * @param workIntent - the intent passed and data that should be processed
     */
    @Override
    protected void onHandleIntent(Intent workIntent) {


        /* Grab the extra doubles for scores put in the intent */
        double speedScore = workIntent.getDoubleExtra(DBTableContract.OverviewTableEntry.COLUMN_VEHICLE_SPEED_SCORE, 0);
        double engineScore = workIntent.getDoubleExtra(DBTableContract.OverviewTableEntry.COLUMN_ENGINE_SPEED_SCORE, 0);
        double mpgeScore = workIntent.getDoubleExtra(DBTableContract.OverviewTableEntry.COLUMN_MPGE_SCORE, 0);
        double accelScore = workIntent.getDoubleExtra(DBTableContract.OverviewTableEntry.COLUMN_ACCELERATOR_SCORE, 0);
        double totalScore = speedScore + engineScore + mpgeScore + accelScore;

        /* Grab the date and put it in a nice format */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.US);
        String date = simpleDateFormat.format(new Date());

        /* Get the database reference */
        EVCoachDBHelper dbHelper = new EVCoachDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /* Put the values within an object to be sent to the database */
        ContentValues values = new ContentValues();
        values.put(DBTableContract.OverviewTableEntry.COLUMN_ACCELERATOR_SCORE, accelScore);
        values.put(DBTableContract.OverviewTableEntry.COLUMN_ENGINE_SPEED_SCORE, engineScore);
        values.put(DBTableContract.OverviewTableEntry.COLUMN_MPGE_SCORE, mpgeScore);
        values.put(DBTableContract.OverviewTableEntry.COLUMN_TIMESTAMP, date);
        values.put(DBTableContract.OverviewTableEntry.COLUMN_TOTAL_SCORE, totalScore);
        values.put(DBTableContract.OverviewTableEntry.COLUMN_VEHICLE_SPEED_SCORE, speedScore);

        /* Insert the values within the database */
        Log.d(TAG, "Inserting values vehicle_speed = " + speedScore + "  mpge = " + mpgeScore +  " engine = " + engineScore + " accel = " + accelScore);
        db.insert(DBTableContract.OverviewTableEntry.TABLE_NAME, null, values);
        db.close();

    }
}
