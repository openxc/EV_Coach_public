package com.ev_coach.ev_coach;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ben Vesel on 11/25/2016.
 *
 * A SQLLiteOpenHelper class which simplifies the process for creating and removing tables on an
 * upgrade or downgrade of the database version.
 */
public class EVCoachDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ev_coach.db";
    private static final String TAG = "EVCoachDBHelper";

    /* Create overview table SQL statement */
    private static final String SQL_CREATE_OVERVIEW_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DBTableContract.OverviewTableEntry.TABLE_NAME + " ("
            + DBTableContract.OverviewTableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DBTableContract.OverviewTableEntry.COLUMN_TOTAL_SCORE + " REAL,"
            + DBTableContract.OverviewTableEntry.COLUMN_ACCELERATOR_SCORE + " REAL,"
            + DBTableContract.OverviewTableEntry.COLUMN_ENGINE_SPEED_SCORE + " REAL,"
            + DBTableContract.OverviewTableEntry.COLUMN_MPGE_SCORE + " REAL,"
            + DBTableContract.OverviewTableEntry.COLUMN_VEHICLE_SPEED_SCORE + " REAL,"
            + DBTableContract.OverviewTableEntry.COLUMN_TIMESTAMP + " TEXT);";

    /* Drop SQL table statement */
    private static final String SQL_DROP_OVERVIEW_TABLE =
            "DROP TABLE IF EXISTS " + DBTableContract.OverviewTableEntry.TABLE_NAME;

    /**
     * Initializes the database with a name and the database version
     *
     * @param context - Context passed in by the caller
     */
    public EVCoachDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates the Overview table as defined by the DBTableContract class if it doesn't already
     * exist
     *
     * @param db - the associated SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_OVERVIEW_TABLE);
    }

    /**
     * Drops the current table that exists (if it exists) as defined by the DBTableContract class,
     * and then recreates the new updated table.
     *
     * @param db - the database being upgraded
     * @param oldVersion - the old database version
     * @param newVersion - the new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_OVERVIEW_TABLE);
        Log.i(TAG, "Upgrading database version");
        onCreate(db);
    }

    /**
     * Drops the current table that exists (if it exists) as defined by the DBTableContract class,
     * and then recreates the new updated table.
     *
     * @param db - the database being downgraded
     * @param oldVersion - the old database version
     * @param newVersion - the new database version
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_OVERVIEW_TABLE);
        Log.i(TAG, "Downgrading database version");
        onCreate(db);
    }
}
